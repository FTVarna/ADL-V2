package uk.ac.westminster.ecwm511.cw2;

import java.util.ArrayList;
import com.example.coursework2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAppointmentsActivity extends Activity implements OnClickListener{
	
	private Toast toast;
	private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	private String dateSelected;
	private TextView list;
	private EditText appointmentNumberTextField;
	private Database db;
	private int recordNo;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_appointmentlist_activity);
		View editButton = findViewById(R.id.edit_button);
		editButton.setOnClickListener(this);
		list = (TextView) findViewById(R.id.view_list_text);
		appointmentNumberTextField = (EditText) findViewById(R.id.view_app_id_text);
		
		Bundle calendarDate = getIntent().getExtras();
		if (calendarDate != null) {
			dateSelected = calendarDate.getString("SelectedDate");
		}
		
		db = new Database(this);
		appointmentList = db.getAppointments(dateSelected);

		if (appointmentList.size() == 0) {
			finish();
			displayMessage("No Appointments Found!");
		} else {
			
			for (int i = 0; i < appointmentList.size(); i++) {
				int idNo = i+1; //listing number
				list.setText(list.getText() + "\n" + idNo + "." + "\t\t"
						+ appointmentList.get(i).getTime() + "\t\t"
						+ appointmentList.get(i).getTitle());
				}
			}
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		
		switch(button.getId()){
		
		case R.id.edit_button:
			recordNo = Integer.parseInt(appointmentNumberTextField.getText().toString());

			//checks if digit entered matches the displayed
			if(recordNo<1 || recordNo>appointmentList.size()){
			
				displayMessage("Enter valid number");
			
			}else{
					
					finish();
					
					Intent EditAppointmentActivity = new Intent(this, EditAppointmentActivity.class);
					EditAppointmentActivity.putExtra("AppointmentId", appointmentList.get(recordNo-1).getId());
					startActivity(EditAppointmentActivity);
					
			break;
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