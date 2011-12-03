package com.bemyapp.telethonmobile.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bemyapp.telethonmobile.constants.Constants;

public class JSONTools {

	public static JSONObject getJSONfromURL(String url) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// convert response to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 1024);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.d(Constants.LOG, "getJSONfromURL  result " + result);
			// try parse the string to a JSON object
			jArray = new JSONObject(result);
		} catch (Exception e) {
			Log.d(Constants.LOG, "Error parsing data " + e.toString());
			jArray = null;
		}

		return jArray;
	}

	public static JSONArray getJSONArrayfromURL(String url) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// convert response to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 1024);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.d(Constants.LOG, "getJSONfromURL  result " + result);
			// try parse the string to a JSON object
			jArray = new JSONArray(result);
		} catch (Exception e) {
			Log.d(Constants.LOG, "Error parsing data " + e.toString());
			jArray = null;
		}

		return jArray;
	}

	public static JSONArray getJSONArrayFromInputStream(InputStream is) {
		String result = "";
		JSONArray jArray = null;
		try {
			// convert response to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 1024);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

			Log.d(Constants.LOG, "getJSONfromURL  result " + result);
			// try parse the string to a JSON object
			jArray = new JSONArray(result);
		} catch (Exception e) {
			Log.d(Constants.LOG, "Error parsing data " + e.toString());
			jArray = null;
		}

		return jArray;
	}

	public static JSONObject getJSONfromInputStream(InputStream is) {
		String result = "";
		JSONObject jArray = null;
		try {
			// convert response to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 1024);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.d(Constants.LOG, "getJSONfromURL  result " + result);
			// try parse the string to a JSON object

			jArray = new JSONObject(result);
		} catch (Exception e) {
			Log.e(Constants.LOG, "Error parsing data " + e.toString());
			jArray = null;
		}

		return jArray;
	}

}
