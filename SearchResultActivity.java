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

public class SearchResultActivity extends Activity implements OnClickListener {

	private Toast toast;
	private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	private Database db;
	private TextView searchResult;
	private EditText appointmentNumberTextField;
	private int recordNo;
	private String searchWord;
	private String currentDate;

	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results_activity);

		searchResult = (TextView) findViewById(R.id.search_result_textview);
		appointmentNumberTextField = (EditText) findViewById(R.id.search_id_text);
		View selectButton = findViewById(R.id.search_view_appointment_details_button);
		selectButton.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			searchWord = extras.getString("searchWord");
			currentDate = extras.getString("currentDate");
		}

		db = new Database(this);
		appointmentList = db.findAppointment(searchWord,currentDate);

		if (appointmentList.size() == 0) {
			searchResult.setText("No Appointments Found!");
		} else {

			for (int i = 0; i < appointmentList.size(); i++) {
				int idNo = i + 1; // number for the list e.g. 1. 2. 3.
				searchResult.setText(searchResult.getText() + "\n" + idNo + "." + "\t"
						+ appointmentList.get(i).getDate() + "\t"
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
		case R.id.search_view_appointment_details_button:
			recordNo = Integer.parseInt(appointmentNumberTextField.getText().toString());
			
			//checks if digit entered matches the displayed
			if(recordNo<1 || recordNo>appointmentList.size()){
			
				displayMessage("Enter valid number");
			
			}else{
				//start activity
				Intent ViewAppointmentDetailsActivity = new Intent(this, ViewAppointmentDetailsActivity.class);
				ViewAppointmentDetailsActivity.putExtra("AppointmentId", appointmentList.get(recordNo-1).getId());
				startActivity(ViewAppointmentDetailsActivity);	
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
