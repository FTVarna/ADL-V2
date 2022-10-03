/**
 * Holds details of the Appointment
 * */
public class Appointment {
	
	private int id;
	private String title;
	private String date;
	private String time;
	private String details;

	/**
	 * Constructor
	 * @param	id		of the appointment
	 * @param	title	of the appointment
	 * @param	time	of the appointment
	 * @param	date	of the appointment
	 * @param	details	of the appointment
	 * */
	public Appointment(int id, String title, String time, String date, String details) {
		this.id = id;
		this.title = title;
		this.time = time;
		this.date = date;
		this.details = details;
	}
	
	public Appointment(String title, String time, String date, String details) {
		this.title = title;
		this.time = time;
		this.date = date;
		this.details = details;
	}

	/**
	 * Returns the appointment id
	 * @return id
	 * */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of the appointment 
	 * @param id
	 * */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the appointment title
	 * @return id
	 * */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of the appointment 
	 * @param title
	 * */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the appointment date
	 * @return date
	 * */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date of the appointment 
	 * @param date
	 * */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the appointment time
	 * @return time
	 * */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time of the appointment 
	 * @param time
	 * */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Returns the appointment details
	 * @return details
	 * */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets the details of the appointment 
	 * @param details
	 * */
	public void setDetails(String details) {
		this.details = details;
	}
}
