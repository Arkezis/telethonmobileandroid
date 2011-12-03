package com.bemyapp.telethonmobile.tools;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyOverlayItem extends OverlayItem{

	private final GeoPoint point;
	private final Poi poi;
	public MyOverlayItem(GeoPoint _point, Poi _poi) {
		super(_point, "", "");
		this.point = _point;
		this.poi = _poi;
	}
	public GeoPoint getPoint() {
		return point;
	}
	public Poi getPoi() {
		return poi;
	}
	
	
}
