package com.example.currentLocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.YuvImage;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragraments_1.R;

public class LocationActivity extends Activity {

	LocationListener locationListener;
	Double latitude, longitude, altitude;
	private TextView latTView, longTView, altTView, lastUpdatedTime;
	private ShareActionProvider mShareActionProvider;
	SharedPreferences.Editor editor;
	SimpleDateFormat sdf;
	GregorianCalendar calendar;
	ActionBar actionBar;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		actionBar = getActionBar();
		// actionBar.setIcon(android.R.drawable.ic_menu_mylocation);
		sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
		calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
		// SharedPreferences sharedPref = ((Activity)
		// this.getApplicationContext()).getPreferences(Context.MODE_PRIVATE);
		// editor = sharedPref.edit();

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		latTView = (TextView) findViewById(R.id.latitudeOut);
		longTView = (TextView) findViewById(R.id.longitudeOut);
		altTView = (TextView) findViewById(R.id.altitudeOut);
		lastUpdatedTime = (TextView) findViewById(R.id.lastUpdatedAt);
		
		if(!MyResources.lastUpdatedTimeSubTitle.isEmpty())
			actionBar.setSubtitle(MyResources.lastUpdatedTimeSubTitle);
		

		locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.d("RishiTag", "LocationActivity - onStatusChanged");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.d("RishiTag", "LocationActivity - onProviderEnabled");
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.d("RishiTag", "LocationActivity - onProviderDisabled");

			}

			@Override
			public void onLocationChanged(android.location.Location location) {
				Log.d("RishiTag", "LocationActivity - onLocationChanged");
				MyResources.lastUpdatedTimeLong = Calendar.getInstance()
						.getTimeInMillis();
				// editor.putLong("lastLocationUpdatedTime",
				// lastUpdatedTimeLong);
				locationChanged(location, MyResources.lastUpdatedTimeLong);
				if (MyResources.gpsLocUpdateCount > 0)
					removeLocationListener();
			}
		};
		if (MyResources.gpsLocUpdateCount == 0)
			setLocationListener();

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);

		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.refresh:
			refreshLocation();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshLocation() {
		MyResources.gpsLocUpdateCount = 0;
		setLocationListener();
	}

	@SuppressLint("NewApi")
	private void locationChanged(android.location.Location location,
			long timeStamp) {

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		latTView.setText("Latitude: " + latitude.toString());
		longTView.setText("Longtitude: " + longitude.toString());
		altTView.setText("Altitude: " + altitude.toString());

		calendar.setTimeInMillis(timeStamp);
		String timeStampStr = sdf.format(calendar.getTime()).toString();
		lastUpdatedTime.setText("Last updated at: " + timeStampStr); // Display
																				// last																					// time
		MyResources.gpsLocUpdateCount++;

		actionBar.setTitle("Current Location");
		MyResources.lastUpdatedTimeSubTitle = "Last upated at: " + timeStampStr;
		actionBar.setSubtitle(MyResources.lastUpdatedTimeSubTitle);
		

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		StringBuilder sb = new StringBuilder();
		sb.append("I'm at location: ");
		sb.append("Latitude: ");
		sb.append(latitude.toString());
		sb.append(", Longtitude: ");
		sb.append(longitude.toString());
		sb.append(", Altitude: ");
		sb.append(altitude.toString());
		sb.append(", Last updated time: ");
		sb.append(timeStampStr);		 
		sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
		sendIntent.setType("text/plain");
		setShareIntent(sendIntent);
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	private void setLocationListener() {
		HomeActivity.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 100, 0, locationListener);
	}

	private void removeLocationListener() {
		HomeActivity.locationManager.removeUpdates(locationListener);
	}

}
