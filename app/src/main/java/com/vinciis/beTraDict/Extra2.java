package com.vinciis.beTraDict;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class Extra2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra2);
        Bundle b=new Bundle();
        b.putString("team1",getIntent().getExtras().getString("team1"));
        b.putString("team2",getIntent().getExtras().getString("team2"));
        FragmentManager fm=getSupportFragmentManager();
        TeamPreviousMatchFragment tmp= new TeamPreviousMatchFragment();
        tmp.setArguments(b);
        fm.beginTransaction().replace(R.id.activityextra2,tmp).commit();

    }
}
