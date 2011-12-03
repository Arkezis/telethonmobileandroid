package com.bemyapp.telethonmobile;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.JSONTools;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapViewActivity extends MapActivity {

	private double latitude = -1;
	private double longitude = -1;
	private LocationManager lm = null;
	private GeoPoint p = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Button addPoi = (Button) findViewById(R.id.addPOI);
		addPoi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MapViewActivity.this);
				// builder.setView(arg0);
				builder.show();
			}
		});

		/**
		 * Recuperation de la derniere position connue (gain de temps)
		 */
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location last = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (last != null) {
			latitude = last.getLatitude();
			longitude = last.getLongitude();

		} else {
			last = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (last != null) {
				latitude = last.getLatitude();
				longitude = last.getLongitude();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0,
				networkLocationListener);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0,
				gpsLocationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(networkLocationListener);
		lm.removeUpdates(gpsLocationListener);
	}

	private LocationListener networkLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e(Constants.LOG, "onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.e(Constants.LOG, "onProviderEnabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.e(Constants.LOG, "onProviderDisabled");
		}

		@Override
		public void onLocationChanged(Location location) {
			latitude = location.getLatitude();
			Log.e(Constants.LOG, "latitude = " + latitude);
			longitude = location.getLongitude();
			Log.e(Constants.LOG, "longitude = " + longitude);
			updateMapPosition();
			getMarkers();
		}
	};

	private LocationListener gpsLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e(Constants.LOG, "onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.e(Constants.LOG, "onProviderEnabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.e(Constants.LOG, "onProviderDisabled");
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.e(Constants.LOG, "onLocationChagned");
			latitude = location.getLatitude();
			Log.e(Constants.LOG, "latitude = " + latitude);
			longitude = location.getLongitude();
			Log.e(Constants.LOG, "longitude = " + longitude);

			updateMapPosition();
			getMarkers();
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void updateMapPosition() {
		MapView mv = (MapView) findViewById(R.id.mapview);
		MapController mc = mv.getController();

		p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
		mc.animateTo(p);
		mc.setZoom(17);
		mv.invalidate();

		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mv.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);

	}

	class MapOverlay extends com.google.android.maps.Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					android.R.drawable.presence_online);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}
	}

	private void getMarkers() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				System.err
						.println(JSONTools
								.getJSONfromURL("http://cilheo.fr/telethonMobile.php?action=list&lat=48.8531475&long=2.3721549&range=5"));

				return null;
			}

		}.execute();

	}

}