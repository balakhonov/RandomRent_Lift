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
@Table(name = "district")
public class District {

	private int id;
	private String name;

	private City city;
	private Integer cityId;


	public District() {
		// no code
	}


	public District(District d) {
		this.id = d.getId();
		this.name = d.getName();
		this.city = d.getCity();
		this.cityId = d.getCityId();
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


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityId", nullable = false)
	public City getCity() {
		return city;
	}


	public void setCity(City city) {
		this.city = city;
	}


	@Column(name = "cityId", insertable = false, updatable = false, nullable = false)
	public Integer getCityId() {
		return cityId;
	}


	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

}
