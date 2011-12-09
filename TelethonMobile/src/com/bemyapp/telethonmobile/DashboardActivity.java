package com.bemyapp.telethonmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bemyapp.telethonmobile.constants.Category;
import com.bemyapp.telethonmobile.constants.Constants;
import com.bemyapp.telethonmobile.tools.IconAdapter;
import com.bemyapp.telethonmobile.view.ActionBar;
import com.bemyapp.telethonmobile.view.MyObserver;
import com.bemyapp.telethonmobile.view.MySurfaceView;

public class DashboardActivity extends Activity implements
		TextToSpeech.OnInitListener, OnUtteranceCompletedListener {
	/** Called when the activity is first created. */
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	TextToSpeech myTTs;
	HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	public int selected;
	Button bAll;
	private String[] listMessage = new String[] { "Restaurants", "Café Bar",
			"Magasins", "Cinéma", "Culture", "Loisirs", "Hotel", "Santé",
			"Transport" };
	AlertDialog.Builder builder = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		MySurfaceView msv = (MySurfaceView) findViewById(R.id.surface);
		msv.disableAllLongClick(msv);

		msv.setObserver(new MyObserver() {

			@Override
			public void onChange() {
				launchVoice();
			}
		});

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar);
		actionBar.setTitle(getResources().getString(R.string.app_name)+"");
		actionBar.setActionDrawable(getResources().getDrawable(
				R.drawable.rouage));
		actionBar.showActionButton(true);

		actionBar.setActionDrawable(getResources().getDrawable(R.drawable.rouage));
		actionBar.showActionButton(true);
	

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
			Intent i = new Intent(DashboardActivity.this, MapViewActivity.class);
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
	protected void onPause() {
		myTTs.shutdown();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		
		myTTs = new TextToSpeech(this, this);

		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myTTs.shutdown();
	}

	private void launchVoice() {

		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {

			// Specify the calling package to identify your application
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			// Display an hint to the user about what he should say.
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"Speech recognition demo");

			// Given an hint to the recognizer about what the user is going to
			// say
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

			// Specify how many results you want to receive. The results will be
			// sorted
			// where the first result is the one with higher confidence.
			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

			// Specify the recognition language. This parameter has to be
			// specified
			// only if the
			// recognition has to be done in a specific language and not the
			// default
			// one (i.e., the
			// system locale). Most of the applications do not have to set this
			// parameter.

			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

		} else {
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			for (String s : matches) {
				for (Category cat : Category.values()) {
					if (s.toUpperCase().equals(cat.name())) {
						Toast.makeText(
								getApplicationContext(),
								"La categorie " + cat.name() + " a �t� demand�",
								Toast.LENGTH_SHORT).show();
					}
				}

				// if (Category.valueOf(s.toUpperCase()) != null) {
				Log.e(Constants.LOG, "string detected = " + s);
				// Toast.makeText(getApplicationContext(), "La cat�gorie " + s
				// + " a �t� demand�", Toast.LENGTH_SHORT);
				// } else
				if (s.equals("aide")) {
					Toast.makeText(getApplicationContext(),
							"Une demande d'aide a �t� demand�",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}