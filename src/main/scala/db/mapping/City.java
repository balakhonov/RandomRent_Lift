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
@Table(name = "cities")
public class City {

	private int id;
	private String name;
	private double lat;
	private double lon;

	private Country country;
	private Integer countryId;
	private Province province;
	private Integer provinceId;

	public City() {
		// no code
	}

	public City(City c) {
		this.id = c.id;
		this.name = c.name;
		this.province = c.province;
		this.provinceId = c.provinceId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provinceId", nullable = false)
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	@Column(name = "provinceId", insertable = false, updatable = false, nullable = false)
	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "countryId", nullable = false)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Column(name = "countryId", insertable = false, updatable = false, nullable = false)
	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
}
