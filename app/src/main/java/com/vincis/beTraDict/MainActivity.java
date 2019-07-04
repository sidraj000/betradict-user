package com.vincis.beTraDict;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView one,two,three,four,five;
   // ImageView btnToggle;
    private DrawerLayout mDrawer;
    JSONArray lastOver;
    Button btnRun;
    ImageView btnRef;
    String data;
    ImageView iv1,iv2;
    TextView tvscore1,lastball;
    RecyclerView mRecyclerBall;
    private ballAdapter mAdapter2;
    public String accesstoken;
    ProgressBar progressBar;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;
    public Bundle arr;
    String[] arrdata;
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
       // btnToggle=findViewById(R.id.btnToggle);
        btn=findViewById(R.id.btnnnn);
        mDrawer = findViewById(R.id.drawer);
        iv1=findViewById(R.id.ivTeama);
        iv2=findViewById(R.id.ivTeamb);
        tvscore1=findViewById(R.id.score1);
        lastball=findViewById(R.id.lastball);
        mRecyclerBall=findViewById(R.id.mRecyclerBall);
        lastball=findViewById(R.id.lastball);
        progressBar=findViewById(R.id.progressBar2);
        btnRef=findViewById(R.id.btnRefScore);
        mRecyclerBall.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
     // mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.open, R.string.close);
        //mDrawer.addDrawerListener(mToggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
     //   View header=navigationView.getHeaderView(0);
      //  navigationView.setItemIconTintList(null);
        arr =getIntent().getExtras();
        arrdata=arr.getStringArray("details");
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
      /*  if(arrdata[2].equals("normal"))
        {
            btnToggle.setImageResource(R.drawable.in);
        }
        else {
            btnToggle.setImageResource(R.drawable.back);
        }
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrdata[2].equals("normal"))
                {
                    //  Toast.makeText(MainActivity.this, "hii", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    arrdata[2]="live";
                    intent.putExtra("details",arrdata);
                    startActivity(intent);
                    finish();
                }
                else if(arrdata[2].equals("live"))
                {
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    arrdata[2]="normal";
                    intent.putExtra("details",arrdata);
                    startActivity(intent);
                    finish();
                }

            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);
                View header=navigationView.getHeaderView(0);
                tvU=header.findViewById(R.id.tvUName);
                tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
        });
        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticateTask().execute();
                new RetrieveFeedTask().execute();
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

if(arrdata[4].equals("c"))
{
    new AuthenticateTask().execute();
    new RetrieveFeedTask().execute();
    FragmentManager fm = getSupportFragmentManager();
    frag4 fragment = new frag4();
    fragment.setArguments(arr);
    fm.beginTransaction().replace(R.id.transmanin, fragment).commit();
}
        new AuthenticateTask().execute();
        new RetrieveFeedTask().execute();


    }

    public class AuthenticateTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url3 = new URL("https://rest.cricketapi.com/rest/v2/auth/?access_key=35faa5e182c8262090cdae7cf7be6f8b&secret_key=a7d00314119875e8a8d22d45cd7818ef&app_id=com.vincis.betradict.admin&device_id="+user.getUid());
                HttpURLConnection urlConnection3 = (HttpURLConnection) url3.openConnection();

                try {
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));
                    StringBuilder stringBuilder3 = new StringBuilder();
                    String line;
                    while ((line = bufferedReader3.readLine()) != null) {
                        stringBuilder3.append(line).append("\n");
                    }
                    bufferedReader3.close();
                    String pass3=stringBuilder3.toString();

                    return pass3;
                }
                finally{
                    urlConnection3.disconnect();
                }
            }
            catch(Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                accesstoken=new JSONObject(s).getJSONObject("auth").getString("access_token");
                atoken token=new atoken(accesstoken);
                FirebaseDatabase.getInstance().getReference().child("authid").child(user.getUid()).setValue(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    public class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {

            try {

                URL url = new URL("https://rest.cricketapi.com/rest/v2/match/"+arrdata[1]+"/?access_token="+accesstoken);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    String pass=stringBuilder.toString();


                    return pass;

                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
            //  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);

            if (response != null) {
                data=response;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jsonObj2=jsonObj.getJSONObject("data");
                    JSONObject jsonObject3=jsonObj2.getJSONObject("card");

                    String status=jsonObject3.getString("status");

                    if(status.equals("notstarted"))
                    {
                        tvscore1.setText("Starts soon");
                        String team1 = jsonObject3.getJSONObject("teams").getJSONObject("a").getString("short_name");
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        String team2 = jsonObject3.getJSONObject("teams").getJSONObject("b").getString("short_name");
                        final File file = new File(MainActivity.this.getFilesDir(),team1+".png");
                        if(file.exists())
                        {
                            iv1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                        }
                        else {

                            storageRef.child("cricketTeamLogo/" + team1+ ".png").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    iv1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });


                        }
                        final File file2 = new File(MainActivity.this.getFilesDir(),team2+".png");
                        if(file2.exists())
                        {
                            iv2.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));


                        }
                        else {

                            storageRef.child("cricketTeamLogo/" + team2+ ".png").getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    iv2.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                }
                            });


                        }
                    }
                    else {
                        JSONObject tossobject = jsonObject3.getJSONObject("toss");
                        if (tossobject.has("str")) {
                            String tossDetails = tossobject.getString("str");
                        }

                        JSONArray playing11a = jsonObject3.getJSONObject("teams").getJSONObject("a").getJSONObject("match").getJSONArray("playing_xi");
                        JSONArray playing11b = jsonObject3.getJSONObject("teams").getJSONObject("b").getJSONObject("match").getJSONArray("playing_xi");
                        JSONArray batorder = jsonObject3.getJSONArray("batting_order");

                        final String batsecond;

                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        final String batfirst = batorder.getJSONArray(0).getString(0);
                        String score = jsonObject3.getJSONObject("innings").getJSONObject(batfirst + "_1").getString("run_str");
                        String team1 = jsonObject3.getJSONObject("teams").getJSONObject(batfirst).getString("short_name");

                        if(batfirst.equals("a"))
                        {
                            batsecond="b";
                        }
                        else {
                            batsecond="a";
                        }
                        String team2 = jsonObject3.getJSONObject("teams").getJSONObject(batsecond).getString("short_name");
                        final File file = new File(MainActivity.this.getFilesDir(),team1+".png");
                        if(file.exists())
                        {
                            iv1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                        }
                        else {

                            storageRef.child("cricketTeamLogo/" + team1+ ".png").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    iv1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });


                        }
                        final File file2 = new File(MainActivity.this.getFilesDir(),team2+".png");
                        if(file2.exists())
                        {
                            iv2.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));


                        }
                        else {

                            storageRef.child("cricketTeamLogo/" + team2+ ".png").getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    iv2.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                }
                            });


                        }
                        iv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(MainActivity.this, trans_teama_data.class);
                                String arr[]={data,batfirst};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        iv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(MainActivity.this, trans_teama_data.class);
                                String arr[]={data,batsecond};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        tvscore1.setText(team1+ ": " + score);
                        if (batorder.length() == 2)
                        {
                            String score2 = jsonObject3.getJSONObject("innings").getJSONObject(batsecond + "_1").getString("run_str");
                            tvscore1.setText(team2 + ":" + score2);
                        } else {

                        }




                        lastOver=jsonObject3.getJSONObject("now").getJSONArray("recent_overs_str").getJSONArray(0).getJSONArray(1);;
                        final int t=playing11a.length();
                        mAdapter2=new ballAdapter(MainActivity.this);
                        mRecyclerBall.setAdapter(mAdapter2);
                        lastball.setText(Html.fromHtml(jsonObject3.getJSONObject("now").getJSONObject("last_ball").getString("comment")));
                        lastball.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(MainActivity.this,trans_commentry.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        tvscore1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(MainActivity.this,trans_scoreCard.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });

                    }
                } catch (JSONException e) {
                  //  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {}
        }
    }
    private class ballViewHolder extends RecyclerView.ViewHolder {
        TextView tvBall;
        public ballViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBall=itemView.findViewById(R.id.tvBalls);
        }
    }
    private class ballAdapter extends RecyclerView.Adapter<ballViewHolder>
    {
        private Context mContext;
        public ballAdapter(final Context context) {
            mContext=context;
        }

        @NonNull
        @Override
        public ballViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.balls, viewGroup, false);

            return new ballViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ballViewHolder ballViewHolder, int i) {
            try {
                ballViewHolder.tvBall.setText(lastOver.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            return lastOver.length();
        }
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

}
