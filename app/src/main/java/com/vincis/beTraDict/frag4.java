package com.vincis.beTraDict;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
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
    ImageView leaderboard;
    private RecyclerView mRecycler,mRecyclerBall;
    private FriendAdapter mAdapter;
   // private ballAdapter mAdapter2;
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
    TextView tvBalance;
    ImageView dQuest;


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
            leaderboard=view.findViewById(R.id.one);
            dQuest=view.findViewById(R.id.two);
            dQuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            final AllQuest allQuest=dataSnapshot.getValue(AllQuest.class);
                            final String key=dataSnapshot.getKey();
                            final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]);
                            ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists())
                                    {
                                        List<Answer> pairlist=new ArrayList<>();
                                        pairlist.add(new Answer("U",0, (float) 0));
                                        ref.child(allQuest.qid).setValue(new Quest(allQuest.ques,allQuest.heading,allQuest.type,allQuest.opt1,allQuest.opt2,allQuest.opt3,allQuest.qid,0,0,0,"U",allQuest.quest_wall.ans,pairlist, (float) 0));

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

                    Intent intent=new Intent(getContext(),MainActivity.class);
                    String arrr[] = {arr[0],arr[1],arr[2],arr[3],"c","Dynamic"};
                    intent.putExtra("details",arrr);
                    startActivity(intent);
                }
            });
        iv1=view.findViewById(R.id.ivTeama);
        iv2=view.findViewById(R.id.ivTeamb);
        lastball=view.findViewById(R.id.lastball);
        tvBalance=view.findViewById(R.id.walletbal);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),trans_leader.class);
                intent.putExtra("details",arr);
                startActivity(intent);
            }
        });
     //   mRecyclerBall=view.findViewById(R.id.mRecyclerBall);
         final DatabaseReference database=FirebaseDatabase.getInstance().getReference();
        database.child("authid").child(muser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        database.child("LeaderBoard").child(arr[1]).child(arr[2]+arr[3]).child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LeaderBAmt leaderBAmt=dataSnapshot.getValue(LeaderBAmt.class);
                float amt= leaderBAmt.Amt;
                int a=(int) amt;
                tvBalance.setText(Integer.toString(a));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnRef=view.findViewById(R.id.btnRefScore);





        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]);
        mManager = new GridLayoutManager(getContext(),2);
       mManager.setReverseLayout(true);
       mManager2=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        mRecycler.setLayoutManager(mManager);
//        mRecyclerBall.setLayoutManager(mManager2);
        ivAnime=view.findViewById(R.id.ivanime);
         animation=AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
         animation2=AnimationUtils.loadAnimation(getContext(),R.anim.blink_anim);
  //      progressBar=view.findViewById(R.id.progressBar2);
//        progressBar.setVisibility(View.GONE);
      //  tvscore1=view.findViewById(R.id.score1);
     //   new AuthenticateTask().execute();
       // new RetrieveFeedTask().execute();
    /*    btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new AuthenticateTask().execute();
            }
        });*/
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
                        if(quest.type.equals(arr[5])) {


                            // [START_EXCLUDE]
                            // Update RecyclerView
                            mQuestIds.add(dataSnapshot.getKey());
                            mQuest.add(quest);
                            notifyItemInserted(mQuest.size() - 1);
                        }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    if(quest.type.equals(arr[5])) {
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

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    if(quest.type.equals(arr[5])) {

                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQuestIds.remove(userIndex);
                            mQuest.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);

                        }
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

                ivAnime.clearAnimation();
                ivAnime.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            final Quest quest = mQuest.get(i);
            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).child("quest_wall");


            mdb.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if (quest.cans.equals("U")) {

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
                                   m.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).child(quest.myans).child(uId).child("cashoutstatus").setValue(1);
                                    cashout(quest.mybid, quest.qid, rate);
                                    msgindex=0;
                                    count=0;
                                    m.child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).child("myans").setValue("U");

                                }

                            }
                        });
                        tvBid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(),bidsDetails.class);
                                String arrdet[]={arr[0],arr[1],arr[2],arr[3],quest.qid, Float.toString(Math.min(rate1,Math.min(rate2,rate3)))};
                                intent.putExtra("details",arrdet);
                                startActivity(intent);
                            }
                        });
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                DatabaseReference mDatabase;

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                 if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars", Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    List<Answer> answers=quest.answers;
                                    answers.add(new Answer("A",parseInt(etAmt.getText().toString()),rate1));
                                    Quest qust = new Quest(quest.ques,quest.heading,quest.type,quest.opt1, quest.opt2, quest.opt3, quest.qid, 1,parseInt(etAmt.getText().toString()), rate1, "A", quest.cans,answers,(float)0);
                                    DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
                                    mData.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).setValue(qust);
                                    updatequest(Integer.parseInt(etAmt.getText().toString()), "A", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid));
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
                                if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars" + wallet.balance, Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    List<Answer> answers=quest.answers;
                                    answers.add(new Answer("B",parseInt(etAmt.getText().toString()),rate2));
                                    Quest qust = new Quest(quest.ques,quest.heading,quest.type,quest.opt1, quest.opt2, quest.opt3, quest.qid, 1,parseInt(etAmt.getText().toString()), rate2, "B", quest.cans,answers,(float)0);
                                    DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
                                    mData.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).setValue(qust);

                                    updatequest(parseInt(etAmt.getText().toString()), "B", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid));
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
                                if (etAmt.getText().toString().isEmpty()) {
                                    Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (wallet.balance < parseInt(etAmt.getText().toString())) {
                                    Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                                } else if (parseInt(etAmt.getText().toString()) < 2) {
                                    Toast.makeText(mContext, "Minimum bid is 2 trollars" + wallet.balance, Toast.LENGTH_SHORT).show();

                                } else {
                                    ivAnime.startAnimation(animation);
                                    ivAnime.setVisibility(View.VISIBLE);
                                    List<Answer> answers=quest.answers;
                                    answers.add(new Answer("C",parseInt(etAmt.getText().toString()),rate3));
                                    Quest qust = new Quest(quest.ques,quest.heading,quest.type,quest.opt1, quest.opt2, quest.opt3, quest.qid, 1,parseInt(etAmt.getText().toString()), rate3, "C", quest.cans,answers,(float)0);
                                    DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
                                    mData.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid).setValue(qust);

                                    updatequest(parseInt(etAmt.getText().toString()), "C", quest.qid, mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(quest.qid));
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
                            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                            String key= mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(qid).push().getKey();
                            mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(key).setValue(new Trollars(qid,bid*-1,rr,opt));
                            allQuest.quest_wall.bids1=allQuest.quest_wall.bids1+bid;
                            allQuest.quest_wall.ctbids1= (float) (bid*0.9);
                            allQuest.quest_wall.cbids1=bid;
                            allQuest.quest_wall.ctbids3= (float) (allQuest.quest_wall.ctbids3+bid*0.9);
                            allQuest.quest_wall.ctbids2= (float) (allQuest.quest_wall.ctbids2+bid*0.9);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                           // mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                             mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(allQuest.qid).child("A").child(key).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else if(opt.equals("B"))
                        {

                            rr=rate2;
                            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                            String key= mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(qid).push().getKey();
                            mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(key).setValue(new Trollars(qid,bid*-1,rr,opt));

                            allQuest.quest_wall.bids2=allQuest.quest_wall.bids2+bid;
                            allQuest.quest_wall.ctbids2= (float) (bid*0.9);
                            allQuest.quest_wall.cbids2=bid;
                            allQuest.quest_wall.ctbids3= (float) (allQuest.quest_wall.ctbids3+bid*0.9);
                            allQuest.quest_wall.ctbids1= (float) (allQuest.quest_wall.ctbids1+bid*0.9);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(allQuest.qid).child("B").child(key).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else
                        {

                            rr=rate3;
                            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                            String key= mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(qid).push().getKey();
                            mRef.child("Trollars").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(key).setValue(new Trollars(qid,bid*-1,rr,opt));
                            allQuest.quest_wall.bids3=allQuest.quest_wall.bids3+bid;
                            allQuest.quest_wall.ctbids3= (float) (bid*0.9);
                            allQuest.quest_wall.cbids3=bid;
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(allQuest.qid).child("C").child(key).setValue(usr);

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
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("LeaderBoard").child(arr[1]).child(arr[2]+arr[3]).child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    LeaderBAmt amt=mutableData.getValue(LeaderBAmt.class);

                if(amt==null)
                {

                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    amt.Amt=amt.Amt-mybid;
                    mutableData.setValue(amt);
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
