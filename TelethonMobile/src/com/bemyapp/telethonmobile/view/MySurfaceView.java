package com.bemyapp.telethonmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bemyapp.telethonmobile.constants.Constants;

public class MySurfaceView extends LinearLayout {

	private MyObserver myobserver = null;

	private OnLongClickListener myOnClick;

	public OnLongClickListener getMyOnClick() {
		return myOnClick;
	}

	private OnItemLongClickListener myOnItemClick;

	public OnItemLongClickListener getMyOnItemClick() {
		return myOnItemClick;
	}

	public MySurfaceView(final Context context, AttributeSet attr) {
		super(context, attr);

		myOnClick = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
				return true;
			}
		};

		myOnItemClick = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(android.widget.AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				preventObserver();
				return true;
			};
		};

		disableAllLongClick(this);
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				preventObserver();
				return true;
			}
		});

	}

	public void setObserver(MyObserver _observer) {
		this.myobserver = _observer;
	}

	public void disableAllLongClick(View v) {
		Log.e(Constants.LOG, "view  = " + v);
		if (v == null) {
			Log.e(Constants.LOG, "view  null");
			return;
		}
		if (!(v instanceof ViewGroup)) {
			Log.e(Constants.LOG, "not view group");
			return;
		}
		ViewGroup vg = (ViewGroup) v;
		if (vg instanceof GridView) {
			Log.e(Constants.LOG, "not childs");
			((GridView) vg).setOnItemLongClickListener(myOnItemClick);
		}
		if (vg.getChildCount() == 0) {
			Log.e(Constants.LOG, "not childs");
			return;
		}

		for (int i = 0; i < vg.getChildCount(); i++) {
			View newV = vg.getChildAt(i);
			newV.setOnLongClickListener(myOnClick);
			disableAllLongClick(newV);
		}

	}

	public void preventObserver() {
		if (myobserver != null) {
			myobserver.onChange();
		}
	}
}