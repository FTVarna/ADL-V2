import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateAppointmentActivity extends Activity implements OnClickListener{
	
	private Database db;
	private Toast toast;
	private String appointmentDate;
	private EditText appointmentTitle;
	private TimePicker appointmentTime;
	private EditText appointmentDetails;
	private TextView appointmentDateText;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_appointment_activity);
		appointmentDateText = (TextView) findViewById(R.id.new_app_date_text);
		appointmentTitle = (EditText) findViewById(R.id.app_title);
		appointmentDetails = (EditText) findViewById(R.id.app_details);
		appointmentTime = (TimePicker) findViewById(R.id.timePicker);
		appointmentTime.setIs24HourView(true);
		appointmentTime.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		View saveButton = findViewById(R.id.save_button);
		saveButton.setOnClickListener(this);
		
		//get passing information from the previous activity
		Bundle calendarDate = getIntent().getExtras();
		if(calendarDate!=null){
			appointmentDate = calendarDate.getString("SelectedDate");
			appointmentDateText.setText("Appointment Date: " + appointmentDate);
		}
		
		db = new Database(this);
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		
		switch(button.getId()){
		
		case R.id.save_button:
			
			String title = appointmentTitle.getText().toString().toLowerCase();
			int timeHour = appointmentTime.getCurrentHour();
			int timeMinutes = appointmentTime.getCurrentMinute();
			String time = String.format("%02d:%02d", timeHour, timeMinutes);
			String details = appointmentDetails.getText().toString();
		
			//check fields
			if(title.isEmpty() || details.isEmpty()){
				
				displayMessage("Please fill in all fields.");
				
			}else{
			
			//check if appointment exists with the same name on same date
			if(db.appointmentExists(title,appointmentDate)){
				
				displayMessage("Appointment '" + title + "' already exists, please choose a different event title");
				
			}else{
			
				//create the appointment
			db.newAppointment(new Appointment(title, time, appointmentDate, details));
			}	
		}
		
		}
		
	}
	
	/**
	 * Takes a parameter message and displays to the user interface.
	 * Used to notify, warn user.
	 * @param messsage
	 * */
	public void displayMessage(String message) {
		toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}
}