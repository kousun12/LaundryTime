package com.robertkcheung.laundrytime;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SchoolActivity extends ListActivity{
	String [] schools = {"University of Illinois Urbana-Champaign", "University of Northern Colorado", "University of Pennsylvania", "Stanford University", "University of Virginia", "Iowa State University", "Trinity University", "Louisiana State University", "University of Southern Indiana", "Humboldt State University", "Univeristy of Nebraska-Kearney", "University of California-Los Angeles", "Towson Univeristy", "University of Toronto Mississauga", "California Polytechnic State Univeristy", "California State University - Monterery Bay", "Buena Vista University", "University of Wisconsin-Milwaukee", "Lehigh University", "Augsburg College", "University of Oklahoma", "Saint Louis University", "Duke University", "Rensselaer Polytechnic Institute", "Rollins College", "University of Dayton", "Univeristy of Louisville", "Washburn University", "Pacific University", "Fort Hays State University", "Xavier University", "Vista Del Campo Norte", "Missouri State University", "Rice University", "Augsburg College", "Yeshiva University", "Central Michigan University"};
	String [] codes = {"urba7723", "unco931", "penn6389", "STAN9568", "uva2571", "isu7983", "tu5651", "lsu3733", "usi4420", "hsu3034", "unk6173", "ucla6023", "towson", "utm3428", "uhcpslo", "csumb721", "bvu999", "uwm8101", "lu1473", "ac4560", "ou7888", "SLU4049", "du9458", "rpi2012", "rc5074", "udy6632", "uofl4018", "wu515", "pacific811", "fhsu5007", "xu9207", "uci3299", "msu9047", "ric	e3927", "ac4560", "yu3914", "cmu4936"};

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
