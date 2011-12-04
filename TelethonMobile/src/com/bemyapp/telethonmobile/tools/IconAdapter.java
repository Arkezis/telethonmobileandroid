package com.bemyapp.telethonmobile.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bemyapp.telethonmobile.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

class ViewHolder {
	ImageView icon;
	CheckedTextView name;
}

public class IconAdapter extends ArrayAdapter<Integer>{
	private Context mContext;
	private final static List<Integer> mIconList = new ArrayList<Integer>(){
		{
			add(R.drawable.handicapmoteur);
			add(R.drawable.handicapmental);
			add(R.drawable.handicapauditif);
			add(R.drawable.handicapvisuel);
		}
	};
	final CharSequence[] mTextList = {"Moteur", "Mental", "Auditif", "Visuel"};
	private ViewHolder vHolder;
	private int mtypeHandi;
	
	public IconAdapter(Context context, int textViewResourceId,int typeHandi) {
		super(context, textViewResourceId, mIconList);
		mtypeHandi = typeHandi;
		mContext = context;
	}
	
	 public Integer getItem(int position) {
	      return mIconList.get(position);
	  }

	  public long getItemId(int position) {
	      return position;
	  }

	  public View getView(int position, View convertView, ViewGroup parent){
		  final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  if(convertView== null){
			  convertView = inflater.inflate(R.layout.list_image_item, null);
			  
			  vHolder = new ViewHolder();
			  vHolder.icon  = (ImageView) convertView.findViewById(R.id.icon);
			  vHolder.name = (CheckedTextView) convertView.findViewById(R.id.name);
			  convertView.setTag(vHolder);
		  }else{
			  vHolder = (ViewHolder) convertView.getTag();
		  }
		  if(position==mtypeHandi) vHolder.name.setChecked(true);
		  Drawable tile = mContext.getResources().getDrawable(mIconList.get(position));
		  vHolder.icon.setImageDrawable(tile);
		  vHolder.name.setText(mTextList[position]);
		  
		  return convertView;
	  }
	  
}
