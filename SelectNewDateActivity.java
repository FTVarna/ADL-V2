package uk.ac.westminster.ecwm511.cw2;

import com.example.coursework2.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class SelectNewDateActivity extends Activity implements OnClickListener{

	private CalendarView calendar;
	private Toast toast;
	private String selectedDate;
	private Database db;
	private int appointmentId;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_new_date_activity);

		View moveButton = findViewById(R.id.move_date_button);
		moveButton.setOnClickListener(this);
		
		calendar = (CalendarView) findViewById(R.id.second_calendar);
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
				month++;//increment month by 1 because it starts from 0;
				selectedDate = day + "/" + month + "/" + year;
			}
		});
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			appointmentId = Integer.parseInt(extras.getString("appointmentId"));
		}
		
		db = new Database(this);
	}
	
	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View v) {
		Log.d("DATE", selectedDate);
		Log.d("ID", Integer.toString(appointmentId));
		Appointment appointment = db.getAppointmentDetailsById(appointmentId);
		appointment.setDate(selectedDate);
		int updateStatus = db.updateAppointmentDate(appointment);
		if(updateStatus==1){
			displayMessage("Appointment Moved");
			finish();
		}else{
			displayMessage("Problem Occoured, Please Retry");
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
