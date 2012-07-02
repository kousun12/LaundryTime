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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LaundryTimeUIUCActivity extends ListActivity {
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
				new LoadListTask().execute();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return true; 
	}
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  ArrayList<Hall> listItems = new ArrayList<Hall>();
	  ArrayAdapter<Hall> ar = new ArrayAdapter<Hall>(this, R.layout.list_item, listItems);
	  setListAdapter(ar);
	  
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);
	  
	  lv.setOnItemClickListener(new OnItemClickListener() {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

		      //Toast.makeText(getApplicationContext(), ((ArrayAdapter<Hall>)getListAdapter()).getItem(position).id_str,
		       //   Toast.LENGTH_SHORT).show();
			
			Intent i = new Intent(LaundryTimeUIUCActivity.this, ShowHallActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("param1",  ((ArrayAdapter<Hall>)getListAdapter()).getItem(position));
	        i.putExtras(bundle);
	        startActivity(i);
		}
	  });
	  
	  new LoadListTask().execute();
	}
	
	private class LoadListTask extends AsyncTask<Void, Hall, Void> {

		private final ProgressDialog pd = new ProgressDialog(LaundryTimeUIUCActivity.this);
		 
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
				HttpGet get = new HttpGet("http://robertkcheung.com/laundrytime/laundry.php?request=GetHallList");
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get, responseHandler));
                //JSONArray ja = new JSONArray(client.execute(get, responseHandler));
				JSONArray ja = jsonob.getJSONArray("halls");
                for (int i = 0; i < ja.length(); i++) {
                     JSONObject jo = (JSONObject) ja.get(i);
                     publishProgress(new Hall(jo.getString("hall_number"),jo.getString("title"),jo.getString("washers_available"), jo.getString("dryers_available"), jo.getString("washers_in_use"), jo.getString("dryers_in_use")));
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