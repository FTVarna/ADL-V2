package uk.ac.westminster.ecwm511.cw2;

import java.util.ArrayList;

import com.example.coursework2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class LanguageDeleteActivity extends Activity implements OnClickListener {

	private Toast toast;
	private Spinner languageSpinner;
	private Database db;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language_delete_activity);
		View deleteButton = findViewById(R.id.delete_language_button);
		deleteButton.setOnClickListener(this);
		languageSpinner = (Spinner) findViewById(R.id.delete_language_list);
		
		db = new Database(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ArrayList<String> langaugeList = db.getLanguages();
		
		if(langaugeList.size()==0){
			langaugeList.add("No Language Found");
		}
		
		for(int i=0; i<langaugeList.size(); i++){
			adapter.add(langaugeList.get(i));
		}
		
		languageSpinner.setAdapter(adapter);
	}

	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.delete_language_button:
			db.deleteLanguage((String) languageSpinner.getSelectedItem());
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