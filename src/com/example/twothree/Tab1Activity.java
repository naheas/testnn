package com.example.twothree;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

public class Tab1Activity extends Activity {

	private ListView mListView = null;
	// Data�� �������ִ� Adapter
	private CustomAdapter mCustomAdapter = null;
	// ���׸�(String)�� ����� ArrayList
	private ArrayList<String> mArrayList = new ArrayList<String>();

	int location;	
	int manNum = 0;
	int womanNum = 0;

	String grid;
	String memTbName;
	
	SQLiteDatabase database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab1);
		
		Intent myintent = getIntent();
		location = myintent.getExtras().getInt("position");
		
		database = openOrCreateDatabase("mtgrsdb", MODE_PRIVATE, null);
		
		initManWomanNumTbName();

		createifnotexistMemberTable();


		initList();
		
		mListView = (ListView) findViewById(R.id.listView1);
		mCustomAdapter = new CustomAdapter(Tab1Activity.this, mArrayList);
		mListView.setAdapter(mCustomAdapter);
		mListView.setOnItemClickListener(new mItemClickListener());
		mListView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
	}

	protected void onDestroy() {
		database.close();
		super.onDestroy();
	}

	// ���� ���� ����ŭ �߰�
	private void initManWomanNumTbName() {
		grid = getGrId();
		memTbName = "mtmemstb" + grid;
		
		String manStr = getDataGr(location, 1);
		if (isStringInt(manStr)) {
			manNum = Integer.parseInt(manStr);
		}
		String womanStr = getDataGr(location, 2);
		if (isStringInt(womanStr)) {
			womanNum = Integer.parseInt(womanStr);
		}	
	}
	
	private void initList() {
        String sql = "select name, moneybool from " + memTbName;
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			mArrayList.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
	}

	public void addMember(View v) {
		AlertDialog alertDlg = new AlertDialog.Builder(Tab1Activity.this)
				.create();
		alertDlg.setTitle("Add New Member");
		alertDlg.setMessage("���ο� �ɹ��� ������?");

		alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "����",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("���ο� ���� �̸� ", false);
						mCustomAdapter.add("���ο� ���� �̸� ");
						mCustomAdapter.notifyDataSetChanged();
						manNum++;
						return;
					}
				});

		alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "����",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("���ο� ���� �̸� ", false);
						mCustomAdapter.add("���ο� ���� �̸� ");
						mCustomAdapter.notifyDataSetChanged();
						womanNum++;
						return;
					}
				});

		alertDlg.show();
	}

	// ListView �ȿ� Item�� Ŭ���ÿ� ȣ��Ǵ� Listener
	private class mItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mCustomAdapter.setChecked(position);
			// Data ����� ȣ�� Adapter�� Data ���� ����� �˷��༭ Update ��.
			mCustomAdapter.notifyDataSetChanged();

		}
	}
	
	private class ListViewItemLongClickListener implements OnItemLongClickListener
	   {
	       @Override
	       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
	       {
	           final String selectedStr = getDataMem(position, 0);
	           AlertDialog alertDlg = new AlertDialog.Builder(Tab1Activity.this).create();
	           alertDlg.setTitle("Delete?");
	           alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
	           // '��' ��ư�� Ŭ���Ǹ�
	           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
	           {
	                @Override
	                public void onClick( DialogInterface dialog, int which ) 
	                {

	                    deleteDataMem(position);
	                	mCustomAdapter.remove(position);
	                    mCustomAdapter.notifyDataSetChanged();       
	                    
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

	// Custom Adapter
	class CustomAdapter extends BaseAdapter {

		private ViewHolder viewHolder = null;
		// �並 ���� ����� ���� Inflater
		private LayoutInflater inflater = null;
		private ArrayList<String> sArrayList = new ArrayList<String>();
		private ArrayList<Boolean> isCheckedConfrim = new ArrayList<Boolean>();;

		public CustomAdapter(Context c, ArrayList<String> mList) {
			inflater = LayoutInflater.from(c);
			this.sArrayList = mList;
			// ArrayList Size ��ŭ�� boolean �迭�� �����.
			// CheckBox�� true/false�� ���� �ϱ� ����
			for (int i = 0; i < sArrayList.size(); i++) {
				this.isCheckedConfrim.add(false);
			}
		}

		public void add(String tempStr) {
			sArrayList.add(tempStr);
			isCheckedConfrim.add(false);
		}
		
		public void remove(int position) {
			sArrayList.remove(position);
			isCheckedConfrim.remove(position);
		}

		public void setChecked(int position) {
			boolean tempBool = !isCheckedConfrim.get(position);
			isCheckedConfrim.set(position, tempBool); // isCheckedConfrim[position]
														// = ischeked; üũ�ϱ�
		}

		public ArrayList<Integer> getChecked() {
			int tempSize = isCheckedConfrim.size();
			ArrayList<Integer> mArrayList = new ArrayList<Integer>();
			for (int b = 0; b < tempSize; b++) {
				if (isCheckedConfrim.get(b)) {
					mArrayList.add(b);
				}
			}
			return mArrayList;
		}

		@Override
		public int getCount() {
			return sArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// ConvertView�� null �� ���
			View v = convertView;

			if (v == null) {
				viewHolder = new ViewHolder();
				// View�� inflater �����ش�.
				v = inflater.inflate(R.layout.row, null);
				viewHolder.cBox = (CheckBox) v.findViewById(R.id.checkBox1);
				v.setTag(viewHolder);
			}

			else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// CheckBox�� �⺻������ �̺�Ʈ�� ������ �ֱ� ������ ListView�� ������
			// Ŭ������ʸ� ����ϱ� ���ؼ��� CheckBox�� �̺�Ʈ�� ���� �־�� �Ѵ�.
			viewHolder.cBox.setClickable(false);
			viewHolder.cBox.setFocusable(false);

			viewHolder.cBox.setText(sArrayList.get(position));
			// isCheckedConfrim �迭�� �ʱ�ȭ�� ��� false�� �ʱ�ȭ �Ǳ⶧����
			// �⺻������ false�� �ʱ�ȭ ��ų �� �ִ�.
			viewHolder.cBox.setChecked(isCheckedConfrim.get(position));

			return v;
		}
	}

	class ViewHolder {
		// ���ο� Row�� �� CheckBox
		private CheckBox cBox = null;
	}

	private void createifnotexistMemberTable() { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.
		Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		cursor.moveToFirst();
		for(;;){
			if(cursor.getString(0).equalsIgnoreCase(memTbName)){
				break;
			}
			if (!cursor.moveToNext()) {
				String sql = "create table if not exists " + memTbName + "(_memid INTEGER PRIMARY KEY AUTOINCREMENT, name text, moneybool integer)";
				database.execSQL(sql);

				for (int i = 0; i < womanNum; i++) {
					insertDataMem("���� �̸� " + i, false);
				}
				for (int i = 0; i < manNum; i++) {
					insertDataMem("���� �̸� " + i, false);
				}
				break;
			}
		}
		cursor.close();
	}

	

	private void insertDataMem(String name, boolean checkbool) {
		int checkNum;
		if(checkbool) checkNum = 1;
		else checkNum = 0;
		
		String sql = "insert into " + memTbName + "(name, moneybool) values('"
				+ name + "', '" + checkNum + "')";
		// ����������, ������ ������ �����ϴ�.
		database.execSQL(sql);
	}
	
	private void deleteDataMem(int location) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select _memid from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		int tempn = cursor.getInt(0);
		cursor.close();
		database.execSQL("delete from " + memTbName + " where _memid = '" + tempn + "'");
	}

	
	private String getDataMem(int location, int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select name, moneybool from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}

	private String getDataGr(int location, int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select name, man, woman, place from mtgrstb";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}
	
	private String getGrId() {
		String sql = "select _grid from mtgrstb";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		return tempStr;
	}

	public static boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
