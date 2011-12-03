package com.bemyapp.telethonmobile;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class MapViewActivity extends MapActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}