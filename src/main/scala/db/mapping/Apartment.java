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
@Table(name = "apartment")
public class Apartment {

	private int id;
	private int cityId;
	private String street;
	private int roomTypeId;
	/**
	 * сниму. сдам, куплю, продам
	 */
	private int adType;

	/**
	 * длительно, посуточно
	 */
	private int period;
	private int rentById;
	private int price;
	private String title;
	private String description;
	private String mobileNumber;
	private long postedDate;
	private String hTitle;
	private String hDescription;
	private String hKeyWords;

	/* addition options */

	private boolean furnished;
	private boolean airConditioning;
	private boolean fridge;
	private boolean cableTelevision;
	private boolean washer;
	private boolean fireplace;
	private boolean internet;
	private boolean boiler;
	private boolean nearbyParking;

	private District district;
	private Integer districtId;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	@Column(name = "districtId", insertable = false, updatable = false, nullable = true)
	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(int roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public int getAdType() {
		return adType;
	}

	public void setAdType(int adType) {
		if (adType < 1 || adType > 4) {
			throw new IllegalArgumentException("Ad type(" + adType + ") should be inrange[1,4]");
		}
		this.adType = adType;
	}

	public int getRentById() {
		return rentById;
	}

	public void setRentById(int rentById) {
		this.rentById = rentById;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(long postedDate) {
		this.postedDate = postedDate;
	}

	public String gethTitle() {
		return "hTitle";
		// return hTitle;
	}

	public void sethTitle(String hTitle) {
		this.hTitle = hTitle;
	}

	public String gethDescription() {
		return "hDescription";
		// return hDescription;
	}

	public void sethDescription(String hDescription) {
		this.hDescription = hDescription;
	}

	public String gethKeyWords() {
		return "hKeyWords";
		// return hKeyWords;
	}

	public void sethKeyWords(String hKeyWords) {
		this.hKeyWords = hKeyWords;
	}

	public boolean isFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean isAirConditioning() {
		return airConditioning;
	}

	public void setAirConditioning(boolean airConditioning) {
		this.airConditioning = airConditioning;
	}

	public boolean isFridge() {
		return fridge;
	}

	public void setFridge(boolean fridge) {
		this.fridge = fridge;
	}

	public boolean isCableTelevision() {
		return cableTelevision;
	}

	public void setCableTelevision(boolean cableTelevision) {
		this.cableTelevision = cableTelevision;
	}

	public boolean isWasher() {
		return washer;
	}

	public void setWasher(boolean washer) {
		this.washer = washer;
	}

	public boolean isFireplace() {
		return fireplace;
	}

	public void setFireplace(boolean fireplace) {
		this.fireplace = fireplace;
	}

	public boolean isInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}

	public boolean isBoiler() {
		return boiler;
	}

	public void setBoiler(boolean boiler) {
		this.boiler = boiler;
	}

	public boolean isNearbyParking() {
		return nearbyParking;
	}

	public void setNearbyParking(boolean nearbyParking) {
		this.nearbyParking = nearbyParking;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "districtId", nullable = true)
	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}
}
