package uk.ac.westminster.ecwm511.cw2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.example.coursework2.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TranslateActivity extends Activity implements OnClickListener{
	
	private Database db;
	private Toast toast;
	private Spinner fromComboBox;
	private Spinner toComboBox;
	private TextView originalText;
	private TextView translatedText;
	private TextView appointmentTitle;
	private String fromLanguage;
	private String toLanguage;
	private String accessToken;
	private int appointmentId;

	/**
	 * Constructs and populates the user interface
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_activity);
		
		fromComboBox = (Spinner) findViewById(R.id.from_language);
		toComboBox = (Spinner) findViewById(R.id.to_language);
		originalText = (TextView) findViewById(R.id.original_text);
		translatedText = (TextView) findViewById(R.id.translated_text);
		appointmentTitle = (TextView) findViewById(R.id.translate_appointment_title);
		
		View transButton = (Button) findViewById(R.id.translate_button);
		transButton.setOnClickListener(this);
		View saveButton = findViewById(R.id.translate_save_button);
		saveButton.setOnClickListener(this);
		
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
		
		fromComboBox.setAdapter(adapter);
		toComboBox.setAdapter(adapter);
		
		// Automatically select two spinner items
		fromComboBox.setSelection(0);
		toComboBox.setSelection(0);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			appointmentId = extras.getInt("appointmentId");
			originalText.setText(extras.getString("appointmentDetails"));
			appointmentTitle.setText(extras.getString("appointmentTitle"));
		}
		
		// get the access token from Microsoft
		new GetAccessTokenTask().execute();
	}


	/**
	 * Extract the language code from the current spinner item 
	 * @return result
	 * */
	private String getLang(Spinner spinner) {
		String result = spinner.getSelectedItem().toString();
		int leftPart = result.indexOf("(");
		int rightPart = result.indexOf(")");
		result = result.substring(leftPart + 1, rightPart);
		return result;
	}

	private void doTranslate2(String original, String from, String to) {
		if (accessToken != null)
			new TranslationTask().execute(original, from, to);
	}
	
	private class TranslationTask extends AsyncTask<String, Void, String> {
		protected void onPostExecute(String translation) {
			translatedText.setText(translation);
		}

		protected String doInBackground(String... s) {
			HttpURLConnection con2 = null;
			String result = getResources().getString(R.string.translation_error);
			String original = s[0];
			String from = getLang(fromComboBox);
			String to = getLang(toComboBox);
			
			try {
				// Read results from the query
				BufferedReader reader;
				@SuppressWarnings("deprecation")
				String uri = "http://api.microsofttranslator.com"
						+ "/v2/Http.svc/Translate?text="
						+ URLEncoder.encode(original) + "&from=" + from
						+ "&to=" + to;
				URL url_translate = new URL(uri);
				String authToken = "bearer" + " " + accessToken;
				con2 = (HttpURLConnection) url_translate.openConnection();
				con2.setRequestProperty("Authorization", authToken);
				con2.setDoInput(true);
				con2.setReadTimeout(10000 /* milliseconds */);
				con2.setConnectTimeout(15000 /* milliseconds */);
				reader = new BufferedReader(new InputStreamReader(
						con2.getInputStream(), "UTF-8"));
				String translated_xml = reader.readLine();
				reader.close();
				
				// parse the XML returned
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(
						translated_xml)));
				NodeList node_list = doc.getElementsByTagName("string");
				NodeList l = node_list.item(0).getChildNodes();
				Node node;
				String translated = null;
				if (l != null && l.getLength() > 0) {
					node = l.item(0);
					translated = node.getNodeValue();
				}
				if (translated != null)
					result = translated;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con2 != null) {
					con2.disconnect();
				}
			}
			return result;
		}
	}

	private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

		protected void onPostExecute(String access_token) {
			accessToken = access_token;
		}

		protected String doInBackground(Void... v) {
			String result = null;
			HttpURLConnection httpConnection = null;
			String clientID = "W1521743";
			String clientSecret = "4JefTKAAERFyAOTb9SoDiQq4p4LixY3oEn8QUeFmXto=";
			String strTranslatorAccessURI = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
			@SuppressWarnings("deprecation")
			String strRequestDetails = "grant_type="
					+ "client_credentials&client_id="
					+ URLEncoder.encode(clientID) + "&client_secret="
					+ URLEncoder.encode(clientSecret)
					+ "&scope=http://api.microsofttranslator.com";
			try {
				URL url = new URL(strTranslatorAccessURI);
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setReadTimeout(10000 /* milliseconds */);
				httpConnection.setConnectTimeout(15000 /* milliseconds */);
				httpConnection.setRequestMethod("POST");
				httpConnection.setDoInput(true);
				httpConnection.setDoOutput(true);
				httpConnection.setChunkedStreamingMode(0);
				
				// Start the query
				httpConnection.connect();
				OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
				out.write(strRequestDetails.getBytes());
				out.flush();
				
				// Read results from the query
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
				String payload = reader.readLine();
				reader.close();
				out.close();

				// Parse to get the access token
				JSONObject jsonObject = new JSONObject(payload);
				result = jsonObject.getString("access_token");
			} catch (IOException e) {
				e.getStackTrace();
			} catch (JSONException e) {
				e.getStackTrace();
			} finally {
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
			}
			return result;
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
		
		case R.id.translate_button:
			
			translatedText.setText("Translating...");
			
			if (accessToken != null)
				doTranslate2(originalText.getText().toString().trim(), fromLanguage, toLanguage);
			break;
		
		case R.id.translate_save_button:
			
			String translatedDetails = translatedText.getText().toString();
			
			int updateStatus = db.updateAppointmentDetails(appointmentId, translatedDetails);
			
			if(updateStatus==1){
				this.displayMessage("Appointment Updated");
			}else{
				this.displayMessage("Cannot update, please retry");
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