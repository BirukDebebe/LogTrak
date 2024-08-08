package logtrak_crm.controller;

import java.nio.file.Files;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import logtrak_crm.dto.ItemDto;
import logtrak_crm.model.Item;
import logtrak_crm.model.User;
import logtrak_crm.repositories.ItemRepository;
import logtrak_crm.repositories.UserRepository;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemRepository repo;
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping({"", "/"})
	public String showItemList(Model model) {
		List<logtrak_crm.model.Item> items = repo.findAll();
		model.addAttribute("items", items);
		return "items";
	}
	
	@GetMapping ("/create")
	public String showCreatePage(Model model) {
		ItemDto itemDto = new ItemDto();
		model.addAttribute("itemDto", itemDto);
		return "CreateItem";
	}
	
	@PostMapping("/create")
	public String createItem(
			@Valid @ModelAttribute ItemDto itemDto,
			BindingResult result,
			Model model
			) {
		
		if (itemDto.getImagefilename().isEmpty()) {
			result.addError(new FieldError("itemDto", "imagefilename", "The image file is required"));
		}
		
		if (result.hasErrors()) {
			return "CreateItem";
		}
		
		// save the file image
		MultipartFile imagefilename = itemDto.getImagefilename();
		Date timeofarrival = new Date();
		String storageFileName = timeofarrival.getTime() + "_" + imagefilename.getOriginalFilename();
		
		try {
			String uploadDir = "/img/";
			Path uploadPath = Paths.get(uploadDir);
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = imagefilename.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir, storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		Item item = new Item();
		item.setFullname(itemDto.getFullname());
		item.setBrand(itemDto.getBrand());
		item.setCategory(itemDto.getCategory());
		item.setDescription(itemDto.getDescription());
		item.setTimeofarrival(timeofarrival);
		item.setImagefilename(storageFileName);
		
		repo.save(item);
		
		// Fetch the user and update the model
		User user = userRepo.findByFullname(item.getFullname());
		if (user != null) {
			List<Item> bookedItems = repo.findByFullname(user.getFullname());
			model.addAttribute("user", user);
			model.addAttribute("bookedItems", bookedItems);
		}
		
		return "redirect:/items";
	}
	
	@GetMapping("/edit/{id}")
	public String editItem(
			Model model,
			@PathVariable int id
			) {
		
		try {
			Item item = repo.findById(id).get();
			model.addAttribute("item", item);
			
			ItemDto itemDto = new ItemDto();
			itemDto.setFullname(item.getFullname());
			itemDto.setBrand(item.getBrand());
			itemDto.setCategory(item.getCategory());
			itemDto.setDescription(item.getDescription());
			
			model.addAttribute("itemDto", itemDto);
			
		} catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/items";
		}
		
		return "EditItem";
	}
	
	@PostMapping("/edit/{id}")
	public String updateItem(
		Model model,
		@PathVariable int id,
		@Valid @ModelAttribute ItemDto itemDto,
		BindingResult result
		) {
		
		try {
			Item item = repo.findById(id).get();
			model.addAttribute("item", item);
			
			if (result.hasErrors()) {
				return "EditItem";
			}
			
			if (!itemDto.getImagefilename().isEmpty()) {
				//delete old image
				String uploadDir = "/img/";
				Path oldImagePath = Paths.get(uploadDir + item.getImagefilename());
				
				try {
					Files.delete(oldImagePath);
				} catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				// save new image file
				MultipartFile image = itemDto.getImagefilename();
				Date timeofarrival = new Date();
				String storageFileName = timeofarrival.getTime() + "_" + image.getOriginalFilename();
				
				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream,  Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
				item.setImagefilename(storageFileName);
			}
			
			item.setFullname(itemDto.getFullname());
			item.setBrand(itemDto.getBrand());
			item.setCategory(itemDto.getCategory());
			item.setDescription(itemDto.getDescription());
			
			repo.save(item);
			
			// Fetch the user and update the model
			User user = userRepo.findByFullname(item.getFullname());
			if (user != null) {
				List<Item> bookedItems = repo.findByFullname(user.getFullname());
				model.addAttribute("user", user);
				model.addAttribute("bookedItems", bookedItems);
			}
			
		} catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/items";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteItem(
			@PathVariable int id,
			Model model
			) {
		
		try {
			Item item = repo.findById(id).get();
			
			// Fetch the user before deleting the item
			User user = userRepo.findByFullname(item.getFullname());
			
			//delete the image
			Path imagePath = Paths.get("/img/" + item.getImagefilename());
			
			try {
				Files.delete(imagePath);
			} catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			//delete the item
			repo.delete(item);
			
			// Update the model with the user's updated booked items
			if (user != null) {
				List<Item> bookedItems = repo.findByFullname(user.getFullname());
				model.addAttribute("user", user);
				model.addAttribute("bookedItems", bookedItems);
			}
			
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/items";
	}
}
