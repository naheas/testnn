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
        
        Resources res = getResources();  //���ҽ� ��ü ����
        TabHost tabHost = getTabHost();  //TabHost ��ü ����
        TabHost.TabSpec spec;    //TabHost.TabSpec ����
        Intent intent;      //Intent ����
        
        intent = new Intent(this, Tab1Activity.class);
        spec = tabHost.newTabSpec("tab1")
             .setIndicator("�ο� üũ")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab2Activity.class);
        spec = tabHost.newTabSpec("tab2")
             .setIndicator("�ķ� ����")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab3Activity.class);
        spec = tabHost.newTabSpec("tab3")
             .setIndicator("���� ����")
             .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(this, Tab4Activity.class);
        spec = tabHost.newTabSpec("tab4")
             .setIndicator("�����", res.getDrawable(android.R.drawable.sym_action_call))
             .setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0); //�ʱ� ���� �� ����
    }
}
