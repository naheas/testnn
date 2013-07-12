package com.example.twothree;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class NewgroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newgroup);
	}
	
	public void newGroup (View v) {

		String nameStr = ((EditText)findViewById(R.id.editGrName)).getText().toString();
		String manStr = ((EditText)findViewById(R.id.editMan)).getText().toString();
		String womanStr = ((EditText)findViewById(R.id.editWoman)).getText().toString();
		String placeStr = ((EditText)findViewById(R.id.editPlace)).getText().toString();

		Intent reintent = new Intent();
	
		reintent.putExtra("name", nameStr);
	    reintent.putExtra("man", manStr);
	    reintent.putExtra("woman", womanStr);
	    reintent.putExtra("place", placeStr);
	    setResult(RESULT_OK, reintent);
	    
		Intent myintent = new Intent(this, InformActivity.class);
	    myintent.putExtra("name", nameStr);
	    myintent.putExtra("man", manStr);
	    myintent.putExtra("woman", womanStr);
	    myintent.putExtra("place", placeStr);
	    startActivity(myintent);

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newgroup, menu);
		return true;
	}

}
