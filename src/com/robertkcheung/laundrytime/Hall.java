package com.robertkcheung.laundrytime;

import java.io.Serializable;

public class Hall implements Serializable{
	private static final long serialVersionUID = 12928735257985L;
	public String hall_number;
	public String title;
    public String washers_available;
    public String dryers_available;
    public String washers_in_use;
    public String dryers_in_use;

    public Hall(String num, String t, String aw, String ad, String wiu, String diu) {
    super();
      hall_number = num;
      title = t;
      washers_available = aw;
      dryers_available = ad;
      washers_in_use = wiu;
      dryers_in_use = diu;
    }
    
    public String toString() {
    	return title;
    }
    
    public String getHallName(){
    	return title;
    }
    
    public String getWashers(){
    	return washers_available;
    }
    public String getDryers(){
    	return dryers_available;
    }
    public String getWashersInUse(){
    	return washers_in_use;
    }
    public String getDryersInUse(){
    	return dryers_in_use;
    }
}
