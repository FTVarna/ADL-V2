package uk.ac.westminster.ecwm511.cw2;

import java.util.Calendar;

import com.example.coursework2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener{
	private Calendar calendar;
	private Toast toast;
	private EditText searchEditText;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		View searchButton = findViewById(R.id.search_appointment_button);
		searchButton.setOnClickListener(this);
		searchEditText = (EditText) findViewById(R.id.search_text);
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button1
	 * */
	@Override
	public void onClick(View button) {
		
		switch(button.getId()){
		
		case R.id.search_appointment_button:
			String search = searchEditText.getText().toString();
			
			if(search.equals("")){
				
				this.displayMessage("Please enter a string to search");
			
			}else{
			
				this.finish();
				calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);
				
				month++;//month starts from 0, so it needs to be incremented by 1
				
				String currentDate = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
				Log.d("Current Date", currentDate);
				
				Intent SearchResultActivity = new Intent(this, SearchResultActivity.class);
				SearchResultActivity.putExtra("searchWord", search);
				SearchResultActivity.putExtra("currentDate", currentDate);
				startActivity(SearchResultActivity);
			
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
