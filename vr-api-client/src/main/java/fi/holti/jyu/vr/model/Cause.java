package fi.holti.jyu.vr.model;

public class Cause {
	private String detailedCategoryCode;
	private String categoryCode;
	// Each cause is associated with departed train and station
	private String stationsShortCode;
	private String trainNumber;
	private String departureDate;

	public String getStationsShortCode() {
		return stationsShortCode;
	}

	public void setStationsShortCode(String stationsShortCode) {
		this.stationsShortCode = stationsShortCode;
	}

	public String getDetailedCategoryCode() {
		return detailedCategoryCode;
	}

	public void setDetailedCategoryCode(String detailedCategoryCode) {
		this.detailedCategoryCode = detailedCategoryCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

}
