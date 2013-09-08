package com.example.currentLocation;

import com.example.fragraments_1.R;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends BaseActivity {

	static LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public void buttonClick(View view) {
		Intent intent = new Intent(this, LocationActivity.class);
		boolean isGPSOn = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isGPSOn)
			startActivity(intent);
		else
			startActivity(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

}
