import java.util.ArrayList;
import java.sql.Date;

public class Calendar {
	private ArrayList<CalItem> calItems = new ArrayList<CalItem>();
	private String saveFile;
	
	public Calendar(String file) {
		// Load calendar data from file here
		saveFile = file;
		addItem(System.currentTimeMillis(), "10:12", "Home", "Subject", "Description");
		addItem(System.currentTimeMillis(), "10:42", "Home", "New Subject", "lol");
		addItem(System.currentTimeMillis(), "10:12", "Home", "Subject", "Description");
		addItem(System.currentTimeMillis(), "10:42", "Home", "New Subject", "lol");
		addItem(System.currentTimeMillis(), "10:12", "Home", "Subject", "Description");
		addItem(System.currentTimeMillis(), "10:42", "Home", "New Subject", "lol");
	}
	
	public void saveCalendar() {
		// Save calendar data to saveFile here
	}
	
	public void addItem(long dateTime, String time, String location, String subject, String description) {
		calItems.add(new CalItem(dateTime, time, location, subject, description));
	}
	
	public void deleteItem(CalItem delete) {
		calItems.remove(delete);
	}
	
	public void deleteAll() {
		calItems.clear();
	}
	
	public ArrayList<String> getPerDate(long dateTime) {
		ArrayList<String> allDesc = new ArrayList<String>();
		for (int i = 0; i < calItems.size(); i++) {
			CalItem thisItem = calItems.get(i);
			if (new Date(dateTime).toString().equals(new Date(thisItem.getDateTime()).toString())) {
				allDesc.add(thisItem.getDesc());
			}
		}
		return allDesc;
	}
	
	public ArrayList<CalItem> getAppointPerDate(long dateTime) {
		ArrayList<CalItem> allAppoint = new ArrayList<CalItem>();
		for (int i = 0; i < calItems.size(); i++) {
			CalItem thisItem = calItems.get(i);
			if (new Date(dateTime).toString().equals(new Date(thisItem.getDateTime()).toString())) {
				allAppoint.add(thisItem);
			}
		}
		return allAppoint;
	}
}