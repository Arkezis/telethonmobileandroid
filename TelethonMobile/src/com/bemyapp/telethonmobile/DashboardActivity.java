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
import android.widget.Button;

public class DashboardActivity extends Activity implements TextToSpeech.OnInitListener, OnUtteranceCompletedListener{
	/** Called when the activity is first created. */
	
	Button b1;
	TextToSpeech myTTs;
	HashMap<String, String> myHashAlarm = null;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		myTTs = new TextToSpeech(this,this);

		b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myTTs.setOnUtteranceCompletedListener(DashboardActivity.this);
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
				myTTs.speak("Restaurant", TextToSpeech.QUEUE_FLUSH	, myHashAlarm);
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"End Message");
			}
		});
	}

	public void onUtteranceCompleted(String uttId) {
	    if (uttId == "End Message") {
	    	Intent i = new Intent(DashboardActivity.this,MapViewActivity.class);
	    	startActivity(i);
	    	finish();
	    } 
	}
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		Locale loc = new Locale("fr", "","");
		if(myTTs.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE){
			myTTs.setLanguage(loc);
		}
	}

}