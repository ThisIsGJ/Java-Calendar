public class CalItem {
	private long dateTime;
	private String time;
	private String location;
	private String subject;
	private String description;
	
	public CalItem(long dateTime, String time, String location, String subject, String description) {
		this.dateTime = dateTime;
		this.time = time;
		this.location = location;
		this.subject = subject;
		this.description = description;
	}
	
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getSubj() {
		return subject;
	}
	public void setSubj(String subject) {
		this.subject = subject;
	}
	
	public String getDesc() {
		return description;
	}
	public void setDesc(String desc) {
		this.description = description;
	}
	
}