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

public class TranslateSelectAppointmentActivity extends Activity implements OnClickListener{
	
	private Database db;
	private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	private EditText appointmentNumberTextField;
	private TextView list;
	private String dateSelected;
	private Toast toast;
	private int recordNo;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_list_appointments);
		View selectButton = findViewById(R.id.translate_select_button);
		selectButton.setOnClickListener(this);
		
		appointmentNumberTextField = (EditText) findViewById(R.id.translate_app_id_text);
		list = (TextView) findViewById(R.id.translate_list_text);
		
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
				int idNo = i+1; //number for the list e.g. 1. 2. 3.
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
		case R.id.translate_select_button:
			recordNo = Integer.parseInt(appointmentNumberTextField.getText().toString());
			//checks if digit entered matches the displayed
			if(recordNo<1 || recordNo>appointmentList.size()){
			
				displayMessage("Enter valid number");
			
			}else{

				Intent TranslateActivity = new Intent(this, TranslateActivity.class);
				TranslateActivity.putExtra("appointmentId", appointmentList.get(recordNo-1).getId());
				TranslateActivity.putExtra("appointmentTitle", appointmentList.get(recordNo-1).getTitle());
				TranslateActivity.putExtra("appointmentDetails", appointmentList.get(recordNo-1).getDetails());
				startActivity(TranslateActivity);
				this.finish();
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
