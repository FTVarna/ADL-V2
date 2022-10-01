package uk.ac.westminster.ecwm511.cw2;

import com.example.coursework2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ViewAppointmentDetailsActivity extends Activity implements OnClickListener{
	
	private Database db;
	private int appointmentId;
	private TextView appointmentDateText;
	private TextView appointmentTitleText;
	private TextView appointmentTimeText;
	private TextView appointmentDetailsText;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_appointment_details);
		appointmentDateText = (TextView) findViewById(R.id.view_app_date_text);
		appointmentTitleText = (TextView) findViewById(R.id.view_app_title_text);
		appointmentTimeText = (TextView) findViewById(R.id.view_app_time_text);
		appointmentDetailsText = (TextView) findViewById(R.id.view_app_detail_text);
		View modifyButton = findViewById(R.id.view_modify_button);
		modifyButton.setOnClickListener(this);
		
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			appointmentId = extra.getInt("AppointmentId");
		}
		
		db = new Database(this);
		
		Appointment appointment = db.getAppointmentDetailsById(appointmentId);
		
		appointmentDateText.setText(appointment.getDate());
		appointmentTitleText.setText(appointment.getTitle());
		appointmentTimeText.setText(appointment.getTime());
		appointmentDetailsText.setText(appointment.getDetails());
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		
		switch(button.getId()){
		
		case R.id.view_modify_button:
			
			Intent EditAppointmentActivity = new Intent(this, EditAppointmentActivity.class);
			EditAppointmentActivity.putExtra("AppointmentId", appointmentId);
			startActivity(EditAppointmentActivity);
			
			finish();
			
			break;
		}
	}
}