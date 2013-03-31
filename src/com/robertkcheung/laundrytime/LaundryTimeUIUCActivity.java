package com.robertkcheung.laundrytime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LaundryTimeUIUCActivity extends Activity {
	
    /** Called when the activity is first created. */
@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
//	  this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	  SharedPreferences sp =  getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
	  int myHall = sp.getInt("myHall", -1);
			  
	  if(myHall==-1){
		  
		  Intent pref = new Intent(LaundryTimeUIUCActivity.this, IntroActivity.class);
		  startActivity(pref);
		  //setContentView(R.layout.first);
	  }
	  else{
		  Intent i = new Intent(LaundryTimeUIUCActivity.this, HallActivity.class);
		  //Bundle b = new Bundle();
		  //b.putSerializable("param1", sp.getInt("myHall", -1)); 
		  //i.putExtra("HALL_NUM", sp.getInt("myHall", 1));
		  startActivity(i);
		  finish();
	  }

	  

	  
	}
	
	
}