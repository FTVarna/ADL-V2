import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LanguageTaskSelectionActivity extends Activity implements OnClickListener{

	/**
	 * Constructs and populates the user interface
	 * */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.languages_task_selection_activity);
		View addLanguageButton = findViewById(R.id.add_languages_button);
		addLanguageButton.setOnClickListener(this);
		View deleteLanguageButton = findViewById(R.id.delete_languages_button);
		deleteLanguageButton.setOnClickListener(this);
	}
	
	/**
	 * Catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {
		switch(button.getId()){
		case R.id.add_languages_button:
			Intent LanguageAddActivity = new Intent(this, LanguageAddActivity.class);
			startActivity(LanguageAddActivity);
			break;
			
		case R.id.delete_languages_button:
			Intent LanguageDeleteActivity = new Intent(this, LanguageDeleteActivity.class);
			startActivity(LanguageDeleteActivity);
			break;
		}
	}
}