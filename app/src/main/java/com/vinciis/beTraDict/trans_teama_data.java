package com.vinciis.beTraDict;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class trans_teama_data extends AppCompatActivity {
    String arr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_team1_data);
        FragmentManager fm=getSupportFragmentManager();
        playinng11details fragment=new playinng11details();
        arr=getIntent().getStringArrayExtra("details");
        Bundle b=new Bundle();
        b.putStringArray("data",arr);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.trans_teama_data,fragment).commit();
    }
}
