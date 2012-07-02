package com.robertkcheung.laundrytime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowHallActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      setContentView(R.layout.main);
	      

	      Bundle bundle = this.getIntent().getExtras();
	      Hall param1 = (Hall) bundle.getSerializable("param1");
	      
	      int wavailable = Integer.parseInt(param1.washers_available);
	      //int winuse = Integer.parseInt(param1.washers_in_use);
	      int winuse = (int) (wavailable * 0.7);
	      int davailable = Integer.parseInt(param1.dryers_available);
	      int dinuse = Integer.parseInt(param1.dryers_in_use);
	      
	      ProgressBar wprog = (ProgressBar) findViewById(R.id.washerProgress);
	      ProgressBar dprog = (ProgressBar) findViewById(R.id.dryerProgress);
	      
//	      Bitmap b = ((BitmapDrawable) d).getBitmap();
//	      
//	      Drawable d2 = new BitmapDrawable(Bitmap.createScaledBitmap(b, 93/wavailable, 20, true));
	      
	      
	      wprog.setMax(wavailable);
	      //wprog.setSecondaryProgress(wavailable-winuse);
	      wprog.setProgress(wavailable-winuse);
	      dprog.setMax(davailable);
	      dprog.setProgress(davailable-dinuse);
	      
	      TextView hallname = (TextView) findViewById(R.id.textViewHall);
	      //shrinkTextToFit(hallname.getWidth(),hallname,hallname.getTextSize(),20f);
	      TextView nwashers = (TextView) findViewById(R.id.numWashers);
	      TextView ndryers = (TextView) findViewById(R.id.numDryers);
	      TextView time = (TextView) findViewById(R.id.timeRemaining);
	      TextView time2 = (TextView) findViewById(R.id.timeRemaining2);
	      //Drawable d = (Drawable) getResources().getDrawable(R.drawable.dryer);
	      //Bitmap b = ((BitmapDrawable) d).getBitmap();
	      
	      //Drawable d2 = new BitmapDrawable(Bitmap.createScaledBitmap(b, 93/wavailable, 20, true));
	      
	      //Item i = (Item) findViewById(android.R.id.progress);
	      
	     
	      
	      hallname.setText(param1.title);
	      ndryers.setText((davailable-dinuse) + " OF " + davailable);
	      nwashers.setText((wavailable-winuse) + " OF " + wavailable);
	      time.setText("0:0:00"); time2.setText("0:0:00");
	}
	

}