package logtrak_crm.model;

import jakarta.persistence.Column;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;




@Entity
@Table(name="Item")
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String fullname;
	private String brand;
	private String category;
	
	
	@Column(columnDefinition ="TEXT")
	private String description;
	private Date timeofarrival; 
	private String imagefilename;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public Date getTimeofarrival() {
		return timeofarrival;
	}
	public void setTimeofarrival(Date timeofarrival) {
		this.timeofarrival = timeofarrival;
	}
	public String getImagefilename() {
		return imagefilename;
	}
	public void setImagefilename(String imagefilename) {
		this.imagefilename = imagefilename;
	}
	
	

}
