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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;

//TODO: 
//add choose school
//remove ETA
//
public class HallActivity extends Activity {
	public static int hallNum;
	public Hall param1;
	public int wminsLeft;
	public int dminsLeft;
	int totalw;
	int totald;
	String code;
	Typeface aroni;
	Typeface sego;

@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
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
		if (item.getItemId() == R.id.reloadButton) {
			try {
				new LoadHallInfo().execute();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if (item.getItemId() == R.id.changePrefButton) {
			try {
				startActivity(new Intent(HallActivity.this,
						SchoolActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return true;
	}

@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		SharedPreferences sp = getSharedPreferences("schoolPref",
				MODE_WORLD_READABLE);
		aroni = Typeface.createFromAsset(this.getAssets(), "aroni.ttf");
		sego = Typeface.createFromAsset(this.getAssets(), "sego.ttf");

		// Parse.initialize(this, "hne38uR4KKnD8kMHsl69nO0lKPAW1sMNB8FZsjML",
		// "ymJ1dkY82xrAyscTeSBqwFUwIXMrTh8X0pMFZj5H");
		// PushService.subscribe(this, "myTestDev", HallActivity.class);
		ViewGroup root = (ViewGroup) findViewById(R.id.scrollV);
		setFont(root, sego);
		ParsePush push = new ParsePush();
		push.setChannel("myTestDev");
		// push.setMessage("TestPush");
		// push.sendInBackground();

		hallNum = sp.getInt("myHall", -1);
		code = sp.getString("code", "");
		new LoadHallInfo().execute();
		// Bundle bundle = this.getIntent().getExtras();
		// Hall param1 = (Hall) bundle.getSerializable("param1");

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
				HttpGet get = new HttpGet(
						"http://robertkcheung.com/laundrytime/laundry.php?request=GetHallList&code="
								+ code);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get,
						responseHandler));
				// JSONArray ja = new JSONArray(client.execute(get,
				// responseHandler));
				JSONArray ja = jsonob.getJSONArray("halls");

				JSONObject jo = (JSONObject) ja.get(hallNum);
				param1 = new Hall(jo.getString("hall_number"),
						jo.getString("title"),
						jo.getString("washers_available"),
						jo.getString("dryers_available"),
						jo.getString("washers_in_use"),
						jo.getString("dryers_in_use"));

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
			// ((ArrayAdapter<Hall>)getListAdapter()).add(progress[0]);
		}

		protected void onPostExecute(Void result) {
			pd.dismiss();
			finisheverything();
		}
	}

	private class getWashTimes extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://robertkcheung.com/laundrytime/laundry.php?request=GetMachinesByHall&hall_number="
								+ hallNum + "&code=" + code);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get,
						responseHandler));
				JSONArray ja = jsonob.getJSONArray("machines");
				wminsLeft = 120;
				int ta = totalw;
				for (int i = 0; i < ta; i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					int time;
					if (jo.getString("status").startsWith("In Use")) {
						time = Integer.parseInt(jo.getString("time_remaining")
								.replace(" min", ""));
						wminsLeft = time < wminsLeft ? time : wminsLeft;
					}
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

		protected void onPostExecute(Void result) {

			// ParsePush pp = new ParsePush();
			// pp.setChannel("myTestDev");
			// pp.setMessage(dminsLeft + "until a machine opens");
			// pp.sendInBackground();
			if (wminsLeft == 120) {
				Toast errortoast = Toast
						.makeText(HallActivity.this.getApplicationContext(),
								"ETA currently not available for this hall",
								Toast.LENGTH_LONG);
				errortoast.show();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(Calendar.getInstance().getTime());
				cal.add(Calendar.MINUTE, wminsLeft);

				Intent intent = new Intent(HallActivity.this,
						AlarmReciever.class);
				intent.putExtra("note",
						"Washer Available!");
				intent.putExtra("title", "SUDS");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						HallActivity.this.getApplicationContext(), 1253,
						intent, PendingIntent.FLAG_UPDATE_CURRENT
								| Intent.FILL_IN_DATA);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						cal.getTimeInMillis(), pendingIntent);
				Toast t = Toast.makeText(getApplicationContext(),
						"Notification set for " + wminsLeft
								+ " minutes from now", Toast.LENGTH_LONG);
				t.show();
				TextView btn = (TextView) findViewById(R.id.btnDryer);
				btn.setClickable(false);
				btn.setText("NOTIFICATION SET");
				btn.setBackgroundColor(getResources().getColor(
						R.color.sudsdarkgray));
			}
		}

	}

	private class getDryTimes extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://robertkcheung.com/laundrytime/laundry.php?request=GetMachinesByHall&hall_number="
								+ hallNum + "&code=" + code);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				JSONObject jsonob = new JSONObject(client.execute(get,
						responseHandler));
				JSONArray ja = jsonob.getJSONArray("machines");
				dminsLeft = 120;
				int ta = totalw + totald;
				for (int i = totalw; i < ta; i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					int time;
					if (jo.getString("status").startsWith("In Use")) {
						time = Integer.parseInt(jo.getString("time_remaining")
								.replace(" min", ""));
						dminsLeft = time < dminsLeft ? time : dminsLeft;
					}
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

		protected void onPostExecute(Void result) {

			// ParsePush pp = new ParsePush();
			// pp.setChannel("myTestDev");
			// pp.setMessage(dminsLeft + "until a machine opens");
			// pp.sendInBackground();
			if (dminsLeft == 120) {
				Toast errortoast = Toast
						.makeText(HallActivity.this.getApplicationContext(),
								"ETA currently not available for this hall",
								Toast.LENGTH_LONG);
				errortoast.show();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(Calendar.getInstance().getTime());
				cal.add(Calendar.MINUTE, dminsLeft);

				Intent intent = new Intent(HallActivity.this,
						AlarmReciever.class);
				intent.putExtra("note",
						"Dryer Available!");
				intent.putExtra("title", "SUDS");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						HallActivity.this.getApplicationContext(), 1253,
						intent, PendingIntent.FLAG_UPDATE_CURRENT
								| Intent.FILL_IN_DATA);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						cal.getTimeInMillis(), pendingIntent);
				Toast t = Toast.makeText(getApplicationContext(),
						"Notification set for " + dminsLeft
								+ " minutes from now", Toast.LENGTH_LONG);
				t.show();
				TextView btn = (TextView) findViewById(R.id.btnDryer);
				btn.setClickable(false);
				btn.setText("NOTIFICATION SET");
				btn.setBackgroundColor(getResources().getColor(
						R.color.sudsdarkgray));
			}
		}

	}

	void finisheverything() {

		try {
			int wavailable = Integer.parseInt(param1.washers_available);
			int winuse = Integer.parseInt(param1.washers_in_use);
			totalw = wavailable + winuse;
			int davailable = Integer.parseInt(param1.dryers_available);
			int dinuse = Integer.parseInt(param1.dryers_in_use);
			totald = davailable + dinuse;


			TextView hallname = (TextView) findViewById(R.id.textViewHall);
			TextView nwashers = (TextView) findViewById(R.id.nWash);
			TextView dwashers = (TextView) findViewById(R.id.dWash);

			TextView ndryers = (TextView) findViewById(R.id.nDryer);
			TextView ddryers = (TextView) findViewById(R.id.dDryer);

			// TextView time = (TextView) findViewById(R.id.timeRemaining);
			// TextView time2 = (TextView) findViewById(R.id.timeRemaining2);

			hallname.setText(param1.title.toUpperCase());
			hallname.setTypeface(aroni);

			if (davailable == 0) {
				TextView btn = (TextView) findViewById(R.id.btnDryer);
				btn.setClickable(true);
				btn.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.no_more_machines_btn));
				btn.setText("Notify Me When Available".toUpperCase());
				btn.setOnClickListener(new View.OnClickListener() {

				@Override
					public void onClick(View v) {
						new getDryTimes().execute();
					}
				});

			}
			if (wavailable == 0) {
				TextView btn = (TextView) findViewById(R.id.btnWasher);
				btn.setClickable(true);
				btn.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.no_more_machines_btn));
				btn.setText("Notify Me When Available".toUpperCase());
				btn.setOnClickListener(new View.OnClickListener() {

				@Override
					public void onClick(View v) {
						new getWashTimes().execute();
					}
				});
			}
			ndryers.setText(Integer.toString(davailable));
			nwashers.setText(Integer.toString(wavailable));
			ddryers.setText(Integer.toString(totald));
			dwashers.setText(Integer.toString(totalw));
			ndryers.setTypeface(aroni);
			nwashers.setTypeface(aroni);
			ddryers.setTypeface(aroni);
			dwashers.setTypeface(aroni);
		} catch (NullPointerException npe) {

		}
	}

	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button /* etc. */)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}
}
