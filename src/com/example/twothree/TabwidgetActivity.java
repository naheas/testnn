package com.example.twothree;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Window;
import android.widget.TabHost;

public class TabwidgetActivity extends TabActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tabwidget);
        
        Resources res = getResources();  //리소스 객체 생성
        TabHost tabHost = getTabHost();  //TabHost 객체 생성
        TabHost.TabSpec spec;    //TabHost.TabSpec 선언
        Intent intent;      //Intent 선언
        
        intent = new Intent(this, Tab1Activity.class);
        spec = tabHost.newTabSpec("tab1")
             .setIndicator("인원 체크")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab2Activity.class);
        spec = tabHost.newTabSpec("tab2")
             .setIndicator("식량 예측")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab3Activity.class);
        spec = tabHost.newTabSpec("tab3")
             .setIndicator("알콜 예측")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab4Activity.class);
        spec = tabHost.newTabSpec("tab4")
             .setIndicator("가계부", res.getDrawable(android.R.drawable.sym_action_call))
             .setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0); //초기 선택 탭 설정
    }
}
