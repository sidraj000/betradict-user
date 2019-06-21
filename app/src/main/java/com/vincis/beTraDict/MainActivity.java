package com.vincis.beTraDict;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView one,two,three,four,five;
    ImageView btnToggle;
    private DrawerLayout mDrawer;

    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;
    public Bundle arr;
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView tvU;
    NavigationView navigationView;
    ImageView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btnToggle=findViewById(R.id.btnToggle);
        btn=findViewById(R.id.btnnnn);
        mDrawer = findViewById(R.id.drawer);
     // mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.open, R.string.close);
        //mDrawer.addDrawerListener(mToggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
     //   View header=navigationView.getHeaderView(0);
      //  navigationView.setItemIconTintList(null);
        arr =getIntent().getExtras();
        final String[] data=arr.getStringArray("details");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("betradict")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });
        if(data[2].equals("normal"))
        {
            btnToggle.setImageResource(R.drawable.in);
        }
        else {
            btnToggle.setImageResource(R.drawable.back);
        }
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data[2].equals("normal"))
                {
                    //  Toast.makeText(MainActivity.this, "hii", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    data[2]="live";
                    intent.putExtra("details",data);
                    startActivity(intent);
                    finish();
                }
                else if(data[2].equals("live"))
                {
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    data[2]="normal";
                    intent.putExtra("details",data);
                    startActivity(intent);
                    finish();
                }

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);
                View header=navigationView.getHeaderView(0);
                tvU=header.findViewById(R.id.tvUName);
                tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
        });




        //mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //one=findViewById(R.id.one);
        //two=findViewById(R.id.two);
        //three=findViewById(R.id.three);
        // five=findViewById(R.id.five);
        /*one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        viewPager=findViewById(R.id.container);
        pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager(),arr);
        viewPager.setAdapter(pagerViewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                onChangeTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
*/

        FragmentManager fm=getSupportFragmentManager();
        frag4 fragment=new frag4();

        fragment.setArguments(arr);
        fm.beginTransaction().replace(R.id.transmanin,fragment).commit();


    }
    private  class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlOfImage = strings[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);

            }
            catch(IOException e)
            {
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            String filename = user.getPhotoUrl().toString()+"+.png";
            FileInputStream fileInputStream;

            try {
                fileInputStream = openFileInput(filename);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }


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
                menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this,(int)wallet.balance,R.drawable.wallet));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
             //   startActivity(new Intent(MainActivity.this,wallet_trans.class));
                return false;
            }
        });

        return true;
    }

    private void onChangeTab(int i) {
        if(i==0)
        {
            one.setImageResource(R.mipmap.quiz);
            two.setImageResource(R.mipmap.ans);
            three.setImageResource(R.mipmap.analytics);

            viewPager.setCurrentItem(0);

        }

        if(i==1)
        {
            one.setImageResource(R.mipmap.question);
            two.setImageResource(R.mipmap.ansc);
            three.setImageResource(R.mipmap.analytics);
            viewPager.setCurrentItem(1);
        }
        if(i==2)
        {
            one.setImageResource(R.mipmap.question);
            two.setImageResource(R.mipmap.ans);
            three.setImageResource(R.mipmap.analyticsc);

            viewPager.setCurrentItem(2);

        }


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
        if(id==R.id.leader_board)
        {
            startActivity(new Intent(this, trans_leader.class));
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

}
