package com.robertkcheung.laundrytime;

public class IndHall {
	public String title;
    public String washers_available;
    public String dryers_available;
    public String washers_in_use;
    public String dryers_in_use;

    public IndHall(String t, String aw, String ad, String wiu, String diu) {
    super();

      title = t;
      washers_available = aw;
      dryers_available = ad;
      washers_in_use = wiu;
      dryers_in_use = diu;
    }
}
