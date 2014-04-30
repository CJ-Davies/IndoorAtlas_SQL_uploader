package com.indooratlas.example;

import com.indooratlas.android.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import android.provider.Settings.Secure; // to get (hopefully) unique ID of each device
import org.apache.http.*;				 // to get NameValuePair
import org.apache.http.message.*;		 // to get BasicNameValuePair
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.*;

/*
 * IndoorAtlas API example
 */
public class IndoorAtlasExample extends Activity implements IndoorAtlasListener {

	private static final String TAG = "IndoorAtlasExample";

	private TextView textView;
	private Handler handler = new Handler();
	private IndoorAtlas indoorAtlas;

	private String deviceId;

	JSONParser jsonParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";

	private final String apiKey = "<api key>";
	private final String secretKey = "<secret key>";
	
	/*
	 * Corresponding names of values from the Web UI;
	 * 
	 * buildingId = Building
	 * levelID = Floor
	 * floorPlanId = Graphics
	 */

	private final String buildingId = "<building ID>";
	private final String levelId = "<level ID>";
	private final String floorPlanId = "<floor plan ID>";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView1);
		
		try {
			// Get handle to the IndoorAtlas API
			// Throws exception when the cloud service cannot be reached
			// Get your Apikey and Secret key from IndoorAtlas My Account
			indoorAtlas = IndoorAtlasFactory.createIndoorAtlas(
					this.getApplicationContext(), 
					this, 
					apiKey,
					secretKey);

			// Get a (hopefully) unique id for the device running the app
			deviceId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

			// Prompts user to perform Figure "8" calibration motion
			showMessageOnUI("Calibrating... Figure \"8\" motion for 10 seconds");

			// Starts the calibration, once completed the onCalibrationFinished() is called
			indoorAtlas.calibrate();

			// Might want to put some sort of progress bar here

		} catch (IndoorAtlasException ex) {
			showMessageOnUI("Failed to connect to IndoorAtlas. Check your credentials.");
			ex.printStackTrace();
		}

	}

	@Override
	public void onStop() {

		Log.d(TAG, "onStop()");

		try {
			showMessageOnUI("onStop(): Stopping positioning.");

			// Stop positioning when not needed
			indoorAtlas.stopPositioning();

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onStop();
	}

	// Called on every new location estimate
	@SuppressLint("SimpleDateFormat")
	public void onServiceUpdate(ServiceState state) {

		long roundtrip;
		double latitude, longitude, x, y, heading, probability;
		int i, j;
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");		// format that MySQL TIMESTAMP supports

		roundtrip = state.getRoundtrip();
		latitude = state.getGeoPoint().getLatitude();
		longitude = state.getGeoPoint().getLongitude();
		x = state.getMetricPoint().getX();
		y = state.getMetricPoint().getY();
		i = state.getImagePoint().getI();
		j = state.getImagePoint().getJ();
		heading = state.getHeadingDegrees();
		probability = state.getProbability();

		Log.d(TAG, "onServiceUpdate()");
		// Use location estimate

		// this string is just used for echoing to the phone display(?)
		String s = new String(
				"Roundtrip : " + roundtrip + "ms\n\n" 
						+ "Lat : "+ latitude + "\n\n" 
						+ "Lon : "+ longitude + "\n\n" 
						+ "X [meter] : "+ x + "\n\n" 
						+ "Y [meter] : "+ y + "\n\n" 
						+ "I [pixel] : "+ i + "\n\n" 
						+ "J [pixel] : "+ j + "\n\n" 
						+ "Heading : "+ heading + "\n\n" 
						+ "Probability : "+ probability + "\n\n"
						+ "Device ID : " + deviceId); // + "\n\n");

		// build HTTP POST request to send these data to the database
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("deviceId", deviceId));
		params.add(new BasicNameValuePair("buildingId", buildingId));
		params.add(new BasicNameValuePair("levelId", levelId));
		params.add(new BasicNameValuePair("floorplanId", floorPlanId));
		params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
		params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
		params.add(new BasicNameValuePair("x", Double.toString(x)));
		params.add(new BasicNameValuePair("y", Double.toString(y)));
		params.add(new BasicNameValuePair("i", Integer.toString(i)));
		params.add(new BasicNameValuePair("j", Integer.toString(j)));
		params.add(new BasicNameValuePair("heading", Double.toString(heading)));
		params.add(new BasicNameValuePair("probability", Double.toString(probability)));
		params.add(new BasicNameValuePair("roundtrip", Long.toString(roundtrip)));
		params.add(new BasicNameValuePair("time", sd.format(d.getTime())));

		JSONObject json = jsonParser.makeHttpRequest("<address of updateposition.php>", "POST", params);

		Log.i("POST: ", json.toString());

		int success;
		try {
			success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				s = s + "\n\ndb success";
			} else {
				s = s + "\n\ndb failure";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		showMessageOnUI(s);

	}

	// Request failed
	public void onServiceFailure(final String reason) {
		Log.d(TAG, "onServiceFailure()");
		showMessageOnUI("onServiceFailure(): reason : " + reason);
	}

	// Initializing location service
	public void onServiceInitializing() {
		Log.d(TAG, "onServiceInitializing()");
		showMessageOnUI("onServiceInitializing()");
	}

	// Initialization completed
	public void onServiceInitialized() {
		Log.d(TAG, "onServiceInitialized()");
		showMessageOnUI("onServiceInitialized(): Walk to get location fix");
	}

	// Location service initialization failed
	public void onInitializationFailed(final String reason) {
		Log.d(TAG, "onInitializationFailed()");
		showMessageOnUI("onInitializationFailed(): "+ reason);
	}

	// Positioning was stopped
	public void onServiceStopped() {
		Log.d(TAG, "onServiceStopped()");
		showMessageOnUI("onServiceStopped(): IndoorAtlas Positioning Service is stopped.");
	}

	// Calibration failed
	public void onCalibrationFailed(String reason) {
		showMessageOnUI("onCalibrationFailed(): Calibration failed.");
	}

	// Calibration successful, positioning can be started
	public void onCalibrationFinished() {
		Log.d(TAG, "onCalibrationFinished()");

		showMessageOnUI("onCalibrationFinished(): starting positioning.");

		// Use Floor Plans tool to get IDs for building, level and floor plan 

		// Setting the last parameter true tries to switch to Mobile network for
		// the positioning session and may disconnect WiFi. 
		// Otherwise WiFi network will be used, if available.

		indoorAtlas.startPositioning(buildingId, levelId,
				floorPlanId, false);
	}

	// Helper method
	private void showMessageOnUI(final String message) {
		handler.post(new Runnable() {
			public void run() {
				textView.setText(message);
			}
		});

	}

}
