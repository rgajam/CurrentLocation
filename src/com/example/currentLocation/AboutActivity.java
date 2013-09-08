package com.example.currentLocation;

import com.example.fragraments_1.R;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;

public class AboutActivity extends Activity {

	ActionBar actionBar;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			// Show the Up button in the action bar.
//			actionBar = getActionBar();
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			actionBar.hide();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.form, menu);
		return true;
	}

}
