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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SetPrefsActivity extends ListActivity{

	private int last=-1;
	String schoolCode = "";
	ListView lv;
@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.radiolist);
		ArrayList<Hall> listItems = new ArrayList<Hall>();
		ArrayAdapter<Hall> ar = new ArrayAdapter<Hall>(this, android.R.layout.simple_list_item_single_choice, listItems);
		setListAdapter(ar);
		lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setTextFilterEnabled(true);
		
		
		
		TextView heading = (TextView) findViewById(R.id.listHeading);
		heading.setText("SELECT HALL");
		
		
		SharedPreferences sp = getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
	    int hallNum = sp.getInt("myHall", -1);
	    schoolCode = sp.getString("code", "");
	    String hallName = sp.getString("hallName", "hallName");
	    if(hallNum!=-1){
	    	lv.setItemChecked(hallNum, true);
	    	last = hallNum;
	    }
	    
//		EditText filterEditText = (EditText) findViewById(R.id.editText1);
//		filterEditText.addTextChangedListener(new TextWatcher() {
//		@Override
//		    public void onTextChanged(CharSequence s, int start, int before,
//		      int count) {
//		      ar.getFilter().filter(s.toString());
//		    }
//		 
//		     @Override
//		     public void beforeTextChanged(CharSequence s, int start, int count,
//		      int after) {
//		     }
//		 
//		     @Override
//		     public void afterTextChanged(Editable s) {
//		     }
//		});
//		
		
		
		
		lv.setOnItemClickListener(new OnItemClickListener()
        {
		@Override
		   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		     long arg3) {
				last = arg2;
//				Toast.makeText(getApplicationContext(),
//						"Click ListItem Number " + arg2, Toast.LENGTH_LONG)
//						.show();
		    // TODO Auto-generated method stub
//			   CheckedTextView cv = (CheckedTextView) arg1;
//			   
//			   if(!sc){
//				   cv.setChecked(true);
//				   //if(!cv.isChecked())
//				   //cv.setChecked(true);
//				   last = arg2;
//				   sc=true;
//			   }
//			   else{
//				   CheckedTextView l = (CheckedTextView) arg0.getChildAt(last);
//				   l.setChecked(false);
//				   cv.setChecked(true);
//				   sc = true;
//			   }
			   
		   }
		   
        });
		new LoadListTask().execute();
		Typeface sego = Typeface.createFromAsset(getAssets(), "sego.ttf");
		Button b = (Button) findViewById(R.id.savePrefBtn);
		b.setTypeface(sego);
		b.setOnClickListener(new View.OnClickListener() {
			
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				
				if(last!=-1){
				Intent i = new Intent(SetPrefsActivity.this, HallActivity.class);
			    SharedPreferences sp = getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
			    Editor spEdit = sp.edit();
			    spEdit.putInt("myHall", last);
			    spEdit.putString("hallName", lv.getAdapter().getItem(last).toString());
			    spEdit.commit();
				startActivity(i);
				}
				
			}
		});
	}
	
//@Override
//	protected void onListItemClick(ListView l, View v, int pos, long id){
//		super.onListItemClick(l, v, pos, id);
//		
//		last = pos;
//	}
//	
	
	
	
	
	
	
	private class LoadListTask extends AsyncTask<Void, Hall, Void> {

		private final ProgressDialog pd = new ProgressDialog(SetPrefsActivity.this);
		 
	      // can use UI thread here
	      @SuppressWarnings("unchecked")
		protected void onPreExecute() {
	    	  ((ArrayAdapter<String>)getListAdapter()).clear();
	         this.pd.setMessage("Loading List...");
	         pd.setCancelable(false);
			pd.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading));
	         this.pd.show();
	      }
	      
		protected Void doInBackground(Void... ars) {
			 // TODO    4 new activity with custom adapter to show schedules
	    	 try {
	    		HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("http://robertkcheung.com/laundrytime/laundry.php?request=GetHallNames&code=" + schoolCode);
				
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
