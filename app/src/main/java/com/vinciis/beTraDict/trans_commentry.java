package com.vinciis.beTraDict;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class trans_commentry extends AppCompatActivity {
String arr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_commentry);
        arr=getIntent().getStringArrayExtra("details");

        FragmentManager fm=getSupportFragmentManager();
        commentryfrag fragment=new commentryfrag();
        Bundle b=new Bundle();
        b.putStringArray("data",arr);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.trans_commentry,fragment).commit();
    }
}
