package logtrak_crm.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ItemDto {
	@NotEmpty(message = "The name is required")
	private String fullname;
	
	@NotEmpty(message = "The brand is required")
	private String brand;
	
	@NotEmpty(message = "The category is required")
	private String category;
	
	@Size(min = 10, max = 2000, message = "The description should be between 10 and 2000 characters")
	private String description;
	
	private MultipartFile imagefilename;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImagefilename() {
		return imagefilename;
	}

	public void setImagefilename(MultipartFile imagefilename) {
		this.imagefilename = imagefilename;
	}
	
	

}
