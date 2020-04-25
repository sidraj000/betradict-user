package com.vinciis.beTraDict;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_pools extends Fragment {
    public SharedViewModel viewModel;

    RecyclerView mRecycler;
    RecyclerView mPrize;
    Bundle b;
    String arr[];
    FirebaseUser mUser;
    ImageView tabWallet,tabGuru;
    TextView tabPools;
    public Integer result=0;
    LinearLayout popUp,popUp2;
    TextView tvPopText;
    ImageView btnCancel;
    Button btnPopConfirm,btnPopCancel;
    Context mcontext;
    public String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<Integer> poolIndex=new ArrayList<>();
    List<Integer> poolRate=new ArrayList<>();
    List<Pair<Integer,Integer>> mPairs=new ArrayList<>();
    Integer count,sum=0,rate=1,index=0;

    public fragment_pools() {
         // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_pools, container, false);
        b=getArguments();
        arr=b.getStringArray("details");
        mRecycler=view.findViewById(R.id.poolList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        tabWallet=view.findViewById(R.id.tabWallet);
        tabGuru=view.findViewById(R.id.tabGuru);
        popUp=view.findViewById(R.id.popup);
        tvPopText=view.findViewById(R.id.tvPopText);
        btnPopCancel=view.findViewById(R.id.btnPopCancel);
        btnPopConfirm=view.findViewById(R.id.btnPopConfirm);
        tabPools=view.findViewById(R.id.tabPoolText);
        mPrize=view.findViewById(R.id.recyclerPrize);
        mPrize.setLayoutManager(new LinearLayoutManager(getContext()));
        popUp2=view.findViewById(R.id.popup2);
        btnCancel=view.findViewById(R.id.btnCancel);
        mcontext=getContext();
        tabWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),wallet_trans.class));

            }
        });
        tabPools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=(new Intent(getContext(),enteredPools.class));
                intent.putExtra("details",arr);
                startActivity(intent);
            }
        });
        tabGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("match").child(arr[0]).child(arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Match match=dataSnapshot.getValue(Match.class);
                        Intent intent=new Intent(getContext(),SelectMatch.class);
                        String arr[]={match.teamA,match.teamB,match.stadium,match.id,"1"};
                        Bundle b=new Bundle();
                        b.putStringArray("details",arr);
                        StadiumStatsFragment frag=new StadiumStatsFragment();
                        frag.setArguments(b);
                        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.tranFragPool,frag).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
     //   MobileAds.initialize(getContext(), "ca-app-pub-8509023804393461~4537102158");


        return view;
    }






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                arr[1]=s;
                poolAdapter madapter;
                madapter=new poolAdapter(getContext());
                mRecycler.setAdapter(madapter);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        popUp.setVisibility(View.GONE);
        popUp2.setVisibility(View.GONE);
    }
    public class prizeviewHolder extends RecyclerView.ViewHolder
    {

        TextView tvPosition,tvPrize;
        public prizeviewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition=itemView.findViewById(R.id.tvPosition);
            tvPrize=itemView.findViewById(R.id.tvPrize);
        }


    }
    public class prizeAdapter extends RecyclerView.Adapter<prizeviewHolder>
    {

        Context mContext2;
        Integer totalwinners,enteramt,totalparticipents;
        public List<Pair<String,String>> mPrizeList=new ArrayList<>();
        public prizeAdapter(Context context, Integer etamt, Integer winn,Integer partici)
        {
            mContext2=context;
            totalwinners=winn;
            totalparticipents=partici;
            enteramt=etamt;
            distribute(totalwinners,enteramt,totalparticipents);


        }



        @NonNull
        @Override
        public prizeviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            View view=inflater.inflate(R.layout.item_prizeamount,viewGroup,false);
            return new prizeviewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull prizeviewHolder prizeviewHolder, int i) {
            if(i==0)
            {

                prizeviewHolder.tvPosition.setText(Integer.toString(poolIndex.get(i+1)));
            }
            else
            {

                prizeviewHolder.tvPosition.setText(Integer.toString(poolIndex.get(i))+"-"+Integer.toString(poolIndex.get(i+1)));
            }
            prizeviewHolder.tvPrize.setText(Integer.toString(poolRate.get(i+1)));
        }

        @Override
        public int getItemCount() {
            return poolIndex.size()-1;
        }
    }
    public  class poolViewHolder extends RecyclerView.ViewHolder
    {
        TextView poolName,poolAmt,totalWinners,totalWinnings,totalParts,tvbatpc,tvballpc;
        ImageView battingPool,bowlingPool;
        LinearLayout llbatting,llbowling;
        public poolViewHolder(@NonNull View itemView) {
            super(itemView);
            poolAmt=itemView.findViewById(R.id.tvEnterAmt);
            battingPool=itemView.findViewById(R.id.ivBattingPool);
            bowlingPool=itemView.findViewById(R.id.ivBowlingPool);
            totalParts=itemView.findViewById(R.id.tvTotalParticipents);
            totalWinners=itemView.findViewById(R.id.tvTotalWinner);
            totalWinnings=itemView.findViewById(R.id.tvTotalWinnings);
            llbatting=itemView.findViewById(R.id.llbatting);
            llbowling=itemView.findViewById(R.id.llbowling);
            tvballpc=itemView.findViewById(R.id.tvbowlingpoolcount);
            tvbatpc=itemView.findViewById(R.id.tvbattingpoolcount);
        }

    }
    public  class poolAdapter extends RecyclerView.Adapter<poolViewHolder>
    {
        public Context mContext;

        public DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
        public List<Levels> mLevels=new ArrayList<>();
        public List<String> mIds=new ArrayList<>();
        public poolAdapter(Context context) {

            mContext=context;
            mRef.child("match").child(arr[0]).child(arr[1]).child("pools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Levels Levels=dataSnapshot.getValue(Levels.class);
                    String id=dataSnapshot.getKey();
                    mIds.add(id);
                    mLevels.add(Levels);
                    notifyItemInserted(mLevels.size() - 1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Levels Levels=dataSnapshot.getValue(Levels.class);
                    String id=dataSnapshot.getKey();
                    Integer index=mIds.indexOf(id);
                    mLevels.set(index,Levels);
                    notifyItemChanged(index);

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
        public poolViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
           LayoutInflater inflater=LayoutInflater.from(mContext);
           View view=inflater.inflate(R.layout.item_pools,viewGroup,false);
            return new poolViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final poolViewHolder poolViewHolder, final int i) {
            poolViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.blink_anim);
                   poolViewHolder.battingPool.startAnimation(animation);
                   poolViewHolder.bowlingPool.startAnimation(animation);

                }
            });
            poolViewHolder.poolAmt.setText(Integer.toString(mLevels.get(i).enteramount));
            poolViewHolder.totalWinnings.setText(Double.toString(mLevels.get(i).totalWinnings/1000.00)+"K");
            poolViewHolder.totalWinners.setText(Integer.toString(mLevels.get(i).totalWinners)+" Winners");
            poolViewHolder.totalParts.setText(Integer.toString(mLevels.get(i).battingPool.totalParticipants)+" Entries");
            poolViewHolder.tvbatpc.setText(Integer.toString(mLevels.get(i).battingPool.totalParticipants-mLevels.get(i).battingPool.poolUsers)+"left");
            poolViewHolder.tvballpc.setText(Integer.toString(mLevels.get(i).bowlingPool.totalParticipants-mLevels.get(i).bowlingPool.poolUsers)+"left");
            poolViewHolder.totalWinnings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prizeAdapter adapter=new prizeAdapter(mContext,mLevels.get(i).enteramount,mLevels.get(i).totalWinners,mLevels.get(i).battingPool.totalParticipants);
                    mPrize.setAdapter(adapter);
                    popUp2.setVisibility(View.VISIBLE);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp2.setVisibility(View.GONE);
                        }
                    });

                }
            });
            if( mLevels.get(i).battingPool.user.indexOf(mUser.getUid())>-1)
            {
                poolViewHolder.llbatting.setBackgroundColor(getResources().getColor(R.color.offred));
            }
            else
            {
                poolViewHolder.llbatting.setBackground(getResources().getDrawable(R.drawable.gradient_colours));
            }
            if( mLevels.get(i).bowlingPool.user.indexOf(mUser.getUid())>-1)
            {
                poolViewHolder.llbowling.setBackgroundColor(getResources().getColor(R.color.offred));
            }
            else
            {
                poolViewHolder.llbowling.setBackground(getResources().getDrawable(R.drawable.gradient_colours));
            }
            poolViewHolder.battingPool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> usr=mLevels.get(i).battingPool.user;
                   Integer index =usr.indexOf(mUser.getUid());
                    if(index<0) {


                        popUp.setVisibility(View.VISIBLE);
                        tvPopText.setText("Click to confirm payment of Rs"+Integer.toString(mLevels.get(i).enteramount));
                        btnPopConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateWallet(mLevels.get(i).enteramount,mLevels.get(i), "battingPool",mContext);

                            }
                        });
                        btnPopCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUp.setVisibility(View.GONE);
                            }
                        });



                    }

                     else
                    {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        String arrr[] = {arr[0], arr[1], mLevels.get(i).id, mLevels.get(i).battingPool.poolname, "c", "normal"};
                        intent.putExtra("details", arrr);
                        startActivity(intent);


                    }


                }
            });
            poolViewHolder.bowlingPool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> usr=mLevels.get(i).bowlingPool.user;
                    Integer index =usr.indexOf(mUser.getUid());
                    if(index<0)
                    {
                        popUp.setVisibility(View.VISIBLE);
                        tvPopText.setText("Click to confirm payment of Rs"+Integer.toString(mLevels.get(i).enteramount));
                        btnPopConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateWallet(mLevels.get(i).enteramount,mLevels.get(i), "bowlingPool",mContext);
                            }
                        });
                        btnPopCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUp.setVisibility(View.GONE);
                            }
                        });
                    }


                    else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        String arrr[] = {arr[0], arr[1], mLevels.get(i).id, mLevels.get(i).bowlingPool.poolname, "c", "normal"};
                        intent.putExtra("details", arrr);
                        startActivity(intent);
                    }



                }
            });
        }

        @Override
        public int getItemCount() {
            return mLevels.size();
        }
    }
    public void updatematch(DatabaseReference md)
    {
        md.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Pools pool=mutableData.getValue(Pools.class);

                if(pool==null)
                {

                    return (Transaction.success(mutableData));
                }
                else {
                    pool.user.add(mUser.getUid());
                    pool.poolUsers++;
                    mutableData.setValue(pool);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
    public void updateWallet(final Integer enterAmt, final Levels levels, final String poolName, final Context context)
    {
        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("wallet").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet wallet=mutableData.getValue(Wallet.class);

                if(wallet==null)
                {
                    return (Transaction.success(mutableData));

                }
                else {
                    if(wallet.balance>=enterAmt) {

                        wallet.balance = wallet.balance - enterAmt;
                        if(poolName.equals("battingPool")) {

                            List<String> usr = levels.battingPool.user;
                            usr.add(mUser.getUid());
                            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            updatematch(mRef.child("match").child(arr[0]).child(arr[1]).child("pools").child(levels.id).child("battingPool"));
                            //  mRef.child("match").child(arr[0]).child(arr[1]).child("pools").child(levels.id).child("battingPool").setValue(new Pools(levels.battingPool.poolname,levels.enteramount,(levels.battingPool.poolUsers+1),usr));
                            mRef.child("LeaderBoard").child(arr[0]).child(arr[1]).child(levels.id).child(levels.battingPool.poolname).child(mUser.getUid()).setValue(new LeaderBAmt((float) 1000, mUser.getDisplayName(), mUser.getPhotoUrl().toString()));
                            Calendar cal = Calendar.getInstance();
                            Date currentDate = cal.getTime();


                            mRef.child("quest").child(arr[0]).child(arr[1]).child(levels.id).child(levels.battingPool.poolname).child(arr[2]).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                                    List<Answer> pairlist = new ArrayList<>();
                                    pairlist.add(new Answer("U", 0, (float) 0));
                                    mRef.child("quest_usr").child(mUser.getUid()).child(arr[0]).child(arr[1]).child(levels.id).child(levels.battingPool.poolname).child(arr[2]).child(allQuest.qid).setValue(new Quest(allQuest.ques, allQuest.heading, allQuest.type, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 0, 0, 0, "U", allQuest.quest_wall.ans, pairlist, (float) 0));
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
                            wallet.lastTransactions.add(new transactions(enterAmt*-1,arr[1],levels.id,poolName,currentDate));
                            wallet.trollars=wallet.trollars+1000;
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            String arrr[] = {arr[0], arr[1], levels.id, levels.battingPool.poolname, "c", "normal"};
                            intent.putExtra("details", arrr);
                            startActivity(intent);
                            mutableData.setValue(wallet);
                            return (Transaction.success(mutableData));
                        }
                        else
                        {
                            List<String> usr = levels.bowlingPool.user;
                            usr.add(mUser.getUid());
                            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            updatematch(mRef.child("match").child(arr[0]).child(arr[1]).child("pools").child(levels.id).child("bowlingPool"));
                            Calendar cal = Calendar.getInstance();
                            Date currentDate = cal.getTime();
                            mRef.child("LeaderBoard").child(arr[0]).child(arr[1]).child(levels.id).child(levels.bowlingPool.poolname).child(mUser.getUid()).setValue(new LeaderBAmt((float) 1000, mUser.getDisplayName(), mUser.getPhotoUrl().toString()));
                            mRef.child("quest").child(arr[0]).child(arr[1]).child(levels.id).child(levels.bowlingPool.poolname).child(arr[2]).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                                    List<Answer> pairlist = new ArrayList<>();
                                    pairlist.add(new Answer("U", 0, (float) 0));
                                    mRef.child("quest_usr").child(mUser.getUid()).child(arr[0]).child(arr[1]).child(levels.id).child(levels.bowlingPool.poolname).child(arr[2]).child(allQuest.qid).setValue(new Quest(allQuest.ques, allQuest.heading, allQuest.type, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 0, 0, 0, "U", allQuest.quest_wall.ans, pairlist, (float) 0));
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
                            wallet.lastTransactions.add(new transactions(enterAmt*-1,arr[1],levels.id,poolName,currentDate));
                            wallet.trollars=wallet.trollars+1000;
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            String arrr[] = {arr[0], arr[1], levels.id, levels.bowlingPool.poolname, "c", "normal"};
                            intent.putExtra("details", arrr);
                            startActivity(intent);
                            mutableData.setValue(wallet);
                            return (Transaction.success(mutableData));
                        }
                    }

                    else
                    {
                        popUp.setVisibility(View.VISIBLE);
                        tvPopText.setText("Insufficient Balance!!Click to add trollers");
                        btnPopConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                        startActivity(new Intent(getContext(),ProfileActivity.class));
                            }
                        });
                        btnPopCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUp.setVisibility(View.GONE);
                            }
                        });
                        return (Transaction.success(mutableData));
                    }

                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
    public void distribute(Integer winners,Integer amt,Integer participents)
    {
        poolIndex.clear();
        poolRate.clear();
        mPairs.clear();
        poolIndex.add(0);
        poolIndex.add(1);
        poolIndex.add(5);
        poolRate.add(1);
        poolRate.add(1);
        poolRate.add(1);
        count=5;
        while (count<winners)
        {
            if(count*4>winners)
            {
                count=winners;
                poolIndex.add(count);
                poolRate.add(1);
            }
            else {
                count = count * 2;
                poolIndex.add(count);
                poolRate.add(1);
            }
        }



        for(int i=poolIndex.size()-1;i>0;i--)
        {
            sum=sum+rate*(poolIndex.get(i)-poolIndex.get(i-1));
            poolRate.set(i,rate);
            rate=rate*2;
        }
        for(int i=1;i<poolRate.size();i++)
        {
            int at;
            poolRate.set(i,(int)((poolRate.get(i)*participents*0.85*amt)/sum));
        }
        for(int i=1;i<poolRate.size();i++)
        {
            while(index<poolIndex.get(i)) {
                Pair<Integer, Integer> pair = new Pair<>(index,poolRate.get(i)*amt);
                mPairs.add(pair);
                index++;
            }
        }
    }


}
