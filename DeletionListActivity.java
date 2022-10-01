package uk.ac.westminster.ecwm511.cw2;

import java.util.ArrayList;
import com.example.coursework2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeletionListActivity extends Activity implements OnClickListener {

	private Toast toast;
	private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	private String dateSelected;
	private TextView list;
	private EditText appointmentNumberTextField;
	private Database db;
	private int recordNo;

	/**
	 * Constructs the user interface
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_appointmentlist_activity);
		list = (TextView) findViewById(R.id.list_text);
		appointmentNumberTextField = (EditText) findViewById(R.id.appointment_id_text);
		View deleteButton = findViewById(R.id.delete_single_button);
		deleteButton.setOnClickListener(this);
		
		//gets the passed information from the previous activity
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

		// yes/no pop up message
		switch (button.getId()) {

		case R.id.delete_single_button:
			recordNo = Integer.parseInt(appointmentNumberTextField.getText().toString());
			
			//validate entered digit
			if(recordNo<1 || recordNo>appointmentList.size()){
			
				displayMessage("Enter valid number");
			
			}else{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are You Sure?")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
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


	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {

			case DialogInterface.BUTTON_POSITIVE:
				db.deleteAppointment(appointmentList.get(recordNo-1).getId());
				finish();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				finish();
				break;
			}
		}
	};
}