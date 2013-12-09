package db.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "province")
public class Province {

	private int id;
	private String name;

	private Country country;
	private Integer countryId;


	public Province() {
		// no code
	}


	public Province(Province p) {
		this.id = p.id;
		this.name = p.name;
		this.country = p.country;
		this.countryId = p.countryId;
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


	@Column(name = "countryId", insertable = false, updatable = false, nullable = false)
	public int getCountryId() {
		return countryId;
	}


	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "countryId", nullable = false)
	// @JsonIgnore
	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
		setCountryId(country.getId());
	}
}
