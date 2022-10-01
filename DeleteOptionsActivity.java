package uk.ac.westminster.ecwm511.cw2;

import com.example.coursework2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class DeleteOptionsActivity extends Activity implements OnClickListener{
	
	private Toast toast;
	private String selectedDate;
	private Database db;
	
	/**
	 * Constructs the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_options_activity);
		View deleteAllButton = findViewById(R.id.delete_all_button);
		deleteAllButton.setOnClickListener(this);
		View deleteSelectButton = findViewById(R.id.delete_select_button);
		deleteSelectButton.setOnClickListener(this);
		Bundle calendarDate = getIntent().getExtras();
		
		if(calendarDate!=null){
			selectedDate = calendarDate.getString("SelectedDate");
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
			case R.id.delete_all_button:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Delete All, Are You Sure?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			break;
				
			case R.id.delete_select_button:
				finish();
				Intent DeletionListActivity = new Intent(this, DeletionListActivity.class);
				DeletionListActivity.putExtra("SelectedDate", selectedDate);
				startActivity(DeletionListActivity);
			break;
		}
		
	}
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {

			case DialogInterface.BUTTON_POSITIVE:
				db.deleteAll(selectedDate);
				finish();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				finish();
				break;
			}
		}
	};
	
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