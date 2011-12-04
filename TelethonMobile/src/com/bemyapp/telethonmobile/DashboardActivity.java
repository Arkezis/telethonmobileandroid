package com.bemyapp.telethonmobile;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bemyapp.telethonmobile.constants.Category;
import com.bemyapp.telethonmobile.view.ActionBar;

public class DashboardActivity extends Activity implements
		TextToSpeech.OnInitListener, OnUtteranceCompletedListener {
	/** Called when the activity is first created. */

	TextToSpeech myTTs;
	HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	public int selected;
	Button bAll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
		actionBar.setTitle("TéléthonMobile");
		myTTs = new TextToSpeech(this, this);

		GridView gridview = (GridView) this.findViewById(R.id.gridView1);
		gridview.setAdapter(new gridAdapter());

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected = arg2 + 1;
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
						String.valueOf(AudioManager.STREAM_ALARM));
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"End Message");
				myTTs.setOnUtteranceCompletedListener(DashboardActivity.this);
				myTTs.speak(Category.getCategory(selected).name,
						TextToSpeech.QUEUE_FLUSH, myHashAlarm);

			}

		});
		bAll = (Button) findViewById(R.id.button1);
		bAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selected = -1;
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
						String.valueOf(AudioManager.STREAM_ALARM));
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"End Message");
				myTTs.setOnUtteranceCompletedListener(DashboardActivity.this);
				myTTs.speak(Category.TOUT.name, TextToSpeech.QUEUE_FLUSH,
						myHashAlarm);
			}
		});

	}

	@Override
	public void onUtteranceCompleted(String uttId) {
		if (uttId.equals("End Message")) {
			myTTs.shutdown();
			Intent i = new Intent(DashboardActivity.this, ListpoiActivity.class);
			i.putExtra("category", this.selected);
			startActivity(i);
		}
	}

	@Override
	public void onInit(int status) {
		Locale loc = new Locale("fr", "", "");
		if (myTTs.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
			myTTs.setLanguage(loc);
		}
	}

	public class gridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Category.values().length;
		}

		@Override
		public Object getItem(int position) {
			return Category.values()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.activity_dashboard_button, null);
			}
			((ImageView) convertView).setImageResource(Category
					.getCategory(position + 1).drawable);
			return convertView;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myTTs.shutdown();
	}

}