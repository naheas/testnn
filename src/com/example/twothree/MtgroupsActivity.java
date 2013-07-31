package com.example.twothree;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MtgroupsActivity extends Activity {
	private static final int NewGr_ACTIVITY = 1;

	private ArrayAdapter<String> _arrAdapter ;

	SQLiteDatabase database;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtgroups);

		database = openOrCreateDatabase("mtgrsdb", MODE_PRIVATE, null);
		createifnotexistTable(); //���̺� ���� �޼ҵ� �ҷ�����
		settingListView();
	}

	protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

	private void settingListView() {  
        _arrAdapter = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_list_item_1 ) ;  
        ListView listView = (ListView) findViewById( R.id.listView1 ) ;  
        listView.setAdapter( _arrAdapter ) ;
        
        String sql = "select name, man, woman, place from mtgrstb";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			refresh(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
        
        listView.setOnItemClickListener( new ListViewItemClickListener() );
        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
    }

	private class ListViewItemClickListener implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
/*        	Intent myintent = new Intent(MtgroupsActivity.this, InformActivity.class);
        	myintent.putExtra("name", getData(position, 0));
    	    myintent.putExtra("man", getData(position, 1));
    	    myintent.putExtra("woman", getData(position, 2));
    	    myintent.putExtra("place", getData(position, 3));
    	    startActivity(myintent);
 */
        	Intent myintent = new Intent(MtgroupsActivity.this, TabwidgetActivity.class);
        	myintent.putExtra("position", position);
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
    		
    		insertData(nameStr, manStr, womanStr, placeStr);
    		refresh(nameStr);
        }
    }


	private void refresh( String inputValue ) {  
        _arrAdapter.add( inputValue ) ;  
        _arrAdapter.notifyDataSetChanged() ;  
    }



    
   /*
    * ListView�� item�� ��� Ŭ������ ���.
    * Ŭ���� item�� �����Ѵ�.
    * @author stargatex
    */
   private class ListViewItemLongClickListener implements OnItemLongClickListener
   {
       @Override
       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
       {
           final String selectedStr = _arrAdapter.getItem(position);
           AlertDialog alertDlg = new AlertDialog.Builder(MtgroupsActivity.this).create();
           alertDlg.setTitle("Delete?");
           alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
           // '��' ��ư�� Ŭ���Ǹ�
           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) 
                {
                	deleteMemTable(position);
                	deleteData(position);
               	 	_arrAdapter.remove(selectedStr);
                    // �Ʒ� method�� ȣ������ ���� ���, ������ item�� ȭ�鿡 ��� ��������.
                    _arrAdapter.notifyDataSetChanged();       
                    
                	return;
                }
           });
           
           // '�ƴϿ�' ��ư�� Ŭ���Ǹ�
           alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                	return;
                }
           });
           
           
           alertDlg.show();
           return true;
       }
    
   }
   

	private void createifnotexistTable() { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.
		String sql = "create table if not exists mtgrstb(_grid INTEGER PRIMARY KEY AUTOINCREMENT, name text, man text, woman text, place text)";
		// ���� ������ ������ �����ϴ�.
		database.execSQL(sql);
	}

	private void deleteMemTable(int location) { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.		
		String sql = "select _grid from mtgrstb";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		database.execSQL("drop table mtmemstb" + tempStr);
	}

	private void insertData(String name, String man, String woman, String place) {// ������
																					// ����
																					// �޼ҵ�
		/*
		 * /������ �����͸� ������ִ� ������ �����մϴ�. �ռ� ��ư���ǿ��� ���̰�����, ������ ���� �޼��忡 �־��ذ̴ϴ�. /
		 */
		String sql = "insert into mtgrstb(name, man, woman, place) values('"
				+ name + "', '" + man + "', '" + woman + "', '" + place + "')";
		// ����������, ������ ������ �����ϴ�.
		database.execSQL(sql);
	}
	
	private void deleteData(int location) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select _grid from mtgrstb";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		int tempn = cursor.getInt(0);
		cursor.close();
		database.execSQL("delete from mtgrstb where _grid = '" + tempn + "'");
	}

	private String getData(int location, int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select name, man, woman, place from mtgrstb";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}


}