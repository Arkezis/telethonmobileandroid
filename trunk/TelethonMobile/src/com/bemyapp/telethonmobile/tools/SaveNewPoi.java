package com.bemyapp.telethonmobile.tools;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.bemyapp.telethonmobile.constants.Constants;

public class SaveNewPoi extends AsyncTask<Void, Void, Void> {

	private final String name;
	private final int category;
	private final double latitude;
	private final double longitude;

	public SaveNewPoi(String _name, int _category, double _latitude,
			double _longitude) {
		this.name = _name;
		this.category = _category;
		this.latitude = _latitude;
		this.longitude = _longitude;

	}

	@Override
	protected Void doInBackground(Void... params) {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			String url = "http://cilheo.fr/telethonMobile.php?action=insert&nom="
					+ URLEncoder.encode(name, "UTF-8")
					+ "&cat="
					+ category
					+ "&lat=" + latitude + "&long=" + longitude;
			HttpGet httpget = new HttpGet(url);
			HttpResponse response;
			response = httpclient.execute(httpget);
			Log.d(Constants.LOG, "status = "
					+ response.getStatusLine().getStatusCode());
		} catch (IllegalArgumentException e) {
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}
}
