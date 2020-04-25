package com.vinciis.beTraDict;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Page extends AppCompatActivity {
    public static int Splash_Time_Out=3500;
    VideoView vv;

    String versionName = "";
    int versionCode = -1;
    String currVersion,url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        vv=findViewById(R.id.videoView);
        vv.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hacked);
        vv.start();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        FirebaseDatabase.getInstance().getReference().child("versionName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currVersion=dataSnapshot.child("code").getValue().toString();
                url=dataSnapshot.child("uri").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser!=null)
                {
                    if(currVersion==null)
                    {

                        startActivity(new Intent(Home_Page.this, login_act.class));
                    }
                            else if(currVersion.equals(versionName)) {
                                startActivity(new Intent(Home_Page.this, trans_activity.class));
                            }
                            else {
                                startActivity(new Intent(Home_Page.this,checkLatestVersion.class));
                            }




                }
                else {
                    startActivity(new Intent(Home_Page.this, login_act.class));
                }
                finish();
            }
        },Splash_Time_Out);
    }
}
