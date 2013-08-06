package com.robertkcheung.laundrytime;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class DetailActivity extends FragmentActivity {
	private PullToRefreshListView mPullRefreshListView;
	private DetailAdapter mAdapter;
	private String code;
	private int hallNum;
	private DetailHall machineList;
	private String hallName;
	private boolean isWasher;
	private SharedPreferences sp;
	FragmentManager fm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		isWasher = getIntent().getExtras().getBoolean("isWasher");
		machineList = new DetailHall();
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.detailListView);
		fm = this.getSupportFragmentManager();
		sp = this.getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView fetch = (TextView) arg1.findViewById(R.id.detail_fetch);
				TextView notresponding = (TextView) arg1.findViewById(R.id.detailNotResponding);
				TextView justfinished = (TextView) arg1.findViewById(R.id.detail_just_finished);
				TextView inuse = (TextView) arg1.findViewById(R.id.detailinUse);
				TextView avail = (TextView) arg1.findViewById(R.id.detailAvailable);
				TextView timeremaining = (TextView) arg1.findViewById(R.id.detailTimeRemaining);
				SetAlarmDialogFragment dialog = new SetAlarmDialogFragment();
				dialog.show(fm, "ye");
				if(inuse.getVisibility()==View.VISIBLE){
				}
			}
			
		});

		
		SharedPreferences sp = getSharedPreferences("schoolPref",MODE_WORLD_READABLE);
		hallNum = sp.getInt("myHall", -1);
		code = sp.getString("code", "");
		hallName = sp.getString("hallName", "Hall");
		
		mAdapter = new DetailAdapter(this, R.layout.detail_item, machineList, isWasher);
		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		mPullRefreshListView.setAdapter(mAdapter);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		TextView title = (TextView) findViewById(R.id.detailHallName);
		title.setText(hallName.toUpperCase());
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		//mPullRefreshListView.setRefreshing();
		//new GetDataTask().execute();
		new FirstTimeLoad().execute();
		
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... ars) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://robertkcheung.com/laundrytime/laundry.php?request=GetMachinesByHall&hall_number="
								+ hallNum + "&code=" + code);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get,
						responseHandler));
				JSONArray ja = jsonob.getJSONArray("machines");
				machineList.machines.clear();
				for(int i=0;i<ja.length();i++){
					JSONObject jo = (JSONObject) ja.get(i);
					String type = jo.getString("type");
					if(isWasher&&type.contains("Dryer"))
						continue;
					if(!isWasher&&type.contains("Washer"))
						continue;
					Machine currMachine = new Machine(
							Integer.parseInt(jo.getString("machine")),
							jo.getString("status"),
							jo.getString("time_remaining"));
					machineList.add(currMachine);
				}
				System.out.println(ja.toString());
				

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

		@Override
		protected void onPostExecute(String[] result) {
			mAdapter.notifyDataSetChanged();
			
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	private class FirstTimeLoad extends AsyncTask<Void, Void, String[]> {
		private final ProgressDialog pd = new ProgressDialog(DetailActivity.this);

		// can use UI thread here
		protected void onPreExecute() {
			String what = isWasher? "Washing Machine":"Drying Machine";
			this.pd.setMessage(" Loading "+ what +" Info...");
			pd.setCancelable(false);
			pd.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading));
			this.pd.show();
		}
		@Override
		protected String[] doInBackground(Void... ars) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://robertkcheung.com/laundrytime/laundry.php?request=GetMachinesByHall&hall_number="
								+ hallNum + "&code=" + code);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get,
						responseHandler));
				JSONArray ja = jsonob.getJSONArray("machines");
				machineList.machines.clear();
				for(int i=0;i<ja.length();i++){
					JSONObject jo = (JSONObject) ja.get(i);
					String type = jo.getString("type");
					if(isWasher&&type.contains("Dryer"))
						continue;
					if(!isWasher&&type.contains("Washer"))
						continue;
					Machine currMachine = new Machine(
							Integer.parseInt(jo.getString("machine")),
							jo.getString("status"),
							jo.getString("time_remaining"));
					machineList.add(currMachine);
				}
				System.out.println(ja.toString());
				

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

		@Override
		protected void onPostExecute(String[] result) {
			mAdapter.notifyDataSetChanged();
			pd.dismiss();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.reloadButton) {
			try {
				//new LoadHallInfo().execute();
				mPullRefreshListView.setRefreshing(true);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if (item.getItemId() == R.id.changePrefButton) {
			try {
				startActivity(new Intent(DetailActivity.this,
						SchoolActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return true;
		
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		mPullRefreshListView.setRefreshing(true);
	}


}
