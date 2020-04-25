package com.vinciis.beTraDict;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Extra extends AppCompatActivity {

    TextView sVenue,sPitchInfo;
    DatabaseReference mRootRef;
    stadiumAnalytics stadiumInfo;
    String arr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        sVenue=findViewById(R.id.svenue);
        sPitchInfo=findViewById(R.id.spitchinfo);
        arr=getIntent().getStringArrayExtra("details");
        mRootRef= FirebaseDatabase.getInstance().getReference().child("analytics").child("cricket").child("stadium").child(arr[2]);
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stadiumInfo=dataSnapshot.getValue(stadiumAnalytics.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FragmentManager fm=getSupportFragmentManager();
        StadiumStatsFragment stadiumStatsFragment=new StadiumStatsFragment();
        Bundle b=new Bundle();
        b.putString("sid",arr[2]);
        stadiumStatsFragment.setArguments(b);
        fm.beginTransaction().replace(R.id.activityextra,stadiumStatsFragment).commit();
    }
}
