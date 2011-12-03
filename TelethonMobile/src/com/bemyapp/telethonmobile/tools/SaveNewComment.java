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

public class SaveNewComment extends AsyncTask<Void, Void, Void> {

	private final int id;
	private final long note;
	private final String comm;

	public SaveNewComment(int _id, long _note, String _comm) {
		this.id = _id;
		this.note = _note;
		this.comm = _comm;

	}

	@Override
	protected Void doInBackground(Void... params) {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			String url = "http://cilheo.fr/telethonMobile.php?action=insertNote&id="
					+ id
					+ "&note="
					+ note
					+ "&com="
					+ URLEncoder.encode(comm, "UTF-8");
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
