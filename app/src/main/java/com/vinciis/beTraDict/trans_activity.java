package com.vinciis.beTraDict;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class trans_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView tvU;
    ImageView btn,nav;
    FirebaseFunctions mFunction;
    NavigationView navigationView;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_activity);
        btn=findViewById(R.id.btnnn);
        mDrawer = findViewById(R.id.trans_background);
        // mToggle = new ActionBarDrawerToggle(trans_activity.this, mDrawer, R.string.open, R.string.close);
        //mDrawer.addDrawerListener(m*Toggle);
      navigationView = findViewById(R.id.nav_view4);
        navigationView.setItemIconTintList(null);
      mUser=FirebaseAuth.getInstance().getCurrentUser();
        // navigationView.setItemIconTintList(null);
        // View header=navigationView.getHeaderView(0);
        // tvU=header.findViewById(R.id.tvUName);
        //  tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        //  navigationView.setNavigationItemSelectedListener(trans_activity.this);
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //   mToggle.syncState();
        mFunction = FirebaseFunctions.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final File file = new File(trans_activity.this.getFilesDir(),mUser.getUid()+".png");
        if(file.exists())
        {
            btn.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

        }
        else {
            new DownLoadImageTask(btn).execute(mUser.getPhotoUrl().toString());

        }

        FragmentManager fm = getSupportFragmentManager();
        cricketMList fragment = new cricketMList();
        fm.beginTransaction().replace(R.id.trans, fragment).commit();


        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        fragment_pools fragmentPools=new fragment_pools();
        Bundle b=new Bundle();
        String array[]={"cricket","icc_wc_2019_g35","normal"};
        b.putStringArray("details",array);
        fragmentPools.setArguments(b);
        fragmentPools.setArguments(b);
        ft.replace(R.id.tranFragPool,fragmentPools).commit();

        nav=findViewById(R.id.navimg);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);
                View header=navigationView.getHeaderView(0);
                tvU=header.findViewById(R.id.tvUName);
                tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                navigationView.setNavigationItemSelectedListener(trans_activity.this);
            }
        });
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
            String filename = mUser.getPhotoUrl().toString()+"+.png";
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
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(trans_activity.this);
            builder.setMessage("Confirm to Exit");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Close!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            getSupportFragmentManager().popBackStack();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            fragment_pools fragmentPools=new fragment_pools();
            Bundle b=new Bundle();
            String array[]={"cricket","icc_wc_2019_g35","normal"};
            b.putStringArray("details",array);
            fragmentPools.setArguments(b);
            fragmentPools.setArguments(b);

            ft.replace(R.id.tranFragPool,fragmentPools).commit();

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
        }

        if(id==R.id.prevml)
        {
            startActivity(new Intent(this, trans_prevmatchList.class));
        }
        if(id==R.id.support)
        {
            startActivity(new Intent(this, transSupport.class));
        }



        return false;
    }

}
