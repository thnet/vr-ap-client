package fi.holti.jyu.vr.model.simple;

public class SimpleTimetableRow {
	private String trainNumber;
	private String departureDate;
	private String stationsShortCode;
	private String type;
	private String scheduledTime;
	private String actualTime;
	private String differenceInMinutes;
	private String weatherLocationId;

	public SimpleTimetableRow(String trainNumber, String departureDate, String stationsCode, String type,
			String scheduledTime, String actualTime, String differenceInMinutes) {
		this.trainNumber = trainNumber;
		this.departureDate = departureDate;
		this.stationsShortCode = stationsCode;
		this.type = type;
		this.scheduledTime = scheduledTime;
		this.actualTime = actualTime;
		this.differenceInMinutes = differenceInMinutes;
	}

	public String getStationsShortCode() {
		return stationsShortCode;
	}

	public void setStationsShortCode(String stationsShortCode) {
		this.stationsShortCode = stationsShortCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getDifferenceInMinutes() {
		return differenceInMinutes;
	}

	public void setDifferenceInMinutes(String differenceInMinutes) {
		this.differenceInMinutes = differenceInMinutes;
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

	public String getWeatherLocationId() {
		return weatherLocationId;
	}

	public void setWeatherLocationId(String weatherLocationId) {
		this.weatherLocationId = weatherLocationId;
	}
}
