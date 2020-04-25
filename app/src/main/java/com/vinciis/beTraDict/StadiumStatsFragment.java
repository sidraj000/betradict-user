package com.vinciis.beTraDict;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StadiumStatsFragment extends Fragment {
    private DatabaseReference mFriendReference;
    public SharedViewModel viewModel;
    TextView t1, t2, t1_date, t2_date, t1_score, t2_score;
    TextView t1_wicket, t2_wicket, t1_6s, t2_6s, t1_4s, t2_4s, t1_catches, t2_catches;
    TextView t1_result, t2_result;
    String arr[];

    private List<String> StadiumIds = new ArrayList<>();
    private List<StadiumData> stadiumlist = new ArrayList<>();
    Button match1,match2,match3;
    ImageView ivPlayer,ivStad,ivTeam;




    public StadiumStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                FirebaseDatabase.getInstance().getReference().child("match").child("cricket").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Match match=dataSnapshot.getValue(Match.class);
                        arr[2]=match.stadium;
                        arr[3]=s;
                        loadData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stadium_stats, container, false);
        arr=getArguments().getStringArray("details");
        mFriendReference = FirebaseDatabase.getInstance().getReference().child("analytics").child("cricket").child("stadium").child(arr[2]);
        t1 = view.findViewById(R.id.team1);
        t2 = view.findViewById(R.id.team2);
        ivStad=view.findViewById(R.id.btnStadium);
        ivPlayer=view.findViewById(R.id.btnPlayer);
        ivTeam=view.findViewById(R.id.btnMatch);
        t1_date = view.findViewById(R.id.teamA_mdate);
        t2_date = view.findViewById(R.id.teamB_mdate);
        t1_score = view.findViewById(R.id.teamA_score);
        t2_score = view.findViewById(R.id.teamB_score);
        t1_wicket = view.findViewById(R.id.teamA_wicketTaken);
        t2_wicket = view.findViewById(R.id.teamB_wicketTaken);
        t1_6s = view.findViewById(R.id.teamA_total6s);
        t2_6s = view.findViewById(R.id.teamB_total6s);
        t1_4s = view.findViewById(R.id.teamA_total4s);
        t2_4s = view.findViewById(R.id.teamB_total4s);
        t1_catches = view.findViewById(R.id.teamA_tcatches);
        t2_catches = view.findViewById(R.id.teamB_tcatches);
        t1_result = view.findViewById(R.id.teamA_result);
        t2_result = view.findViewById(R.id.teamB_result);
        match1=view.findViewById(R.id.stadiumm1);
        match2=view.findViewById(R.id.stadiumm2);
        match3=view.findViewById(R.id.stadiumm3);

        ivTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr[4].equals("2"))
                {

                    Bundle b = new Bundle();
                    b.putStringArray("details", arr);
                    TeamPreviousMatchFragment frag = new TeamPreviousMatchFragment();
                    frag.setArguments(b);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.transmanin, frag).commit();
                }
                else {

                    Bundle b = new Bundle();
                    b.putStringArray("details", arr);
                    TeamPreviousMatchFragment frag = new TeamPreviousMatchFragment();
                    frag.setArguments(b);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.tranFragPool, frag).commit();
                }
            }
        });
        match2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText(stadiumlist.get(stadiumlist.size()-2).t1Name);
                t2.setText(stadiumlist.get(stadiumlist.size()-2).t2Name);
                t1_date.setText(stadiumlist.get(stadiumlist.size()-2).t1Date);
                t2_date.setText(stadiumlist.get(stadiumlist.size()-2).t2Date);
                t1_score.setText(stadiumlist.get(stadiumlist.size()-2).t1Score);
                t2_score.setText(stadiumlist.get(stadiumlist.size()-2).t2Score);
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t1WicketTaken));
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t2WicketTaken));
                t1_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t14s));
                t2_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t24s));
                t1_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t16s));
                t2_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t26s));
                t1_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t1Catches));
                t2_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-2).t2Catches));
                t1_result.setText(stadiumlist.get(stadiumlist.size()-2).t1Result);
                t2_result.setText(stadiumlist.get(stadiumlist.size()-2).t2Result);
            }
        });
        match3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText(stadiumlist.get(stadiumlist.size()-3).t1Name);
                t2.setText(stadiumlist.get(stadiumlist.size()-3).t2Name);
                t1_date.setText(stadiumlist.get(stadiumlist.size()-3).t1Date);
                t2_date.setText(stadiumlist.get(stadiumlist.size()-3).t2Date);
                t1_score.setText(stadiumlist.get(stadiumlist.size()-3).t1Score);
                t2_score.setText(stadiumlist.get(stadiumlist.size()-3).t2Score);
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t1WicketTaken));
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t2WicketTaken));
                t1_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t14s));
                t2_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t24s));
                t1_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t16s));
                t2_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t26s));
                t1_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t1Catches));
                t2_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-3).t2Catches));
                t1_result.setText(stadiumlist.get(stadiumlist.size()-3).t1Result);
                t2_result.setText(stadiumlist.get(stadiumlist.size()-3).t2Result);
            }
        });
        match1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText(stadiumlist.get(stadiumlist.size()-1).t1Name);
                t2.setText(stadiumlist.get(stadiumlist.size()-1).t2Name);
                t1_date.setText(stadiumlist.get(stadiumlist.size()-1).t1Date);
                t2_date.setText(stadiumlist.get(stadiumlist.size()-1).t2Date);
                t1_score.setText(stadiumlist.get(stadiumlist.size()-1).t1Score);
                t2_score.setText(stadiumlist.get(stadiumlist.size()-1).t2Score);
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t1WicketTaken));
                t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t2WicketTaken));
                t1_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t14s));
                t2_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t24s));
                t1_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t16s));
                t2_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t26s));
                t1_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t1Catches));
                t2_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t2Catches));
                t1_result.setText(stadiumlist.get(stadiumlist.size()-1).t1Result);
                t2_result.setText(stadiumlist.get(stadiumlist.size()-1).t2Result);
            }
        });
      loadData();
        return view;
        }
        public void loadData()
        {
            mFriendReference = FirebaseDatabase.getInstance().getReference().child("analytics").child("cricket").child("stadium").child(arr[2]);
            mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stadiumAnalytics listdata = dataSnapshot.getValue(stadiumAnalytics.class);
                    stadiumlist = listdata.list;

                    t1.setText(stadiumlist.get(stadiumlist.size()-1).t1Name);
                    t2.setText(stadiumlist.get(stadiumlist.size()-1).t2Name);
                    t1_date.setText(stadiumlist.get(stadiumlist.size()-1).t1Date);
                    t2_date.setText(stadiumlist.get(stadiumlist.size()-1).t2Date);
                    t1_score.setText(stadiumlist.get(stadiumlist.size()-1).t1Score);
                    t2_score.setText(stadiumlist.get(stadiumlist.size()-1).t2Score);
                    t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t1WicketTaken));
                    t1_wicket.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t2WicketTaken));
                    t1_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t14s));
                    t2_4s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t24s));
                    t1_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t16s));
                    t2_6s.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t26s));
                    t1_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t1Catches));
                    t2_catches.setText(String.valueOf(stadiumlist.get(stadiumlist.size()-1).t2Catches));
                    t1_result.setText(stadiumlist.get(stadiumlist.size()-1).t1Result);
                    t2_result.setText(stadiumlist.get(stadiumlist.size()-1).t2Result);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }