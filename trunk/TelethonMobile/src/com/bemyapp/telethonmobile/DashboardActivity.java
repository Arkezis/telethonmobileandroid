package com.bemyapp.telethonmobile;

import java.util.HashMap;
import java.util.Locale;

import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.IconAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bemyapp.telethonmobile.constants.Category;
import com.bemyapp.telethonmobile.view.ActionBar;

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
	AlertDialog.Builder builder = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
		actionBar.setTitle("TéléthonMobile");
		actionBar.setActionDrawable(getResources().getDrawable(R.drawable.rouage));
		actionBar.showActionButton(true);
	
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
		actionBar.getActionButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showChange();
			}
		});
		
		SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        int typeHandi = pref.getInt("TYPE_HANDI", -1);
		if(typeHandi==-1){ // pas de type de handicap séléectionné
        	showChange();
        }
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

	private void showChange(){
		final SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        int typeHandi = pref.getInt("TYPE_HANDI", -1);
		final ListAdapter adapt = new IconAdapter(DashboardActivity.this, R.layout.list_image_item,typeHandi);
		builder = new AlertDialog.Builder(this);
    	builder.setTitle("Votre type de handicap ?");       
    	builder.setSingleChoiceItems(adapt, typeHandi, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        Toast.makeText(getApplicationContext(), item+" "+item, Toast.LENGTH_SHORT).show();
            	SharedPreferences.Editor editor = pref.edit();
                editor.putInt("TYPE_HANDI", item);
                editor.commit();
    	        dialog.cancel();
    	    }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	
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
			return Category.values().length-1;
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