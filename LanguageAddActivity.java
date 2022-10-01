package uk.ac.westminster.ecwm511.cw2;

import java.util.ArrayList;

import com.example.coursework2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LanguageAddActivity extends Activity implements OnClickListener{
	
	private ArrayList<String> languages = new ArrayList<String>();
	private Database db;
	private Toast toast;
	private Spinner languageList;
	private TextView list;
	
	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_language_activity);

		list = (TextView) findViewById(R.id.language_list_view);
		languageList = (Spinner) findViewById(R.id.language);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		languageList.setAdapter(adapter);
		
		View addButton = findViewById(R.id.add_language_button);
		addButton.setOnClickListener(this);
		
		db = new Database(this);
		
		languages = db.getLanguages();

		if (languages.size() == 0) {
			list.setText("No Language Found!");
		} else {
			
			for (int i = 0; i < languages.size(); i++) {
				list.setText(list.getText() + "\n" + languages.get(i));
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
		case R.id.add_language_button:
			db.addLanguage((String) languageList.getSelectedItem());
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