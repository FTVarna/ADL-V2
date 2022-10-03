import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditAppointmentActivity extends Activity implements OnClickListener{
	
	private Appointment appointment;
	private Toast toast;
	private int appointmentId;
	private Database db;
	private TimePicker appointmentTime;
	private EditText titleTextField;
	private EditText detailsTextField;
	
	/**
	 * Constructs the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_appointment_activity);
		titleTextField = (EditText) findViewById(R.id.edit_app_title_text);
		detailsTextField = (EditText) findViewById(R.id.edit_app_details_text);
		appointmentTime = (TimePicker) findViewById(R.id.edit_timePicker);
		appointmentTime.setIs24HourView(true);
		View updateButton = findViewById(R.id.edit_update_button);
		updateButton.setOnClickListener(this);
		
		//gets the passed information from the previous activity
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			appointmentId = extra.getInt("AppointmentId");
		}
		
		db = new Database(this);
		
		appointment = db.getAppointmentDetailsById(appointmentId);
		String hour = appointment.getTime().substring(0, 2);
		String minutes = appointment.getTime().substring(3, 5);
		titleTextField.setText(appointment.getTitle());
		appointmentTime.setCurrentHour(Integer.parseInt(hour));
		appointmentTime.setCurrentMinute(Integer.parseInt(minutes));
		detailsTextField.setText(appointment.getDetails());
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		
		switch(button.getId()){
			
			case R.id.edit_update_button:
			
				int timeHour = appointmentTime.getCurrentHour();
				int timeMinutes = appointmentTime.getCurrentMinute();
				String time = String.format("%02d:%02d", timeHour, timeMinutes);
			
				appointment.setTitle(titleTextField.getText().toString().toLowerCase());
				appointment.setTime(time);
				appointment.setDetails(detailsTextField.getText().toString());
				
				int updateStatus = db.updateAppointmentDetails(appointment);
				
				if(updateStatus==1){
					displayMessage("Record Updated");
					finish();
				}else{
					displayMessage("Cannot update. Please retry.");
				}

				break;
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