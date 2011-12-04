package com.bemyapp.telethonmobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bemyapp.telethonmobile.tools.Comment;
import com.bemyapp.telethonmobile.tools.CommentAdapter;
import com.bemyapp.telethonmobile.tools.GetListComments;
import com.bemyapp.telethonmobile.tools.SaveNewComment;
import com.bemyapp.telethonmobile.view.ActionBar;

public class CommentsListActivity extends ListActivity {

	private ArrayList<Comment> comments = null;
	private String title = null;
	private Double latitude = null;
	private Double longitude = null;
	private int id = 0;
	private Double note = null;
	private String add;

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
			note = b.getDouble("note");
		}

		ActionBar ab = (ActionBar) findViewById(R.id.actionBar);
		ab.setTitle(title);


		TextView adresseTV = (TextView) findViewById(R.id.adresse);
		adresseTV.setText("");
		if (latitude != null && longitude != null) {
			Geocoder geoCoder = new Geocoder(getBaseContext(),
					Locale.getDefault());
			try {
				List<Address> addresses = geoCoder.getFromLocation(latitude,
						longitude, 1);

				add = "";
				if (addresses.size() > 0) {
					for (int i = 0; i < addresses.get(0)
							.getMaxAddressLineIndex(); i++) {
						add += addresses.get(0).getAddressLine(i) + "\n";
					}
				}
				adresseTV.setText(add);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RatingBar ratingTV = (RatingBar) findViewById(R.id.ratingBar);
		ratingTV.setRating(Float.parseFloat(note.toString()));

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

		// Button addCommentButton = (Button) findViewById(R.id.addComment);
		ImageButton addCommentButton = ab.getActionButton();
		ab.setActionDrawable(getResources().getDrawable(R.drawable.plus));
		ab.showActionButton(true);
		addCommentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						CommentsListActivity.this);
				LayoutInflater inflater = getLayoutInflater();
				final View body = inflater.inflate(R.layout.popup_new_comment,
						null);
				alert.setView(body);

				int maxLength = 140;
				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(maxLength);
				((EditText) body.findViewById(R.id.message))
						.setFilters(FilterArray);
				alert.setPositiveButton("Envoyer",

				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText et = (EditText) body
								.findViewById(R.id.message);
						RatingBar rb = (RatingBar) body
								.findViewById(R.id.ratingBar);

						new SaveNewComment(id, rb.getRating(), et.getText()
								.toString()).execute();
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
		Button goTo = (Button) this.findViewById(R.id.goTo);
		goTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add != null) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("google.navigation:q=" + add));
					startActivity(i);
				}
			}
		});
		if (add == null) {
			goTo.setVisibility(0);
		} else {
			goTo.setVisibility(1);
		}
	}

	public void fillList() {
		getListView().setAdapter(new CommentAdapter(comments, this));
	}
}