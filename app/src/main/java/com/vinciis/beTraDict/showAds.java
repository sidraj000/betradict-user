package com.vinciis.beTraDict;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class showAds extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    Button btnAd;
    Button btnWithdraw;
    public String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);
        btnAd=findViewById(R.id.btnShowAd);
        MobileAds.initialize(this, "ca-app-pub-8509023804393461~4537102158");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        btnAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
                else
                {
                    Toast.makeText(showAds.this, "Ad is loading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd("ca-app-pub-8509023804393461/5339047938", new AdRequest.Builder().build());
        }
    }
    @Override
    public void onRewardedVideoAdLoaded() {
        btnAd.setText("Ad Loaded");
          }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d("opened","opened");
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        btnAd.setText("Ad Loading...");
        loadRewardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet wallet=mutableData.getValue(Wallet.class);

                if(wallet==null)
                {
                    return (Transaction.success(mutableData));
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();
                    wallet.balance=wallet.balance+1;
                    wallet.lastTransactions.add(new transactions(1,"add","add","add",currentDate));
                    mutableData.setValue(wallet);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            }
        });
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, Integer.toString(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }


}

