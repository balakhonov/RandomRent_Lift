package db.mapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "country")
public class Country {

	private int id;
	private String name;


	public Country() {
		// no code
	}


	public Country(Country c) {
		this.id = c.id;
		this.name = c.name;
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


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n id: " + id);
		sb.append("\n name: " + name);

		return sb.toString();
	}
}
