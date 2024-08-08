package logtrak_crm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import logtrak_crm.dto.UserDto;
import logtrak_crm.model.Item;
import logtrak_crm.model.User;
import logtrak_crm.repositories.ItemRepository;
import logtrak_crm.repositories.UserRepository;
import logtrak_crm.service.UserService;

@Controller
public class UserController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }

    @PostMapping ("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully");
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        List<Item> bookedItems = itemRepository.findByFullname(user.getFullname());

        model.addAttribute("user", user);
        model.addAttribute("bookedItems", bookedItems);
        return "user";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }
}

