package com.bemyapp.telethonmobile;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Comparator;

import com.bemyapp.telethonmobile.ListpoiActivity.listPOIAdapter;
import com.bemyapp.telethonmobile.ListpoiActivity.listPoiSorter;
import com.bemyapp.telethonmobile.constants.Category;
import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.GetListPois;
import com.bemyapp.telethonmobile.tools.MapItemizedOverlay;
import com.bemyapp.telethonmobile.tools.MyOverlayItem;
import com.bemyapp.telethonmobile.tools.Poi;
import com.bemyapp.telethonmobile.view.ActionBar;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;


public class ListpoiActivity extends Activity {

	public ListView listPOI;
	public int catSelected;
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent kEvent) {
		if (keycode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keycode, kEvent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_poi);

		
		this.catSelected = this.getIntent().getExtras().getInt("category", 0);
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
		actionBar.setTitle(Category.getCategory(catSelected)+"");
		actionBar.setActionDrawable(getResources().getDrawable(R.drawable.map));
		actionBar.showActionButton(true);
		
		actionBar.getActionButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ListpoiActivity.this, MapViewActivity.class);
				i.putExtra("category", catSelected);
				startActivity(i);
				finish();
			}
		});
		this.listPOI = (ListView) this.findViewById(R.id.listView1);
		
		final listPOIAdapter listAdapter = new listPOIAdapter();
		listPOI.setAdapter(listAdapter);

		listPOI.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ListpoiActivity.this,
						CommentsListActivity.class);
				Poi item = listAdapter.listPoi[arg2];
				
				intent.putExtra("title", item.getName());
				intent.putExtra("latitude", item.getLatitude());
				intent.putExtra("longitude", item.getLongitude());
				intent.putExtra("id", item.getId());
				intent.putExtra("note", item.getNote());
				ListpoiActivity.this.startActivity(intent);
			}
			
		});

		
		
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	if (listAdapter.userLocation == null){
		    		listAdapter.userLocation = location;
		    	}else {
		    		listAdapter.setLocation(location);
		    	}
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		listAdapter.setFirstLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
	}
	
	public class listPoiSorter implements Comparator<Poi>{

		private Location userLocation;
		
		public listPoiSorter(Location userLocation){
			this.userLocation = userLocation;
		}
		
		@Override
		public int compare(Poi poi1, Poi poi2) {

			float[] distanceResult = new float[3];
			Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
					poi1.getLatitude(), poi1.getLongitude(),
					distanceResult);
			
			int distancePoi1 = (int) distanceResult[0];
			
			Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
					poi2.getLatitude(), poi2.getLongitude(),
					distanceResult);
			
			int distancePoi2 = (int) distanceResult[0];

			return distancePoi1 - distancePoi2;
		}
		
	}
	
	public class listPOIAdapter extends BaseAdapter {

		public Poi[] listPoi = new Poi[]{};
		public Location userLocation;
		public Location firstUserLocation;
		
		public void setFirstLocation(Location location){
			this.firstUserLocation = location;
			this.setLocation(location);
			this.userLocation = null;
		}
		
		public void setLocation(Location location){
			this.userLocation = location;
			
			
			
			GetListPois gpois = new GetListPois(location.getLatitude(), location.getLongitude(), 2, catSelected){

				@Override

				protected void onPostExecute(Void result) {
					ArrayList<Poi> listpois = getListPois();
					listPoi = listpois.toArray(new Poi[]{});
					
					Location l = userLocation;
					if (l == null) l = firstUserLocation;
					
					listPoiSorter poiComparator = new listPoiSorter(l);
					Arrays.sort(listPoi, poiComparator);
					
					listPOIAdapter.this.notifyDataSetChanged();
				}

			};

			gpois.execute();
		}
		
		@Override
		public int getCount() {
			return listPoi.length;
		}

		@Override
		public Object getItem(int position) {
			return listPoi[position];
		}

		@Override
		public long getItemId(int position) {
			return listPoi[position].getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.popup_details_poi, null);
			}
			
			Poi item = this.listPoi[position];
			
			TextView nameText = (TextView) convertView.findViewById(R.id.poiNameText);
			nameText.setText(item.getName());
			
			TextView distanceText = (TextView) convertView.findViewById(R.id.distanceTextView);
			distanceText.setVisibility(LinearLayout.VISIBLE);

			if (userLocation != null) {
				float[] distanceResult = new float[3];
				Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
						item.getLatitude(), item.getLongitude(),
						distanceResult);
				
				distanceText.setText((int)distanceResult[0]+" m");
			} else if (firstUserLocation != null)  {
				float[] distanceResult = new float[3];
				Location.distanceBetween(firstUserLocation.getLatitude(), firstUserLocation.getLongitude(),
						item.getLatitude(), item.getLongitude(),
						distanceResult);
				
				distanceText.setText((int)distanceResult[0]+" m");
			}
			
			
			TextView categoryText = (TextView) convertView.findViewById(R.id.poiCategoryText);
			String[] categ = getApplicationContext().getResources().getStringArray(R.array.categories);
			categoryText.setText(categ[item.getCategory()-1]);
			RatingBar voteRating = (RatingBar)convertView.findViewById(R.id.poiRatingBar);
			voteRating.setRating(item.getNote().floatValue());
			
			return convertView;
		}
		
	}
	
}
