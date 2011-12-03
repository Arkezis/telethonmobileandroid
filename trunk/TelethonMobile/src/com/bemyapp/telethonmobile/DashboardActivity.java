package com.bemyapp.telethonmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DashboardActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		startActivity(new Intent(this, MapViewActivity.class));
	}

}