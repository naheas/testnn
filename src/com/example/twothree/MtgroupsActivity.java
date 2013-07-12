package com.example.twothree;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MtgroupsActivity extends Activity {
	private static final int NewGr_ACTIVITY = 1;
	
	private ArrayAdapter<String> _arrAdapter ;
	
	private mtGroup [] listGr = new mtGroup[30];
	
	private int listNum = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtgroups);
		
		settingListView();
	}
	
	private void settingListView() {  
        _arrAdapter = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_list_item_1 ) ;  
        ListView listView = (ListView) findViewById( R.id.listView1 ) ;  
        listView.setAdapter( _arrAdapter ) ;
        
        listView.setOnItemClickListener( new ListViewItemClickListener() );
//        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
    }
	
	private class ListViewItemClickListener implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
        	Intent myintent = new Intent(MtgroupsActivity.this, InformActivity.class);
    	    myintent.putExtra("name", listGr[position].name);
    	    myintent.putExtra("man", listGr[position].man);
    	    myintent.putExtra("woman", listGr[position].woman);
    	    myintent.putExtra("place", listGr[position].place);
    	    startActivity(myintent);
        }        
    }
	
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void go (View v) {

	    Intent myintent = new Intent(this, NewgroupActivity.class);
	    startActivityForResult(myintent, NewGr_ACTIVITY);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {		
        if(resultCode == RESULT_OK && requestCode == NewGr_ACTIVITY) {        	
        	String nameStr = data.getExtras().getString("name");
    		String manStr = data.getExtras().getString("man");
    		String womanStr = data.getExtras().getString("woman");
    		String placeStr = data.getExtras().getString("place");
    		listGr[listNum] = new mtGroup(nameStr, manStr, womanStr, placeStr);
    		listNum++;
    		
    		refresh(nameStr);
        }
    }

	
	private void refresh( String inputValue ) {  
        _arrAdapter.add( inputValue ) ;  
        _arrAdapter.notifyDataSetChanged() ;  
    }
	
	
	
    
   /*
    * ListView의 item을 길게 클릭했을 경우.
    * 클릭된 item을 삭제한다.
    * @author stargatex
    *
   private class ListViewItemLongClickListener implements OnItemLongClickListener
   {
       @Override
       public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
       {
           String selectedStr = listGr[position].name;
           AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
           alertDlg.setTitle(R.string.alert_title_question);

           // '예' 버튼이 클릭되면
           alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) 
                {
               	 _arrAdapter.remove();
                    
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    adapter.notifyDataSetChanged();                     
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
           });
           
           // '아니오' 버튼이 클릭되면
           alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
           });
           
           
           alertDlg.setMessage( String.format( getString(R.string.alert_msg_delete), _arrAdapter.getItem(position)) );
           alertDlg.show();
           return false;
       }
    
   }
   
   */

}
