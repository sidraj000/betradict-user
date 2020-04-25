package com.vinciis.beTraDict;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class trans_leader extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView btn;
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    public String arr[];
TextView tvU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_leader);
        mDrawer = findViewById(R.id.d2);
        mToggle = new ActionBarDrawerToggle(trans_leader.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view12);
        navigationView.setNavigationItemSelectedListener(trans_leader.this);
        navigationView.setItemIconTintList(null);
        View header=navigationView.getHeaderView(0);
        tvU=header.findViewById(R.id.tvUName);
        tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
      //  mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arr=getIntent().getStringArrayExtra("details");
        FragmentManager fm=getSupportFragmentManager();
        LeaderBoard fragment=new LeaderBoard();
        Bundle b=new Bundle();
        b.putStringArray("details",arr);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.trans_l,fragment).commit();
        btn=findViewById(R.id.btnRef);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=(new Intent(trans_leader.this,trans_leader.class));
               intent.putExtra("details",arr);
               startActivity(intent );
               finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.home)
        {
            startActivity(new Intent(this,trans_activity.class));
            finish();
        }

        if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, login_act.class));
            finish();
        }
        if(id==R.id.wallet)
        {
            startActivity(new Intent(this,wallet_trans.class));
            finish();
        }

        if(id==R.id.prevml)
        {
            startActivity(new Intent(this, trans_prevmatchList.class));
            finish();
        }
        if(id==R.id.support)
        {
            startActivity(new Intent(this, transSupport.class));
            finish();
        }


        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart_action);
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                menuItem.setIcon(Converter.convertLayoutToImage(trans_leader.this,(int)wallet.balance,R.drawable.wallet));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(trans_leader.this,wallet_trans.class));
                return false;
            }
        });

        return true;
    }


}

