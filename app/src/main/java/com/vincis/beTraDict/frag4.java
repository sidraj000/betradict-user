package com.vincis.beTraDict;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Integer.parseInt;



/**
 * A simple {@link Fragment} subclass.
 */
public class frag4 extends Fragment {
    ProgressBar progressBar;
    List<String> pla=new ArrayList<>();
    List<String> plb=new ArrayList<>();
    TextView tvmsg,tvtoss,tvscore1,tvscore2,tvResult,tvStatus,det;
   TextView ball1,ball2,ball3,ball4,ball5,ball6,lastball;
    Button refresh;
    String data;
    private RecyclerView mRecycler,mRecyclerBall;
    private FriendAdapter mAdapter;
    private ballAdapter mAdapter2;
    private LinearLayoutManager mManager,mManager2;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Wallet wallet=new Wallet();
    public Usrwal u;
    public Quest_wall quest_wall;
    public Bundle b;
    public String arr[];
    public float rr;
    Animation animation;
    ImageView ivAnime;
    public int size=19;
    Animation animation2;
    float rate1=0,rate2=0,rate3=0;
    public String accesstoken;
    ImageView iv1,iv2,ivCancel;
    TextView tvquest, tvRate1, tvRate2, tvRate3,tvBid,tvMsgs;
    EditText etAmt;
    Button btn1;
    Button btn2;
    Button btn3;
    ImageView btnRef;
    LinearLayout ll;
    LinearLayout fl;
    int count=0;
    JSONArray lastOver;
    Button btnRun;
    int msgindex=0;


    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public frag4() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag4, container, false);
        mRecycler = view.findViewById(R.id.questList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        b=getArguments();
        arr=b.getStringArray("details");
        tvquest = view.findViewById(R.id.tvQuest);
        tvRate1 = view.findViewById(R.id.tvRate1);
           tvRate2 = view.findViewById(R.id.tvRate2);
            tvRate3 = view.findViewById(R.id.tvRate3);
            btn1 = view.findViewById(R.id.btn1);
            btn2 = view.findViewById(R.id.btn2);
            btn3 = view.findViewById(R.id.btn3);
            ivCancel=view.findViewById(R.id.ivCancel);
            etAmt = view.findViewById(R.id.etAmt);
            ll=view.findViewById(R.id.ll);
            fl=view.findViewById(R.id.framefade);
            tvBid=view.findViewById(R.id.tvBid);
            btnRun=view.findViewById(R.id.btnRun);
            tvMsgs=view.findViewById(R.id.tvmsgs);
        iv1=view.findViewById(R.id.ivTeama);
        iv2=view.findViewById(R.id.ivTeamb);
        lastball=view.findViewById(R.id.lastball);
        mRecyclerBall=view.findViewById(R.id.mRecyclerBall);
        final DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("authid").child(muser.getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                   atoken tok= dataSnapshot.getValue(atoken.class);
                    accesstoken=tok.token;
                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnRef=view.findViewById(R.id.btnRefScore);





        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]);
        mManager = new GridLayoutManager(getContext(),2);
       mManager.setReverseLayout(true);
       mManager2=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        mRecycler.setLayoutManager(mManager);
        mRecyclerBall.setLayoutManager(mManager2);
        ivAnime=view.findViewById(R.id.ivanime);
         animation=AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
         animation2=AnimationUtils.loadAnimation(getContext(),R.anim.blink_anim);
        progressBar=view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        tvscore1=view.findViewById(R.id.score1);
        new AuthenticateTask().execute();
        new RetrieveFeedTask().execute();
        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticateTask().execute();
            }
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new FriendAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);

        ivAnime.setVisibility(View.VISIBLE);
        ivAnime.startAnimation(animation);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class AuthenticateTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url3 = new URL("https://rest.cricketapi.com/rest/v2/auth/?access_key=35faa5e182c8262090cdae7cf7be6f8b&secret_key=a7d00314119875e8a8d22d45cd7818ef&app_id=com.vincis.betradict.admin&device_id="+muser.getUid());
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
                   FirebaseDatabase.getInstance().getReference().child("authid").child(muser.getUid()).setValue(token);
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
                        final File file = new File(getContext().getFilesDir(),team1+".png");
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
                        final File file2 = new File(getContext().getFilesDir(),team2+".png");
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
                        final File file = new File(getContext().getFilesDir(),team1+".png");
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
                        final File file2 = new File(getContext().getFilesDir(),team2+".png");
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
                                Intent intent=new Intent(getContext(), trans_teama_data.class);
                                String arr[]={data,batfirst};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        iv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(), trans_teama_data.class);
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
                        mAdapter2=new ballAdapter(getContext());
                        mRecyclerBall.setAdapter(mAdapter2);
                        lastball.setText(Html.fromHtml(jsonObject3.getJSONObject("now").getJSONObject("last_ball").getString("comment")));
                        lastball.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(),trans_commentry.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });
                        tvscore1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(),trans_scoreCard.class);
                                String arr[]={data,"0"};
                                intent.putExtra("details",arr);
                                startActivity(intent);
                            }
                        });

                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvHead,tvMaxRate;

        public FriendViewHolder(View itemView) {
            super(itemView);
            tvHead=itemView.findViewById(R.id.tvHeadMain);
            tvMaxRate=itemView.findViewById(R.id.tvRateMax);
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();

        public FriendAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;
            Query query=ref.orderByChild("myans");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                        Quest quest = dataSnapshot.getValue(Quest.class);


                            // [START_EXCLUDE]
                            // Update RecyclerView
                            mQuestIds.add(dataSnapshot.getKey());
                            mQuest.add(quest);
                            notifyItemInserted(mQuest.size() - 1);


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    String userKey = dataSnapshot.getKey();
                    int userIndex = mQuestIds.indexOf(userKey);


                        // [START_EXCLUDE]

                        if (userIndex > -1) {
                            // Replace with the new data
                            mQuest.set(userIndex, quest);

                            // Update the RecyclerView
                            notifyItemChanged(userIndex);
                        } else {

                        }

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();
                    Quest quest = dataSnapshot.getValue(Quest.class);

                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQuestIds.remove(userIndex);
                            mQuest.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);

                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            query.addChildEventListener(childEventListener);
            mChildEventListener = childEventListener;

      DatabaseReference md1=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            md1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr=dataSnapshot.getValue(User.class);
                        wallet=usr.wallet;
                    //Toast.makeText(context, Integer.toString(usr.wallet.balance), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.question, viewGroup, false);

            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendViewHolder friendViewHolder, int i) {

            if(i==0)
            {
                new AuthenticateTask().execute();
                new RetrieveFeedTask().execute();
            }

                ivAnime.clearAnimation();
                ivAnime.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            final Quest quest = mQuest.get(i);
            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child("quest_wall");


            mdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Quest_wall q = dataSnapshot.getValue(Quest_wall.class);
                    if(dataSnapshot.exists()) {

                        if (q.ctbids1 / q.cbids1 <= 1) {
                            Random r = new Random();
                            int ret = r.nextInt(20 + 1);
                            rate1 = (float) ((ret / 100.0) + 1.3);
                        } else {
                            rate1 = q.ctbids1 / q.cbids1;
                            if (rate1 > 7) {
                                rate1 = 7;
                            }
                        }
                        if (q.ctbids2 / q.cbids2 <= 1) {
                            Random r = new Random();
                            int ret = r.nextInt(20 + 1);
                            rate2 = (float) ((ret / 100.0) + 1.3);
                        } else {
                            rate2 = q.ctbids2 / q.cbids2;
                            if (rate2 > 7) {
                                rate2 = 7;
                            }
                        }
                        if (q.ctbids3 / q.cbids3 <= 1) {
                            Random r = new Random();
                            int ret = r.nextInt(20 + 1);
                            rate3 = (float) ((ret / 100.0) + 1.3);
                        } else {
                            rate3 = q.ctbids3 / q.cbids3;
                            if (rate3 > 10) {
                                rate3 = 10;
                            }
                        }

                        friendViewHolder.tvMaxRate.setText(Float.toString(Math.max(rate1,Math.max(rate2,rate3))));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            friendViewHolder.tvHead.setText(quest.heading);
            if(quest.myans.equals("U"))
            {
                friendViewHolder.tvHead.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                friendViewHolder.tvHead.setBackgroundColor(getResources().getColor(R.color.green));
            }


                friendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(count==0)
                        {
                            if (quest.myans.equals("U") && quest.cans.equals("U")) {

                                etAmt.setVisibility(View.VISIBLE);

                            }
                            else {
                                etAmt.setVisibility(View.GONE);
                            }
                            if(quest.status==0)
                            {
                                btnRun.setVisibility(View.GONE);
                            }
                            else {
                                btnRun.setVisibility(View.VISIBLE);
                            }
                            count=1;
                            ll.setVisibility(View.VISIBLE);
                        float a = (float) 0.5;
                        fl.setAlpha(a);
                            if(quest.myans.equals("U"))
                            {
                                btn1.setBackgroundColor(getResources().getColor(R.color.white));
                                btn2.setBackgroundColor(getResources().getColor(R.color.white));
                                btn3.setBackgroundColor(getResources().getColor(R.color.white));
                                btn1.setText(quest.opt1);
                                btn2.setText(quest.opt2);
                                btn3.setText(quest.opt3);
                                tvBid.setVisibility(View.GONE);
                            }
                            else {
                                tvBid.setText("Your Bid:"+quest.mybid);
                                tvBid.setVisibility(View.VISIBLE);
                                if(quest.myans.equals("A"))
                                {
                                    btn1.setBackgroundColor(getResources().getColor(R.color.blue));

                                    btn2.setBackgroundColor(getResources().getColor(R.color.white));

                                    btn3.setBackgroundColor(getResources().getColor(R.color.white));
                                    btn1.setText(quest.opt1+" @"+quest.myrate);
                                    btn2.setText(quest.opt2);
                                    btn3.setText(quest.opt3);
                                }
                                else if(quest.myans.equals("B"))
                                {
                                    btn1.setBackgroundColor(getResources().getColor(R.color.white));

                                    btn2.setBackgroundColor(getResources().getColor(R.color.blue));

                                    btn3.setBackgroundColor(getResources().getColor(R.color.white));
                                    btn1.setText(quest.opt1);
                                    btn2.setText(quest.opt2+" @"+quest.myrate);
                                    btn3.setText(quest.opt3);
                                }
                                else  if(quest.myans.equals("C"))
                                {
                                    btn1.setBackgroundColor(getResources().getColor(R.color.white));

                                    btn2.setBackgroundColor(getResources().getColor(R.color.white));

                                    btn3.setBackgroundColor(getResources().getColor(R.color.blue));
                                    btn1.setText(quest.opt1);
                                    btn2.setText(quest.opt2);
                                    btn3.setText(quest.opt3+" @"+quest.myrate);
                                }
                            }

                        tvquest.setText(quest.ques);
                            mdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Quest_wall q = dataSnapshot.getValue(Quest_wall.class);
                                    if(dataSnapshot.exists()) {

                                        if (q.ctbids1 / q.cbids1 <= 1) {
                                            Random r = new Random();
                                            int ret = r.nextInt(20 + 1);
                                            rate1 = (float) ((ret / 100.0) + 1.3);
                                        } else {
                                            rate1 = q.ctbids1 / q.cbids1;
                                            if (rate1 > 7) {
                                                rate1 = 7;
                                            }
                                        }
                                        if (q.ctbids2 / q.cbids2 <= 1) {
                                            Random r = new Random();
                                            int ret = r.nextInt(20 + 1);
                                            rate2 = (float) ((ret / 100.0) + 1.3);
                                        } else {
                                            rate2 = q.ctbids2 / q.cbids2;
                                            if (rate2 > 7) {
                                                rate2 = 7;
                                            }
                                        }
                                        if (q.ctbids3 / q.cbids3 <= 1) {
                                            Random r = new Random();
                                            int ret = r.nextInt(20 + 1);
                                            rate3 = (float) ((ret / 100.0) + 1.3);
                                        } else {
                                            rate3 = q.ctbids3 / q.cbids3;
                                            if (rate3 > 10) {
                                                rate3 = 10;
                                            }
                                        }

                                        tvRate1.setText("X" + Float.toString(rate1));
                                        tvRate2.setText("X" + Float.toString(rate2));
                                        tvRate3.setText("X" + Float.toString(rate3));
                                        btn1.setVisibility(View.VISIBLE);
                                        btn2.setVisibility(View.VISIBLE);
                                        btn3.setVisibility(View.VISIBLE);
                                        tvRate1.setVisibility(View.VISIBLE);
                                        tvRate2.setVisibility(View.VISIBLE);
                                        tvRate3.setVisibility(View.VISIBLE);
                                        tvquest.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });


                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                fl.setAlpha(1);
                                ll.setVisibility(View.GONE);
                                count=0;
                            }
                        });
                        btnRun.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(msgindex==0&&(quest.cans.equals("U"))) {
                                    Float rate;
                                    if (rate1 <= rate2 && rate1 <= rate3) {
                                        rate = rate1;
                                    } else if (rate2 <= rate1 && rate2 <= rate3) {
                                        rate = rate2;
                                    } else {
                                        rate = rate3;
                                    }
                                    tvMsgs.setText("You would get:" + Float.toString(rate * quest.mybid/2));
                                    tvMsgs.setVisibility(View.VISIBLE);
                                    msgindex = 1;
                                    btnRun.setText("Confirm");
                                }
                                else if(msgindex==1&&(quest.cans.equals("U")))
                                {
                                    Float rate;
                                    if (rate1 <= rate2 && rate1 <= rate3) {
                                        rate = rate1;
                                    } else if (rate2 <= rate1 && rate2 <= rate3) {
                                        rate = rate2;
                                    } else {
                                        rate = rate3;
                                    }

                                    tvMsgs.setVisibility(View.GONE);
                                   DatabaseReference m = FirebaseDatabase.getInstance().getReference();
                                   m.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child(quest.myans).child(uId).child("cashoutstatus").setValue(1);
                                    cashout(quest.mybid, quest.qid, rate);
                                    msgindex=0;
                                    count=0;
                                    m.child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child("myans").setValue("U");

                                }

                            }
                        });
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                DatabaseReference mDatabase;

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                if (quest.status == 1) {
                                    Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                                } else if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars", Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    updatequest(Integer.parseInt(etAmt.getText().toString()), "A", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                                    fl.setAlpha(1);
                                    ll.setVisibility(View.GONE);
                                    count=0;

                                }
                            }
                        });

                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                DatabaseReference mDatabase;

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                if (quest.status == 1) {
                                    Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                                } else if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars" + wallet.balance, Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    updatequest(parseInt(etAmt.getText().toString()), "B", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                                    fl.setAlpha(1);
                                    ll.setVisibility(View.GONE);
                                    count=0;

                                }
                            }
                        });

                        btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                DatabaseReference mDatabase;

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                if (quest.status == 1) {
                                    Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                                } else if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars" + wallet.balance, Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    updatequest(parseInt(etAmt.getText().toString()), "C", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                                    fl.setAlpha(1);
                                    ll.setVisibility(View.GONE);
                                    count=0;

                                }
                            }
                        });


                    }}
                });







                }
        @Override
        public int getItemCount() {
            size=mQuest.size();
            return mQuest.size();
        }
        public void cashout(final float mybid,final String qid,final float r)
        {

            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    User user=mutableData.getValue(User.class);

                    if(user==null)
                    {

                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                        return (Transaction.success(mutableData));
                    }
                    else {
                        float money=mybid*r/2;
                        user.wallet.balance=user.wallet.balance+money;
                        Calendar cal = Calendar.getInstance();
                        Date currentDate = cal.getTime();
                        user.wallet.lastTransactions.add(new transactions(money,qid,arr[1],currentDate,arr[2]));
                        mutableData.setValue(user);
                        Toast.makeText(mContext, "Successfully done...", Toast.LENGTH_SHORT).show();
                        return (Transaction.success(mutableData));
                    }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
        }


        public void updatequest(final float bid,final String opt,final String qid,final DatabaseReference mD)
        {
            mD.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    AllQuest allQuest=mutableData.getValue(AllQuest.class);
                    if(allQuest==null)
                    {
                        return (Transaction.success(mutableData));
                    }
                    else {
                        if(opt.equals("A"))
                        {
                            rr=rate1;
                            allQuest.quest_wall.bids1=allQuest.quest_wall.bids1+bid;
                            allQuest.quest_wall.ctbids1= (float) (bid*0.9);
                            allQuest.quest_wall.cbids1=bid;
                            allQuest.quest_wall.ctbids3= (float) (allQuest.quest_wall.ctbids3+bid*0.9);
                            allQuest.quest_wall.ctbids2= (float) (allQuest.quest_wall.ctbids2+bid*0.9);
                            Quest qust = new Quest(allQuest.ques,allQuest.heading, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "A", allQuest.quest_wall.ans,(float)0);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("A").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else if(opt.equals("B"))
                        {

                            rr=rate2;

                            allQuest.quest_wall.bids2=allQuest.quest_wall.bids2+bid;
                            allQuest.quest_wall.ctbids2= (float) (bid*0.9);
                            allQuest.quest_wall.cbids2=bid;
                            allQuest.quest_wall.ctbids3= (float) (allQuest.quest_wall.ctbids3+bid*0.9);
                            allQuest.quest_wall.ctbids1= (float) (allQuest.quest_wall.ctbids1+bid*0.9);
                            Quest qust = new Quest(allQuest.ques,allQuest.heading, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "B", allQuest.quest_wall.ans,(float)0);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("B").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else
                        {

                            rr=rate3;

                            allQuest.quest_wall.bids3=allQuest.quest_wall.bids3+bid;
                            allQuest.quest_wall.ctbids3= (float) (bid*0.9);
                            allQuest.quest_wall.cbids3=bid;
                            Quest qust = new Quest(allQuest.ques,allQuest.heading, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "C", allQuest.quest_wall.ans,(float)0);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("C").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }

                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });

        }





        public void updatewallet(final float mybid,final String qid)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    User user=mutableData.getValue(User.class);

                if(user==null)
                {

                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    user.wallet.balance=user.wallet.balance-mybid;
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();

                    user.wallet.lastTransactions.add(new transactions(mybid*-1,qid,arr[1],currentDate,arr[2]));
                    mutableData.setValue(user);
                    return (Transaction.success(mutableData));
                }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    ivAnime.setVisibility(View.GONE);
                    ivAnime.clearAnimation();
                }
            });
        }



        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }

        }

    }

}
