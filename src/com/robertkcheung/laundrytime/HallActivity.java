package com.robertkcheung.laundrytime;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;




//TODO: 
	//add choose school
	//remove ETA
	//
public class HallActivity extends Activity {
	public static int hallNum;
	public Hall param1;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return super.onPrepareOptionsMenu(menu);
	}
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
		if(item.getItemId() == R.id.reloadButton)
		{
			try {
				new LoadHallInfo().execute();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else if(item.getItemId() == R.id.changePrefButton)
		{
			try {
				startActivity(new Intent(HallActivity.this,SetPrefsActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return true; 
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      setContentView(R.layout.main);
	      SharedPreferences sp = getSharedPreferences("hallPref", MODE_WORLD_READABLE);
	      hallNum = sp.getInt("myHall", -1);
	      new LoadHallInfo().execute();
	      //Bundle bundle = this.getIntent().getExtras();
	      //Hall param1 = (Hall) bundle.getSerializable("param1");

	}
	
	private class LoadHallInfo extends AsyncTask<Void, Hall, Void> {

		private final ProgressDialog pd = new ProgressDialog(HallActivity.this);
		 
	      // can use UI thread here
		protected void onPreExecute() {
	         this.pd.setMessage(" Loading Hall Info...");
	         this.pd.show();
	      }
	      
		protected Void doInBackground(Void... ars) {
	    	 try {
	    		HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("http://robertkcheung.com/laundrytime/laundry.php?request=GetHallList");
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get, responseHandler));
                //JSONArray ja = new JSONArray(client.execute(get, responseHandler));
				JSONArray ja = jsonob.getJSONArray("halls");
                
                     JSONObject jo = (JSONObject) ja.get(hallNum);
                     param1 = new Hall(jo.getString("hall_number"),jo.getString("title"),jo.getString("washers_available"), jo.getString("dryers_available"), jo.getString("washers_in_use"), jo.getString("dryers_in_use"));
                
	    	 } catch (IOException e) {
	             // TODO Auto-generated catch block
	             e.printStackTrace();
	         } catch (JSONException e) {
	             // TODO Auto-generated catch block
	             e.printStackTrace();
	         } catch (Exception e) {
				// TODO: handle exception
	        	 e.printStackTrace();
			}
	    	 
	    	 return null;
	     }

		protected void onProgressUpdate(Hall... progress) {
	    	 //((ArrayAdapter<Hall>)getListAdapter()).add(progress[0]);
	    	 }

	     protected void onPostExecute(Void result) {
	    	 pd.dismiss();
	    	 finisheverything();
	     }
	 }
	
	void finisheverything(){
	      
	      
	      int wavailable = Integer.parseInt(param1.washers_available);
	      int winuse = Integer.parseInt(param1.washers_in_use);
	      int totalw = wavailable + winuse;
	      //int winuse = (int) (wavailable * 0.7);
	      int davailable = Integer.parseInt(param1.dryers_available);
	      int dinuse = Integer.parseInt(param1.dryers_in_use);
	      int totald = davailable + dinuse;
	      
	      ProgressBar wprog = (ProgressBar) findViewById(R.id.washerProgress);
	      ProgressBar dprog = (ProgressBar) findViewById(R.id.dryerProgress);
	      
//	      Bitmap b = ((BitmapDrawable) d).getBitmap();
//	      
//	      Drawable d2 = new BitmapDrawable(Bitmap.createScaledBitmap(b, 93/wavailable, 20, true));
	      
	      
	      wprog.setMax(totalw);
	      //wprog.setSecondaryProgress(wavailable-winuse);
	      wprog.setProgress(totalw-winuse);
	      dprog.setMax(totald);
	      dprog.setProgress(totald-dinuse);
	      
	      TextView hallname = (TextView) findViewById(R.id.textViewHall);
	      //shrinkTextToFit(hallname.getWidth(),hallname,hallname.getTextSize(),20f);
	      TextView nwashers = (TextView) findViewById(R.id.numWashers);
	      TextView ndryers = (TextView) findViewById(R.id.numDryers);
	      //TextView time = (TextView) findViewById(R.id.timeRemaining);
	      //TextView time2 = (TextView) findViewById(R.id.timeRemaining2);
	      //Drawable d = (Drawable) getResources().getDrawable(R.drawable.dryer);
	      //Bitmap b = ((BitmapDrawable) d).getBitmap();
	      
	      //Drawable d2 = new BitmapDrawable(Bitmap.createScaledBitmap(b, 93/wavailable, 20, true));
	      
	      //Item i = (Item) findViewById(android.R.id.progress);
	      
	     
	      
	      hallname.setText(param1.title);
	      ndryers.setText((totald-dinuse) + " OF " + totald);
	      nwashers.setText((totalw-winuse) + " OF " + totalw);
	      
	      if(davailable <= (int)(totald*0.2) || davailable==1){
	    	  ndryers.setTextColor(getResources().getColor(R.color.low_machines));
	      }if(wavailable <= (int)(totalw*0.2) || wavailable==1){
	    	  ndryers.setTextColor(getResources().getColor(R.color.low_machines));
	      }
	      if(dinuse == totald){
	    	  ndryers.setTextColor(getResources().getColor(R.color.no_more_machines));
	      }
	      if(winuse == totalw){
	    	  nwashers.setTextColor(getResources().getColor(R.color.no_more_machines));
	      }
	      //time.setText("0:0:00"); time2.setText("0:0:00");
	}
}
