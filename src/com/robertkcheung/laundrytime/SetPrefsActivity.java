package com.robertkcheung.laundrytime;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

public class SetPrefsActivity extends ListActivity{
/*	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	

	}*/
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.radiolist);
		ArrayList<Hall> listItems = new ArrayList<Hall>();
		ArrayAdapter<Hall> ar = new ArrayAdapter<Hall>(this, android.R.layout.simple_list_item_single_choice, listItems);
		setListAdapter(ar);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener()
        {
		boolean somethingChecked = false;
		int lastChecked;
		   public void onItemClick(AdapterView arg0, View arg1, int arg2,
		     long arg3) {
		    // TODO Auto-generated method stub
			   if(somethingChecked){
				   ListView lv = (ListView) arg0;
				   TextView tv = (TextView) lv.getChildAt(lastChecked);
				   CheckedTextView cv = (CheckedTextView) tv;
				   cv.setChecked(false);
			   }
			   ListView lv = (ListView) arg0;
			   TextView tv = (TextView) lv.getChildAt(arg2);
			   CheckedTextView cv = (CheckedTextView) tv;
			   if(!cv.isChecked())
			   cv.setChecked(true);
			   lastChecked = arg2;
			   somethingChecked=true;
		   }
        });
		new LoadListTask().execute();
		
		Button b = (Button) findViewById(R.id.savePrefBtn);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int selectedIndex = -1;
				
				for(int i=0; i<((ArrayAdapter<Hall>)getListAdapter()).getCount(); i++){
					((ArrayAdapter<Hall>)getListAdapter()).getItem(i);
				
				}
				if(selectedIndex!=-1){
				Intent i = new Intent(SetPrefsActivity.this, HallActivity.class);
				
				startActivity(i);
				}
				
			}
		});
	}
	
	
	
	
	
	
	
	private class LoadListTask extends AsyncTask<Void, Hall, Void> {

		private final ProgressDialog pd = new ProgressDialog(SetPrefsActivity.this);
		 
	      // can use UI thread here
	      @SuppressWarnings("unchecked")
		protected void onPreExecute() {
	    	  ((ArrayAdapter<String>)getListAdapter()).clear();
	         this.pd.setMessage("Loading List...");
	         this.pd.show();
	      }
	      
		protected Void doInBackground(Void... ars) {
			 // TODO    4 new activity with custom adapter to show schedules
	    	 try {
	    		HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("http://robertkcheung.com/laundrytime/laundry.php?request=GetHallNames");
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();

                //JSONArray ja = new JSONArray(client.execute(get, responseHandler));
                JSONObject job = new JSONObject(client.execute(get,responseHandler));
                JSONArray hlist = job.getJSONArray("halls");
                for (int i = 0; i < hlist.length(); i++) {
                     JSONObject jo = (JSONObject) hlist.get(i);
                     
                     publishProgress(new Hall(jo.getString("hall_number"),jo.getString("title")));
                }
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

	     @SuppressWarnings("unchecked")
		protected void onProgressUpdate(Hall... progress) {
	    	 ((ArrayAdapter<Hall>)getListAdapter()).add(progress[0]);
	    	 }

	     protected void onPostExecute(Void result) {
	    	 pd.dismiss();
	     }
	 }
}
