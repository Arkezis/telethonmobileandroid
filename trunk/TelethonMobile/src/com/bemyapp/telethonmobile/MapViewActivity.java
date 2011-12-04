package com.bemyapp.telethonmobile;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bemyapp.telethonmobile.constants.Category;
import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.GetListPois;
import com.bemyapp.telethonmobile.tools.MapItemizedOverlay;
import com.bemyapp.telethonmobile.tools.MyOverlayItem;
import com.bemyapp.telethonmobile.tools.Poi;
import com.bemyapp.telethonmobile.tools.SaveNewPoi;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapViewActivity extends MapActivity {

	private Button addPoi;
	private MyLocationOverlay myLocationOverlay;
	MapView mv;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		final String errorMessage = getString(R.string.errorLabelEmpty);
		addPoi = (Button) findViewById(R.id.addPOI);
		addPoi.setVisibility(View.INVISIBLE);
		
		this.mv = (MapView) findViewById(R.id.mapview);
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

		this.myLocationOverlay = new MyLocationOverlay(this, mv);
		this.myLocationOverlay.enableMyLocation();
		
		this.myLocationOverlay.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mv.getController().animateTo(myLocationOverlay.getMyLocation());
						mv.getController().setZoom(17);
						addPoi.setVisibility(View.VISIBLE);
					}
				});
			}
		});
		
		List<Overlay> listOfOverlays = mv.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(this.myLocationOverlay);
		
		
		this.getMarkers();
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

		final int catSelected = this.getIntent().getExtras().getInt("category", 0);

		GetListPois gpois = new GetListPois(mv.getMapCenter().getLatitudeE6()/1E6, mv.getMapCenter().getLongitudeE6()/1E6, 2, catSelected){

			@Override

			protected void onPostExecute(Void result) {

				ArrayList<Poi> listpois = getListPois();

				Log.d(Constants.LOG, "size = "+listpois.size());

				Log.d(Constants.LOG, "Pouet"+catSelected);

				while (mv.getOverlays().size()>1){
					mv.getOverlays().remove(1);
				}
				
				if (catSelected == 0) {

					MapItemizedOverlay[] listMap = new MapItemizedOverlay[]{
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(1).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(2).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(3).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(4).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(5).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(6).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(7).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(8).poiDrawable), MapViewActivity.this),
							new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(9).poiDrawable), MapViewActivity.this)	
					};

					for(int i=0;i<9;i++){
						mv.getOverlays().add(listMap[i]);
					}
					
					for (Poi poi : listpois){

						int latitude = (int) (poi.getLatitude()*1E6);
						int longitude = (int) (poi.getLongitude()*1E6);

						MyOverlayItem overlayItem = new MyOverlayItem(new GeoPoint(latitude,longitude),poi);
						listMap[poi.getCategory()-1].addOverlay(overlayItem);

					}
				}else {

					MapItemizedOverlay mapitemizedOverlay = new MapItemizedOverlay(getResources().getDrawable(Category.getCategory(catSelected-1).poiDrawable), MapViewActivity.this);
					mv.getOverlays().add(mapitemizedOverlay);
					for (int i = 0; i < listpois.size(); i++) {

						Log.d(Constants.LOG, "Add de 1 item");

						int latitude = (int) (listpois.get(i).getLatitude()*1E6);

						int longitude = (int) (listpois.get(i).getLongitude()*1E6);

						MyOverlayItem overlayItem = new MyOverlayItem(new GeoPoint(latitude,longitude),listpois.get(i));
						mapitemizedOverlay.addOverlay(overlayItem);

					}
					mv.invalidate();



				}

			}

		};

		gpois.execute();
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