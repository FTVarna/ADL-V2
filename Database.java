package uk.ac.westminster.ecwm511.cw2;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "cw2DB";
	
	// Appointment table name
    private static final String TABLE_APPOINTMENT = "appointment";
    private static final String TABLE_LANGUAGE = "language";

    // Appointment Table Columns names
    private static final String APP_ID = "id";
    private static final String APP_TITLE = "title";
    private static final String APP_TIME = "time";
    private static final String APP_DATE = "date";
    private static final String APP_DETAILS = "details";
    
    //Language Table Columns names
    private static final String LANG_ID = "id";
    private static final String LANG_NAME = "language";

    private static final String[] APPOINTMENT_COLUMNS = {APP_ID, APP_TITLE, APP_TIME, APP_DATE, APP_DETAILS};
    private static final String[] LANGUAGE_COLUMNS = {LANG_ID, LANG_NAME};
    
    private Context currentContext;
	private java.util.Date date;
	private java.sql.Date sqlDate;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.currentContext = context;
	}

	/**
	 * Called when the database file does not exist and
	 * creates tables that is needed for application to run.
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String appointmentTableSql = "CREATE TABLE appointment ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, " + "time TIME, " + "date DATE, " + "details TEXT )";
		String languageTableSql = "CREATE TABLE language ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "language TEXT )";
		
		db.execSQL(appointmentTableSql);
		db.execSQL(languageTableSql);
	}
	
	/**
	 * Called when the database file exists 
	 * but the stored version number is lower than requested in constructor
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS appointment");
		db.execSQL("DROP TABLE IF EXISTS language");

		this.onCreate(db);
	}

	/**
	 * Creates a new appointment.
	 * @param appointment object
	 * */
	public void newAppointment(Appointment appointment) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(APP_TITLE, appointment.getTitle());
		values.put(APP_TIME, appointment.getTime());
		values.put(APP_DATE, getLongDate(appointment.getDate()));
		values.put(APP_DETAILS, appointment.getDetails());

		db.insert(TABLE_APPOINTMENT, null, values);

		db.close();
		
		((CreateAppointmentActivity) currentContext).displayMessage("Appointment is saved");
		((CreateAppointmentActivity) currentContext).finish();
	}
	
	/**
	 * Accepts id as a parameter, tries to match an
	 * appointment's id to the parameter and returns
	 * if found a record.
	 * @param id of the appointment
	 * @return appointment
	 * */
	public Appointment getAppointmentDetailsById(int id){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor =  db.query(TABLE_APPOINTMENT, APPOINTMENT_COLUMNS," id = ?",
	            new String[] { String.valueOf(id) },
	            null,
	            null,
	            null,
	            null);
		
	    if(cursor != null)
	    	
	    	cursor.moveToFirst();
	    
	    	Appointment appointment = new Appointment(cursor.getInt(0),cursor.getString(1),cursor.getString(2),getStringDate(cursor.getString(3)),cursor.getString(4));

	    	return appointment;
	}
	
	/**
	 * Checks if appointment exists and returns a boolean. Used to 
	 * check if appointment exists before inserting an appointment to
	 * avoid duplicate appointment title on the same date.
	 * @param	title of the appointment
	 * @param	date of the appointment
	 * @return 	boolean
	 * */
	public boolean appointmentExists(String title, String date){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor =  db.query(TABLE_APPOINTMENT, APPOINTMENT_COLUMNS," title = ? AND date = ? ",
	            new String[] { title, String.valueOf(getLongDate(date))}, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			return true;//already exists
		}
		
	    cursor.close();
        db.close();
        
        return false;
	}
	
	/**
	 * Deletes all appointments for the specified date.
	 * @param date
	 * */
	public void deleteAll(String date){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_APPOINTMENT," date = '"+getLongDate(date)+"'", null);
		
		((DeleteOptionsActivity) currentContext).displayMessage("All appointments on "+date+" deleted");
		((DeleteOptionsActivity) currentContext).finish();
	}
	
	/**
	 * Deletes the chosen appointment. Deletes the appointment
	 * that id matches the parameter.
	 * @param id of the appointment
	 * */
	public void deleteAppointment(int id){

      SQLiteDatabase db = this.getWritableDatabase();

      db.delete(TABLE_APPOINTMENT," id = "+id,null);

      db.close();
      
		((DeletionListActivity) currentContext).displayMessage("Appointment is deleted");
		((DeletionListActivity) currentContext).finish();
	}
	
	/**
	 * Returns all appointments after a specific date. It is used as part
	 * of the search functionality.
	 * @param currentDate
	 * @return list of the found appointments
	 * */
	public ArrayList<Appointment> getFutureAppointments(String currentDate){
		
		SQLiteDatabase db = this.getReadableDatabase();
		   
		String sqlQuery = "SELECT * from appointment WHERE date > " + getLongDate(currentDate) + " ORDER by date ASC";
		
		long longDate = getLongDate(currentDate);
		Log.d("LONG", String.valueOf(longDate));
		
		String longDateString = String.valueOf(longDate);
		Log.d("STRING", getStringDate(longDateString));
		
		ArrayList<Appointment> list = new ArrayList<Appointment>();
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		
		if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
            		
                	Appointment appointment = new Appointment(cursor.getInt(0),cursor.getString(1),cursor.getString(2),getStringDate(cursor.getString(3)),cursor.getString(4));
                    list.add(appointment);

                } while (cursor.moveToNext());

            }
            cursor.close();
            db.close();
		}
		return list;
	}
	
	
	/**
	 * Returns appointments for specific date. Called when user
	 * chooses to perform an action that requires an access
	 * to the appointments of that date.
	 * @param selectedDate
	 * @return list of the appointments
	 * */
	public ArrayList<Appointment> getAppointments(String selectedDate){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		ArrayList<Appointment> list = new ArrayList<Appointment>();
		
		Cursor cursor = db.query(TABLE_APPOINTMENT, APPOINTMENT_COLUMNS, " date = ? ORDER by time ASC", new String[] {String.valueOf(getLongDate(selectedDate))}, null, null, null, null);
		
		if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                	
                	Appointment appointment = new Appointment(cursor.getInt(0),cursor.getString(1),cursor.getString(2),getStringDate(cursor.getString(3)),cursor.getString(4));
                    list.add(appointment);

                } while (cursor.moveToNext());
            }
            
            cursor.close();
            db.close();
		}
		return list;
	}
	
	/**
	 * Updates a date of the appointment. Invoked when user
	 * moves an appointment from one date to other.
	 * @param appointment object
	 * @return i	integer 0 or 1 depending on the success of the operation 
	 * */
	public int updateAppointmentDate(Appointment appointment) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("date", getLongDate(appointment.getDate()));
	 
	    int i = db.update(TABLE_APPOINTMENT,
	            values,
	            APP_ID+" = ?",
	            new String[] { String.valueOf(appointment.getId()) });
	    db.close();
	    
	   return i;
	}
	
	/**
	 * Accepts the arraylist containing all appointments, and the entered search word by the user.
	 * Returns the an arraylist of appointment objects that contains the searchWord in either
	 * in title or detail field.
	 * @param searchWord	string entered by the user
	 * @param currentDate	current day's date
	 * @return foundAppointments	list of appointments that matches the criteria
	 * */
	public ArrayList<Appointment> findAppointment(String searchWord,String currentDate){
		
		String word = searchWord.toLowerCase(Locale.ENGLISH);
		
		ArrayList<Appointment> allAppointments = this.getFutureAppointments(currentDate);
		
		ArrayList<Appointment> foundAppointments = new ArrayList<Appointment>();
		
		for(int i=0; i<allAppointments.size(); i++){
			if(allAppointments.get(i).getTitle().contains(word) || allAppointments.get(i).getDetails().contains(word) ){
				foundAppointments.add(allAppointments.get(i));
			}
		}
		
		return foundAppointments;
	}
	
	/**
	 * Updates the details of the appointment, called when
	 * user edits and attempt to overwrite the details of the appointment.
	 * @param appointment
	 * @return i	Integer 0 or 1 depending on the success of the operation.
	 * */
	public int updateAppointmentDetails(Appointment appointment) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("title", appointment.getTitle());
	    values.put("time", appointment.getTime());
	    values.put("details", appointment.getDetails());
	 
	    int i = db.update(TABLE_APPOINTMENT,
	            values,
	            APP_ID+" = ?",
	            new String[] { String.valueOf(appointment.getId()) });

	    db.close();
	    
	   return i;
	}
	
	/**
	 * Updates the details of the appointment, called if user
	 * chooses to save the translated version of the appointment
	 * details.
	 * @param appointment
	 * @return i	Integer 0 or 1 depending on the success of the operation.
	 * */
	public int updateAppointmentDetails(int id, String details) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("details", details);
	 
	    int i = db.update(TABLE_APPOINTMENT,
	            values,
	            APP_ID+" = ?",
	            new String[] { String.valueOf(id) });
	    db.close();
	    
	   return i;
	}
	
	/**
	 * Returns list of the languages stored. Called when the
	 * TranslateActivity is created, the returned arraylist is used to
	 * populate the comboboxes used for the selection of the languages.
	 * @return languages
	 * */
	public ArrayList<String> getLanguages(){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		ArrayList<String> languages = new ArrayList<String>();
		
		Cursor cursor = db.query(TABLE_LANGUAGE, LANGUAGE_COLUMNS, null, null, null, null, null);
		
		if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
               
                	languages.add(cursor.getString(1));

                } while (cursor.moveToNext());

            }
            cursor.close();
            db.close();
		}
		return languages;
	}
	
	/**
	 * Adds a new language to the database. Invoked when user
	 * selects a languages and wishes to use it for the translation.
	 * @param language
	 * */
	public void addLanguage(String language){

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LANG_NAME, language);

		db.insert(TABLE_LANGUAGE, null, values);

		db.close();
		
		((LanguageAddActivity) currentContext).displayMessage("Language is added");
	}
	
	/**
	 * Deletes a language from the database.
	 * @param language
	 * */
	public void deleteLanguage(String language){
    SQLiteDatabase db = this.getWritableDatabase();

    db.delete(TABLE_LANGUAGE," language = '"+language+"'",null);

    db.close();
    
		((LanguageDeleteActivity) currentContext).displayMessage("Language is deleted");
		((LanguageDeleteActivity) currentContext).finish();
	}
	
	/**
	 * Accepts the unformatted date from the database and
	 * Returns a formatted date which is assigned to a String variable
	 * and used to construct the Appointment object.
	 * @param longDate
	 * @return dateFormat.format(date)	date as String
	 * */
	private String getStringDate(String longDate) {
		date = new Date(Long.valueOf(longDate));
		return dateFormat.format(date);
	}

	/**
	 * Accepts a String date from "appointment.getDate()" and returns 
	 * a date as sql.date format which is saved into the database.
	 * @param stringDate
	 * @return sqlDate.getTime()	date as Long
	 * */
	private long getLongDate(String stringDate) {
		try {
			date = dateFormat.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sqlDate = new java.sql.Date(date.getTime());

		return sqlDate.getTime();
	}
}