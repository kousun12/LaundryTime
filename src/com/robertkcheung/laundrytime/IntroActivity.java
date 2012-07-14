package com.robertkcheung.laundrytime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.first);
		
		Button b = (Button) findViewById(R.id.setPrefbtn);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(IntroActivity.this, SetPrefsActivity.class));
			}
		});
		
	}
}
