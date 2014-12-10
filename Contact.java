public class Contact {
	private String fName;
	private String lName;
	private String phone;
	private String mobile;
	private String company;
	private String picture;
	
	public Contact(String fName, String lName, String phone, String mobile, String company, String picture) {
		this.fName = fName;
		this.lName = lName;
		this.phone = phone;
		this.mobile = mobile;
		this.company = company;
		this.picture = picture;
	}
	
	public void updateAll(String fName, String lName, String phone, String mobile, String company, String picture) {
		this.fName = fName;
		this.lName = lName;
		this.phone = phone;
		this.mobile = mobile;
		this.company = company;
		this.picture = picture;
	}
	
	public String getFName() {
		return fName;
	}
	public void setFName(String fName) {
		this.fName = fName;
	}
	
	public String getLName() {
		return lName;
	}
	public void setLName(String lName) {
		this.lName = lName;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getFullName() {
		return lName + ", " + fName;
	}
}