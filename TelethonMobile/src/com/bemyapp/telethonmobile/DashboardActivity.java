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

public class DashboardActivity extends Activity implements
		TextToSpeech.OnInitListener, OnUtteranceCompletedListener {
	/** Called when the activity is first created. */


	TextToSpeech myTTs;
	HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	public int selected;
	Button bAll;

	private String[] listMessage = new String[]{
			"Restaurants" ,
			"Café Bar" ,
			"Magasins" ,
			"Cinéma" ,
			"Culture" ,
			"Loisirs" ,
			"Hotel" ,
			"Santé" ,
			"Transport"};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		myTTs = new TextToSpeech(this, this);
		
		
		GridView gridview = (GridView) this.findViewById(R.id.gridView1);
		gridview.setAdapter(new gridAdapter());
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = arg2;
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
						String.valueOf(AudioManager.STREAM_ALARM));
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"End Message");
				myTTs.setOnUtteranceCompletedListener(DashboardActivity.this);
				myTTs.speak(listMessage[arg2], TextToSpeech.QUEUE_FLUSH, myHashAlarm);
				
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
				myTTs.speak("Toutes catégories", TextToSpeech.QUEUE_FLUSH, myHashAlarm);
			}
		});
		
	}

	@Override
	public void onUtteranceCompleted(String uttId) {
		if (uttId.equals("End Message")) {
			myTTs.shutdown();
			Intent i = new Intent(DashboardActivity.this, MapViewActivity.class);
			i.putExtra("category", this.selected+1);
			startActivity(i);			
			finish();
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		Locale loc = new Locale("fr", "", "");
		if (myTTs.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
			myTTs.setLanguage(loc);
		}
	}
	
	public class gridAdapter extends BaseAdapter {

		private int[] listButton = new int[]{
				R.drawable.coffee_icon,
				R.drawable.health_icon,
				R.drawable.hotel_icon,
				R.drawable.restaurant_icon,
				R.drawable.transport_icon,
				R.drawable.coffee_icon,
				R.drawable.health_icon,
				R.drawable.hotel_icon,
				R.drawable.restaurant_icon			
		};	

		public int getCount() {return this.listButton.length;}
		public Object getItem(int position) {return this.listButton[position];}
		public long getItemId(int position) {return position;}
		@Override
		
		
		public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
            	convertView = getLayoutInflater().inflate(R.layout.activity_dashboard_button, null);
            }
            ((ImageView) convertView).setImageResource(this.listButton[position]);
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