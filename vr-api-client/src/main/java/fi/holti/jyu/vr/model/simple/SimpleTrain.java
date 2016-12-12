package fi.holti.jyu.vr.model.simple;

public class SimpleTrain {

	private String trainNumber;
	private String departureDate;
	private String trainType;
	private String trainCategory;
	private String scheduledTime;
	private String differenceInMinutes;

	public SimpleTrain(String trainNumber, String departureDate, String trainType, String trainCategory,
			String scheduledTime, String difference) {
		this.trainNumber = trainNumber;
		this.departureDate = departureDate;
		this.trainType = trainType;
		this.trainCategory = trainCategory;
		this.scheduledTime = scheduledTime;
		this.differenceInMinutes = difference;
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

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	public String getTrainCategory() {
		return trainCategory;
	}

	public void setTrainCategory(String trainCategory) {
		this.trainCategory = trainCategory;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getDifferenceInMinutes() {
		return differenceInMinutes;
	}

	public void setDifferenceInMinutes(String differenceInMinutes) {
		this.differenceInMinutes = differenceInMinutes;
	}

}
