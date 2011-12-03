package com.bemyapp.telethonmobile.tools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetListPois extends AsyncTask<Void, Void, Void> {

	private ArrayList<Poi> pois = null;
	private final double latitude;
	private final double longitude;
	private final int range;
	private final int category;

	public GetListPois(double _latitude, double _longitude, int _range,
			int _category) {
		this.latitude = _latitude;
		this.longitude = _longitude;
		this.range = _range;
		this.category = _category;
	}

	@Override
	protected Void doInBackground(Void... params) {
		pois = new ArrayList<Poi>();
		String url = "http://cilheo.fr/telethonMobile.php?action=list&lat="
				+ latitude + "&long=" + longitude + "&range=" + range;
		if (category > 0) {
			url += "&category=" + category;
		}

		JSONArray array = JSONTools.getJSONArrayfromURL(url);
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject object = array.getJSONObject(i);
				Poi poi = new Poi();
				poi.setName(object.getString("nom"));
				poi.setDistance(object.getLong("distance"));
				poi.setCategory(object.getInt("cat"));
				poi.setLongitude(object.getDouble("long"));
				poi.setLatitude(object.getDouble("lat"));
				pois.add(poi);

			} catch (JSONException e) {
			}
		}
		return null;
	}

}
