package com.bemyapp.telethonmobile.tools;

public class Poi {
	private String name;
	private int category;
	private Double latitude;
	private Double longitude;
	private Double note;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setNote(Double note) {
		this.note = note;
	}

	public Double getNote() {
		return note;
	}
}
