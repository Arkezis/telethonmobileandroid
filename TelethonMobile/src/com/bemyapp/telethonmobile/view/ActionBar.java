package com.bemyapp.telethonmobile.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bemyapp.telethonmobile.R;

public class ActionBar extends LinearLayout {
	public ActionBar(Context context, AttributeSet attr) {
		super(context, attr);
		initializeLayoutBasics(context);

	}

	private void initializeLayoutBasics(Context context) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.actionbar, this);
	}

	public void setTitle(String title) {
		TextView titleTV = (TextView) findViewById(R.id.title);
		titleTV.setText(title);
	}

	public void setActionDrawable(Drawable imageAction) {
		ImageButton ib = (ImageButton) findViewById(R.id.btn_title_action);
		ib.setImageDrawable(imageAction);
	}

	public void showProgressDialog(boolean activated) {
		RelativeLayout progressBlock = (RelativeLayout) findViewById(R.id.progressBlock);
		if (activated) {
			progressBlock.setVisibility(View.VISIBLE);
		} else {
			progressBlock.setVisibility(View.GONE);
		}
	}

	public void showActionButton(boolean activated) {
		ImageButton ib = (ImageButton) findViewById(R.id.btn_title_action);
		if (activated) {
			ib.setVisibility(View.VISIBLE);
		} else {
			ib.setVisibility(View.GONE);
		}
	}

	public ImageButton getActionButton() {
		return (ImageButton) findViewById(R.id.btn_title_action);
	}

}