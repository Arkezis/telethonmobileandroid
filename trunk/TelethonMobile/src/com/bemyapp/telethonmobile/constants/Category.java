package com.bemyapp.telethonmobile.constants;

import com.bemyapp.telethonmobile.R;

public enum Category {

	RESTAURANT(1,R.drawable.resto_on,R.drawable.resto_poi,"Restaurants"),
	CAFE(2,R.drawable.cafe_bar_on,R.drawable.cafe_bar_poi,"Café Bar"),
	MAGASIN(3,R.drawable.magasin_on,R.drawable.magasin_poi,"Magasins"),
	CINEMA(4,R.drawable.cinema,R.drawable.cinema_poi,"Cinéma"),
	CULTURE(5,R.drawable.culture_on,R.drawable.culture_poi,"Culture"),
	LOISIR(6,R.drawable.loisirs_on,R.drawable.loisirs_poi,"Loisirs"),
	HOTEL(7,R.drawable.hotel_on,R.drawable.hotel_poi,"Hotel"),
	SANTE(8,R.drawable.sante_on,R.drawable.sante_poi,"Santé"),
	TRANSPORT(9,R.drawable.transp_on,R.drawable.transp_poi,"Transport");

	public int number;
	public int drawable;
	public int poiDrawable;
	public String name;
	
	private Category(int _number, int _drawable, int _poiDrawable, String _name){
		this.number = _number;
		this.drawable = _drawable;
		this.poiDrawable = _poiDrawable;
		this.name = _name;
	}
	
	public static Category getCategory(int number){
		if (RESTAURANT.number == number) return RESTAURANT;
		if (CAFE.number == number) return CAFE;
		if (MAGASIN.number == number) return MAGASIN;
		if (CINEMA.number == number) return CINEMA;
		if (CULTURE.number == number) return CULTURE;
		if (LOISIR.number == number) return LOISIR;
		if (HOTEL.number == number) return HOTEL;
		if (SANTE.number == number) return SANTE;
		return TRANSPORT;
	}
	
}
