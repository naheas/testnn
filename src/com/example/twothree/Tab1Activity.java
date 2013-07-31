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
	// Data를 관리해주는 Adapter
	private CustomAdapter mCustomAdapter = null;
	// 제네릭(String)을 사용한 ArrayList
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

	// 남자 여자 수만큼 추가
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
		alertDlg.setMessage("새로운 맴버의 성별은?");

		alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "남자",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("새로운 남자 이름 ", false);
						mCustomAdapter.add("새로운 남자 이름 ");
						mCustomAdapter.notifyDataSetChanged();
						manNum++;
						return;
					}
				});

		alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "여자",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("새로운 여자 이름 ", false);
						mCustomAdapter.add("새로운 여자 이름 ");
						mCustomAdapter.notifyDataSetChanged();
						womanNum++;
						return;
					}
				});

		alertDlg.show();
	}

	// ListView 안에 Item을 클릭시에 호출되는 Listener
	private class mItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mCustomAdapter.setChecked(position);
			// Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
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
	           // '예' 버튼이 클릭되면
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
	           
	           // '아니오' 버튼이 클릭되면
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
		// 뷰를 새로 만들기 위한 Inflater
		private LayoutInflater inflater = null;
		private ArrayList<String> sArrayList = new ArrayList<String>();
		private ArrayList<Boolean> isCheckedConfrim = new ArrayList<Boolean>();;

		public CustomAdapter(Context c, ArrayList<String> mList) {
			inflater = LayoutInflater.from(c);
			this.sArrayList = mList;
			// ArrayList Size 만큼의 boolean 배열을 만든다.
			// CheckBox의 true/false를 구별 하기 위해
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
														// = ischeked; 체크하기
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

			// ConvertView가 null 일 경우
			View v = convertView;

			if (v == null) {
				viewHolder = new ViewHolder();
				// View를 inflater 시켜준다.
				v = inflater.inflate(R.layout.row, null);
				viewHolder.cBox = (CheckBox) v.findViewById(R.id.checkBox1);
				v.setTag(viewHolder);
			}

			else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
			// 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
			viewHolder.cBox.setClickable(false);
			viewHolder.cBox.setFocusable(false);

			viewHolder.cBox.setText(sArrayList.get(position));
			// isCheckedConfrim 배열은 초기화시 모두 false로 초기화 되기때문에
			// 기본적으로 false로 초기화 시킬 수 있다.
			viewHolder.cBox.setChecked(isCheckedConfrim.get(position));

			return v;
		}
	}

	class ViewHolder {
		// 새로운 Row에 들어갈 CheckBox
		private CheckBox cBox = null;
	}

	private void createifnotexistMemberTable() { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.
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
					insertDataMem("여자 이름 " + i, false);
				}
				for (int i = 0; i < manNum; i++) {
					insertDataMem("남자 이름 " + i, false);
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
		// 마찬가지로, 정의한 쿼리를 보냅니다.
		database.execSQL(sql);
	}
	
	private void deleteDataMem(int location) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select _memid from " + memTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		int tempn = cursor.getInt(0);
		cursor.close();
		database.execSQL("delete from " + memTbName + " where _memid = '" + tempn + "'");
	}

	
	private String getDataMem(int location, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select name, moneybool from " + memTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToPosition(location);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}

	private String getDataGr(int location, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select name, man, woman, place from mtgrstb";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
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
