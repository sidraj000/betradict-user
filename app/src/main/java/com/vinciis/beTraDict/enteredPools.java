package com.vinciis.beTraDict;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class enteredPools extends AppCompatActivity {
    RecyclerView mRecycler;
    FirebaseUser mUser;
    String arr[];
    String url;



    public String accesstoken;
    ProgressBar progressBar;
    ImageView btnRef;
    String data;
    ImageView iv1,iv2;
    JSONArray lastOver;
    TextView tvscore1,lastball;
    RecyclerView mRecyclerBall;
    FirebaseUser user;
    ImageView ivAnime;
    WebView wv;
    public ballAdapter mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entered_pools);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        arr=getIntent().getStringArrayExtra("details");
        mRecycler=findViewById(R.id.rEnteredPools);

        user = FirebaseAuth.getInstance().getCurrentUser();
        iv1=findViewById(R.id.ivTeama);
        iv2=findViewById(R.id.ivTeamb);
        tvscore1=findViewById(R.id.score1);
        lastball=findViewById(R.id.lastball);
        mRecyclerBall=findViewById(R.id.mRecyclerBall);
        lastball=findViewById(R.id.lastball);
        progressBar=findViewById(R.id.progressBar2);
        btnRef=findViewById(R.id.btnRefScore);
        ivAnime=findViewById(R.id.ivanime2);
        wv=findViewById(R.id.webview2);
        Animation animation= AnimationUtils.loadAnimation(enteredPools.this,R.anim.rotate);
        ivAnime.setAnimation(animation);
        ivAnime.setVisibility(View.VISIBLE);
        mRecyclerBall.setLayoutManager(new LinearLayoutManager(enteredPools.this,LinearLayoutManager.HORIZONTAL,false));
        // mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.open, R.string.close);
        //mDrawer.addDrawerListener(mToggle);
        mRecycler.setLayoutManager(new LinearLayoutManager(enteredPools.this));
        mRecycler.setHasFixedSize(true);
        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticateTask().execute();
                new RetrieveFeedTask().execute();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("scoreurl").child(arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url=dataSnapshot.child("url").getValue().toString();
                wv.loadUrl(url);
                MyWebViewClient webViewClient = new MyWebViewClient();
                wv.setWebViewClient(webViewClient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewAdapter adapter=new viewAdapter(enteredPools.this);
        new AuthenticateTask().execute();
        new RetrieveFeedTask().execute();

        mRecycler.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewAdapter adapter=new viewAdapter(enteredPools.this);
        mRecycler.setAdapter(adapter);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView enterdAmt,prizeAmt;
        ImageView ivIcon;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            enterdAmt=itemView.findViewById(R.id.tvEEnterAmt);
            prizeAmt=itemView.findViewById(R.id.tvETotalWinnings);
            ivIcon=itemView.findViewById(R.id.ivEPoolLogo);
        }

    }
    public class viewAdapter extends RecyclerView.Adapter<viewHolder>
    {
        Context mContext;
        List<Pools> mPools=new ArrayList<>();
        List<Levels> mLevels=new ArrayList<>();
        public viewAdapter(Context context) {
            mContext=context;
            FirebaseDatabase.getInstance().getReference().child("match").child(arr[0]).child(arr[1]).child("pools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Levels level=dataSnapshot.getValue(Levels.class);
                    if(level.battingPool.user.indexOf(mUser.getUid())>-1)
                    {
                        mPools.add(level.battingPool);
                        mLevels.add(level);
                        notifyItemInserted(mPools.size()-1);
                    }
                    if(level.bowlingPool.user.indexOf(mUser.getUid())>-1)
                    {
                        mPools.add(level.bowlingPool);
                        mLevels.add(level);
                        notifyItemInserted(mPools.size()-1);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_entered_pools, viewGroup, false);

            return new viewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
            final int k=i;
            if(i==0)
            {
                ivAnime.clearAnimation();
                 ivAnime.setVisibility(View.GONE);
                new AuthenticateTask().execute();
                new RetrieveFeedTask().execute();

            }
            if(mPools.get(i).poolname.equals("battingPool")) {
                viewHolder.ivIcon.setImageResource(R.mipmap.battingpool);
            }
            else {
                viewHolder.ivIcon.setImageResource(R.mipmap.bowlingpool);
            }
           viewHolder.prizeAmt.setText(Double.toString(mPools.get(i).totalWinnings/1000.0)+"K");
            viewHolder.enterdAmt.setText(Integer.toString(mPools.get(i).enteramount));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent=(new Intent(enteredPools.this,MainActivity.class));
                  String arrr[]={arr[0],arr[1],mLevels.get(k).id, mPools.get(k).poolname, "c", "normal"};
                  intent.putExtra("details",arrr);
                  startActivity(intent);
                  finish();

                }
            });

        }

        @Override
        public int getItemCount() {
            return mPools.size();
        }
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

                URL url = new URL("https://rest.cricketapi.com/rest/v2/match/"+arr[1]+"/?access_token="+accesstoken);
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
                        final File file = new File(enteredPools.this.getFilesDir(),team1+".png");
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
                        final File file2 = new File(enteredPools.this.getFilesDir(),team2+".png");
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
                        final File file = new File(enteredPools.this.getFilesDir(),team1+".png");
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
                        final File file2 = new File(enteredPools.this.getFilesDir(),team2+".png");
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
                                Intent intent=new Intent(enteredPools.this, trans_teama_data.class);
                                String arr[]={data,batfirst};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        iv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(enteredPools.this, trans_teama_data.class);
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
                        mAdapter2=new ballAdapter(enteredPools.this);
                        mRecyclerBall.setAdapter(mAdapter2);
                        lastball.setText(Html.fromHtml(jsonObject3.getJSONObject("now").getJSONObject("last_ball").getString("comment")));
                        lastball.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(enteredPools.this,trans_commentry.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        tvscore1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(enteredPools.this,trans_scoreCard.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });

                    }
                } catch (JSONException e) {
                    //  Toast.makeText(enteredPools.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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



}
