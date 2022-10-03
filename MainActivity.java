import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Toast toast;
	private CalendarView calendar;
	private String selectedDate;

	/**
	 * Constructs the user interface
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		View createButton = findViewById(R.id.create_button);
		createButton.setOnClickListener(this);
		View viewEditButton = findViewById(R.id.view_edit_button);
		viewEditButton.setOnClickListener(this);
		View deleteButton = findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(this);
		View moveButton = findViewById(R.id.move_button);
		moveButton.setOnClickListener(this);
		View searchButton = findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		View translateButton = findViewById(R.id.translate_button);
		translateButton.setOnClickListener(this);
		View selectLanguageButton = findViewById(R.id.select_language_button);
		selectLanguageButton.setOnClickListener(this);

		calendar = (CalendarView) findViewById(R.id.calendar);
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
				month++;//increments month by 1 because it starts from 0;
				selectedDate = day + "/" + month + "/" + year;
			}
		});

	}

	/**
	 * OnClickListener catches which button is pressed and takes 
	 * action accordingly.
	 * @param button
	 * */
	@Override
	public void onClick(View button) {

		switch (button.getId()) {

		case R.id.create_button:
			if (isDateSelected()) {
				Intent createAppointmentActivity = new Intent(this, CreateAppointmentActivity.class);
				createAppointmentActivity.putExtra("SelectedDate", selectedDate);
				startActivity(createAppointmentActivity);
			} else {
				this.displayMessage("Please select a date");
			}
			break;

		case R.id.view_edit_button:
			if (isDateSelected()) {
				Intent ViewAppointmentsActivity = new Intent(this, ViewAppointmentsActivity.class);
				ViewAppointmentsActivity.putExtra("SelectedDate", selectedDate);
				startActivity(ViewAppointmentsActivity);
			} else {
				this.displayMessage("Please select a date");
			}
			break;

		case R.id.delete_button:
			if (isDateSelected()) {
				Intent DeleteOptionsActivity = new Intent(this, DeleteOptionsActivity.class);
				DeleteOptionsActivity.putExtra("SelectedDate", selectedDate);
				startActivity(DeleteOptionsActivity);
			} else {
				this.displayMessage("Please select a date");
			}
			break;

		case R.id.move_button:
			if (isDateSelected()) {
				Intent MoveAppointmensListActivity = new Intent(this, MoveAppointmensListActivity.class);
				MoveAppointmensListActivity.putExtra("SelectedDate",selectedDate);
				startActivity(MoveAppointmensListActivity);
			} else {
				this.displayMessage("Please select a date");
			}
			break;

		case R.id.search_button:
			Intent SearchActivity = new Intent(this, SearchActivity.class);
			startActivity(SearchActivity);
			break;

		case R.id.translate_button:
			if (isDateSelected()) {
				Intent TranslateSelectAppointmentActivity = new Intent(this, TranslateSelectAppointmentActivity.class);
				TranslateSelectAppointmentActivity.putExtra("SelectedDate", selectedDate);
				startActivity(TranslateSelectAppointmentActivity);
			} else {
				this.displayMessage("Please select a date");
			}
			break;

		case R.id.select_language_button:
			Intent LanguageTaskSelectionActivity = new Intent(this, LanguageTaskSelectionActivity.class);
			startActivity(LanguageTaskSelectionActivity);
			break;
		}
	}

	/**
	 * Checks if the user selected a date from the
	 * calendar. Called when user chooses to perform
	 * an operation that requires a date from the calendar.
	 * @return boolean
	 * */
	private boolean isDateSelected() {
		if (selectedDate == null) {
			return false;
		}

		return true;
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

	public void onDestroy() {
		super.onDestroy();
		finish();
	}

}