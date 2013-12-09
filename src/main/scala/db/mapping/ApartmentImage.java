package db.mapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apartment_images")
public class ApartmentImage {

	private int id;
	private String name;
	private int apartmentId;

	public ApartmentImage() {
		// no code
	}

	public ApartmentImage(String name, int apartmentId) {
		this.name = name;
		this.apartmentId = apartmentId;
	}

	public ApartmentImage(ApartmentImage c) {
		this.id = c.id;
		this.name = c.name;
		this.apartmentId = c.apartmentId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(int apartmentId) {
		this.apartmentId = apartmentId;
	}

}
