package com.bemyapp.telethonmobile;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.SaveNewPoi;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapViewActivity extends MapActivity {

	private Button addPoi;
	private MyLocationOverlay myLocationOverlay;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		final String errorMessage = getString(R.string.errorLabelEmpty);
		addPoi = (Button) findViewById(R.id.addPOI);
		addPoi.setVisibility(View.INVISIBLE);
		
		MapView mv = (MapView) findViewById(R.id.mapview);
		mv.setBuiltInZoomControls(true);
		
		addPoi.setOnClickListener(new OnClickListener() {


			
			@Override
			public void onClick(View v) {

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						MapViewActivity.this);
				LayoutInflater inflater = getLayoutInflater();
				final View body = inflater
						.inflate(R.layout.popup_new_poi, null);
				alert.setView(body);
				alert.setPositiveButton("Créer",

						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText et = (EditText) body
										.findViewById(R.id.poiName);
								final String name = et.getText().toString();
								if (name == null || name.equals("")) {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											MapViewActivity.this);
									builder.setMessage(errorMessage);
									builder.show();
								}
								Spinner sp = (Spinner) body
										.findViewById(R.id.category);

								final int categoryId = sp
										.getSelectedItemPosition() + 1;

								new SaveNewPoi(name, categoryId, myLocationOverlay.getMyLocation().getLatitudeE6()/1E6,
										myLocationOverlay.getMyLocation().getLongitudeE6()/1E6).execute();
							}

						});
				alert.setNegativeButton("Annuler",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alert.show();
			}
		});

		this.myLocationOverlay = new MyLocationOverlay(getApplicationContext(), mv);
		this.myLocationOverlay.enableMyLocation();
		
		this.myLocationOverlay.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						addPoi.setVisibility(View.VISIBLE);
					}
				});
			}
		});
		
		List<Overlay> listOfOverlays = mv.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(this.myLocationOverlay);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void getMarkers() {
	}

	public boolean onKeyDown(int keycode, KeyEvent kEvent){
		if(keycode == KeyEvent.KEYCODE_BACK){
			Intent i = new Intent(MapViewActivity.this,DashboardActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onKeyDown(keycode, kEvent);
	}
	
}