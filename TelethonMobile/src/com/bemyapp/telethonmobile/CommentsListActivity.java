package com.bemyapp.telethonmobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bemyapp.telethonmobile.tools.Comment;
import com.bemyapp.telethonmobile.tools.CommentAdapter;
import com.bemyapp.telethonmobile.tools.GetListComments;

public class CommentsListActivity extends ListActivity {

	private ArrayList<Comment> comments = null;
	private String title = null;
	private Double latitude = null;
	private Double longitude = null;
	private int id = 0;
	private float note = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			title = b.getString("title");
			latitude = b.getDouble("latitude");
			longitude = b.getDouble("longitude");
			id = b.getInt("id");
			note = b.getFloat("note");
		}

		TextView titleTV = (TextView) findViewById(R.id.title);
		titleTV.setText(title);

		TextView adresseTV = (TextView) findViewById(R.id.adresse);
		adresseTV.setText("");
		if (latitude != null && longitude != null) {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = geocoder.getFromLocation(latitude,
						longitude, 1);
				if (addresses.size() > 0) {
					Address adress = addresses.get(0);
					String adresseString = "";
					int i = 0;

					String adresseLine = null;
					while ((adresseLine = adress.getAddressLine(i)) != null) {
						adresseString += adresseLine + "\n";
						i++;
					}
					adresseString += " - ";
					adresseString += adress.getCountryName();

					;
				}
			} catch (IOException e) {
			}
		}
		RatingBar ratingTV = (RatingBar) findViewById(R.id.ratingBar);
		ratingTV.setRating(note);

		View v = LayoutInflater.from(this).inflate(R.layout.empty_screen, null,
				false);
		getListView().setEmptyView(v);

		new GetListComments(id) {
			ProgressDialog pd = null;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(CommentsListActivity.this);
				try {
					pd.show();
				} catch (Exception e) {
				}
			};

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				comments = getComments();
				fillList();
				try {
					pd.dismiss();
				} catch (Exception e) {
				}
			}
		}.execute();
	}

	public void fillList() {
		getListView().setAdapter(new CommentAdapter(comments, this));
	}
}