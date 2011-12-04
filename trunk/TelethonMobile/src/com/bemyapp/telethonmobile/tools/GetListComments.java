package com.bemyapp.telethonmobile.tools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetListComments extends AsyncTask<Void, Void, Void> {

	private ArrayList<Comment> comments = null;
	private final int id;

	public GetListComments(int _id) {
		this.id = _id;
	}

	@Override
	protected Void doInBackground(Void... params) {
		comments = new ArrayList<Comment>();
		String url = "http://cilheo.fr/telethonMobileTrunk.php?action=getNote&id="
				+ id;

		JSONArray array = JSONTools.getJSONArrayfromURL(url);
		for (int i = 0; array != null && i < array.length(); i++) {
			try {
				JSONObject object = array.getJSONObject(i);
				Comment c = new Comment();
				c.setComment(object.getString("com"));
				c.setNote(object.getLong("note"));
				comments.add(c);

			} catch (JSONException e) {
			}
		}
		return null;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}
}
