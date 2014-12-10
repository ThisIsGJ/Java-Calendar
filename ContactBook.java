import java.util.ArrayList;

public class ContactBook {
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private String saveFile;
	
	public ContactBook(String file) {
		// Load calendar data from file here
		saveFile = file;
		addContact("Jamie", "Warburton", "01903 882928", "07819 927842", "", "");
		addContact("George", "Aldridge", "01903 812424", "07881 929382", "", "");
	}
	
	public void saveContacts() {
		// Save calendar data to saveFile here
	}
	
	public void addContact(String fName, String lName, String phone, String mobile, String company, String picture) {
		contacts.add(new Contact(fName, lName, phone, mobile, company, picture));
	}
	
	public void deleteItem(Contact contact) {
		contacts.remove(contact);
	}
	
	public void deleteAll() {
		contacts.clear();
	}
	
	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> allCont = new ArrayList<Contact>();
		for (int i = 0; i < contacts.size(); i++) {
			allCont.add(contacts.get(i));
		}
		return allCont;
	}
}