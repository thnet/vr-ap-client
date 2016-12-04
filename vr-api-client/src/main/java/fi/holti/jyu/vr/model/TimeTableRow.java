package fi.holti.jyu.vr.model;

import java.util.ArrayList;
import java.util.List;

public class TimeTableRow {
	private String stationsShortCode;
	private String type;
	private String scheduledTime;
	private String actualTime;
	private String differenceInMinutes;
	private List<Cause> causes = new ArrayList<Cause>();

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
	
	public List<Cause> getCauses() {
		return causes;
	}
	public void setCauses(List<Cause> causes) {
		this.causes = causes;
	}
}
