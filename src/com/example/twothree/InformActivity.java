package com.example.twothree;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class InformActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inform);
		
		Intent myintent = getIntent();
		String nameStr = myintent.getExtras().getString("name");
		String manStr = myintent.getExtras().getString("man");
		String womanStr = myintent.getExtras().getString("woman");
		String placeStr = myintent.getExtras().getString("place");
		
		((TextView)findViewById(R.id.textGrName)).setText(nameStr);
		((TextView)findViewById(R.id.textMan)).setText(manStr);
		((TextView)findViewById(R.id.textWoman)).setText(womanStr);
		((TextView)findViewById(R.id.textPlace)).setText(placeStr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inform, menu);
		return true;
	}

}
