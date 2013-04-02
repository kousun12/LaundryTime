package com.robertkcheung.laundrytime;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SchoolActivity extends ListActivity{
	String [] schools = {"University of Illinois Urbana-Champaign","Augsburg College","Augsburg College","Buena Vista University","California Polytechnic State Univeristy","California State University - Monterery Bay","Central Michigan University","Duke University","Fort Hays State University","Humboldt State University","Iowa State University","Lehigh University","Louisiana State University","Missouri State University","Pacific University","Rensselaer Polytechnic Institute","Rice University","Rollins College","Saint Louis University","Stanford University","Towson Univeristy","Trinity University","Univeristy of Louisville","Univeristy of Nebraska-Kearney","University of California-Los Angeles","University of Dayton","University of Northern Colorado","University of Oklahoma","University of Pennsylvania","University of Southern Indiana","University of Toronto Mississauga","University of Virginia","University of Wisconsin-Milwaukee","Vista Del Campo Norte","Washburn University","Xavier University","Yeshiva University"};
	String [] codes = {"urba7723","ac4560","ac4560","bvu999","uhcpslo","csumb721","cmu4936","du9458","fhsu5007","hsu3034","isu7983","lu1473","lsu3733","msu9047","pacific811","rpi2012","ric	e3927","rc5074","SLU4049","STAN9568","towson","tu5651","uofl4018","unk6173","ucla6023","udy6632","unco931","ou7888","penn6389","usi4420","utm3428","uva2571","uwm8101","uci3299","wu515","xu9207","yu3914"};

	int schoolNum;
	private int last=-1;
@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.radiolist);
		ArrayList<String> listItems = new ArrayList<String>();
		listItems.addAll(Arrays.asList(schools));
		
		ArrayAdapter<String> ar = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, listItems);
		setListAdapter(ar);
		
		ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setTextFilterEnabled(true);
		
		TextView heading = (TextView) findViewById(R.id.listHeading);
		heading.setText("SELECT SCHOOL");
		
		
		
		SharedPreferences sp = getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
	    schoolNum = sp.getInt("mySchool", -1);
	    if(schoolNum!=-1){
	    	lv.setItemChecked(schoolNum, true);
	    	last = schoolNum;
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
		
		Button b = (Button) findViewById(R.id.savePrefBtn);
		Typeface sego = Typeface.createFromAsset(getAssets(), "sego.ttf");
		b.setTypeface(sego);
		b.setOnClickListener(new View.OnClickListener() {
			
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				
				if(last!=-1){
				Intent i = new Intent(SchoolActivity.this, SetPrefsActivity.class);
			    SharedPreferences sp = getSharedPreferences("schoolPref", MODE_WORLD_READABLE);
			    Editor spEdit = sp.edit();
			    spEdit.putInt("mySchool", last);
			    spEdit.putString("schoolName", schools[last]);
			    spEdit.putString("code", codes[last]);
			    spEdit.commit();
				startActivity(i);
				}
				
			}
		});
	}
	

}
