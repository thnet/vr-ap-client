package fi.holti.jyu.vr.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
	
	private String trainNumber;
	private String departureDate;
	private String trainType;
	private String trainCategory;
	private String scheduledTime;
	private String  differenceInMinutes;
	private List<TimeTableRow> timeTableRows = new ArrayList<TimeTableRow>();

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
	
	public List<TimeTableRow> getTimeTableRows() {
		return timeTableRows;
	}

	public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
		this.timeTableRows = timeTableRows;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
