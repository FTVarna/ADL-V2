package uk.ac.westminster.ecwm511.cw2;

import java.util.ArrayList;

import com.example.coursework2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoveAppointmensListActivity extends Activity implements OnClickListener {

	private Toast toast;
	private String dateSelected;
	private TextView list;
	private EditText appointmentNumberTextField;
	private int recordNo;
	private Database db;
	private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();

	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_appointmens_list_activity);
		View selectButton = findViewById(R.id.move_select_button);
		selectButton.setOnClickListener(this);
		list = (TextView) findViewById(R.id.move_list_text);
		appointmentNumberTextField = (EditText) findViewById(R.id.move_app_id_text);
		
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
				int idNo = i + 1; // number for the list e.g. 1. 2. 3.
				list.setText(list.getText() + "\n" + idNo + "." + "\t"
						+ appointmentList.get(i).getTime() + "\t"
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
		
		case R.id.move_select_button:
			
			recordNo = Integer.parseInt(appointmentNumberTextField.getText().toString());
			recordNo--; //because array starts from 0, need to decrement by 1 to match the record index in array
			
			Log.d("APPOINTMENT ID", Integer.toString(appointmentList.get(recordNo).getId()));
			
			Intent SelectNewDateActivity = new Intent(this, SelectNewDateActivity.class);
			SelectNewDateActivity.putExtra("appointmentId", Integer.toString(appointmentList.get(recordNo).getId()));
			startActivity(SelectNewDateActivity);
			finish();
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