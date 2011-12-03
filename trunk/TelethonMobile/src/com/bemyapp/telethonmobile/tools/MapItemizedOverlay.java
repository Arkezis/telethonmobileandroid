package com.bemyapp.telethonmobile.tools;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.bemyapp.telethonmobile.CommentsListActivity;
import com.bemyapp.telethonmobile.R;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends com.google.android.maps.ItemizedOverlay {
	// Liste des marqueurs
	private ArrayList mOverlays = new ArrayList();
	private Context mContext;

	public MapItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	// Appeler quand on rajoute un nouvel marqueur a la liste des marqueurs
	public void addOverlay(MyOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return (OverlayItem) mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	// Appeer quand on clique sur un marqueur
	@Override
	protected boolean onTap(int index) {
		// On récupére le marqueur en question,
		// et on affiche une alerte avec le nom et description du marqueur
		final MyOverlayItem item = (MyOverlayItem) mOverlays.get(index);
		/*
		 * AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		 * alertDialog.setTitle("Nom du panneau : " + item.getTitle());
		 * alertDialog.setMessage("Description du panneau : " +
		 * item.getSnippet()); alertDialog.setButton("OK", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { return; } });
		 * alertDialog.show();
		 */

		final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		final View body = LayoutInflater.from(mContext).inflate(
				R.layout.popup_details_poi, null);
		alert.setView(body);
		alert.setPositiveButton("Ok" + item.getPoi().getName(),

		new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});
		alert.setNegativeButton("Ajouter/afficher les commentaires",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(mContext,
								CommentsListActivity.class);
						intent.putExtra("title", item.getPoi().getName());
						intent.putExtra("latitude", item.getPoint()
								.getLatitudeE6() / 1E6);
						intent.putExtra("longitude", item.getPoint()
								.getLongitudeE6() / 1E6);
						intent.putExtra("id", item.getPoi().getId());
						intent.putExtra("note", item.getPoi().getNote());
						mContext.startActivity(intent);

					}
				});
		alert.show();
		return true;
	}
}