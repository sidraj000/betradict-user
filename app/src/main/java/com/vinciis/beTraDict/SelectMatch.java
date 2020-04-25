package com.vinciis.beTraDict;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectMatch extends AppCompatActivity {

    Button b1,b2;
    String arr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_match);
        b1=(Button)findViewById(R.id.get_stadium_stat);
        b2=(Button)findViewById(R.id.get_match_stats);
        arr=getIntent().getStringArrayExtra("details");


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStadiumStat(v);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStats(v);
            }
        });



    }



    public void getStats(View view) {
        Bundle b=new Bundle();
        Intent intent=new Intent(this,Extra2.class);
        b.putString("team1",arr[0]);
        b.putString("team2",arr[1]);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void getStadiumStat(View view) {

        Intent intent=new Intent(this,Extra.class);
        intent.putExtra("details",arr);
        startActivity(intent);
    }


}
