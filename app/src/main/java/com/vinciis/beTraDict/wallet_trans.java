package com.vinciis.beTraDict;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class wallet_trans extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    TextView tvU;
    ImageView ivWallet;
    ImageView ivVips;
    TextView tabRefer;
    FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_trans);
        FragmentManager fm=getSupportFragmentManager();
        mDrawer = findViewById(R.id.d1);
        mToggle = new ActionBarDrawerToggle(wallet_trans.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view11);
        navigationView.setNavigationItemSelectedListener(wallet_trans.this);
        View header=navigationView.getHeaderView(0);
        tvU=header.findViewById(R.id.tvUName);
        tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        navigationView.setItemIconTintList(null);



        ivWallet=findViewById(R.id.tabWWallet);
         tabRefer=findViewById(R.id.tabReferText);
          ivVips=findViewById(R.id.tabVips);

        mRef.child("users").child(uid).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                int balance=(int)wallet.balance;
                ivWallet.setImageDrawable(Converter.convertLayoutToImage(wallet_trans.this,balance,R.mipmap.appl));
             }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final walletdetail fragment=new walletdetail();
        final viipsfragment fragment2=new viipsfragment();
        fm.beginTransaction().replace(R.id.wallet_t,fragment).commit();


        ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm1=getSupportFragmentManager();
                FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();

                ft1.replace(R.id.wallet_t,fragment).commit();
                ft1.addToBackStack(null);
            }
        });
        tabRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");

                waIntent.setPackage("com.whatsapp");
                if (waIntent != null) {
                    FirebaseDatabase.getInstance().getReference().child("versionName").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tex=dataSnapshot.child("uri").getValue().toString();
                            String text = "Download betradict "+tex+" and get 100 trollars free to play by using code "+mUser.getUid().substring(0,6);
                            waIntent.putExtra(Intent.EXTRA_TEXT, text);
                            startActivity(Intent.createChooser(waIntent, "Share with"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(wallet_trans.this,"WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        ivVips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm1=getSupportFragmentManager();
                FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                ft1.addToBackStack(null);
                ft1.replace(R.id.wallet_t,fragment2).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
      startActivity(new Intent(wallet_trans.this,trans_activity.class));
      finish();

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
            startActivity(new Intent(this, trans_activity.class));
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
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                menuItem.setIcon(Converter.convertLayoutToImage(wallet_trans.this,(int)wallet.balance,R.drawable.wallet));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

}
