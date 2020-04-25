package com.vinciis.beTraDict;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamPreviousMatchFragment extends Fragment {


    TextView team_1,team_2,team_1Opp,team_2Opp;
    TextView team1_date,team2_date;
    TextView team1_toss,team2_toss;
    TextView team1_score,team2_score;
    TextView team1_wickettaken,team2_wickettaken;
    TextView team1_6s,team2_6s,team_1ROS,team_2ROS,team_1Noball,team_2Noball,team_1Wide,team_2Wide;
    TextView team1_4s,team2_4s;
    TextView team1_extras,team2_extras;
    TextView team1_op,team2_op;
    TextView team_1hc,team_2hc,team_1c,team_2c,team_1bUsed,team_2bUsed;
    TextView team1_totalcatches,team2_totalcatches;
    TextView team1_result,team2_result;
    String arr[];
    ImageView ivPlayer,ivStad,ivTeam;
    Button match1,match2,match3;
    public List<MatchDetail> mDetail;
    public SharedViewModel viewModel;
    public TeamPreviousMatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b=this.getArguments();
        String t1_Id=b.getString("team1");
        String t2_Id=b.getString("team2");
        View view = inflater.inflate(R.layout.fragment_team_previous_match, container, false);
        arr=getArguments().getStringArray("details");
        team_1=view.findViewById(R.id.team1);
        team_2=view.findViewById(R.id.team2);
        team_1Opp=view.findViewById(R.id.teamA_opp);
        team_2Opp=view.findViewById(R.id.teamB_opp);
        team1_date=view.findViewById(R.id.teamA_mdate);
        team2_date=view.findViewById(R.id.teamB_mdate);
        team1_toss=view.findViewById(R.id.teamA_toss);
        team2_toss=view.findViewById(R.id.teamB_toss);
        team1_score=view.findViewById(R.id.teamA_score);
        team2_score=view.findViewById(R.id.teamB_score);
        team1_wickettaken=view.findViewById(R.id.teamA_wicketTaken);
        team2_wickettaken=view.findViewById(R.id.teamB_wicketTaken);
        team1_6s=view.findViewById(R.id.teamA_total6s);
        team2_6s=view.findViewById(R.id.teamB_total6s);
        team1_4s=view.findViewById(R.id.teamA_total4s);
        team2_4s=view.findViewById(R.id.teamB_total4s);
        team1_extras=view.findViewById(R.id.teamA_extras);
        team2_extras=view.findViewById(R.id.teamB_extras);
        team_1ROS=view.findViewById(R.id.teamA_rs);
        team_2ROS=view.findViewById(R.id.teamB_rs);
        team_1Noball=view.findViewById(R.id.teamA_noBalls);
        team_2Noball=view.findViewById(R.id.teamB_noBalls);
        team_1Wide=view.findViewById(R.id.teamA_wideBalls);
        team_2Wide=view.findViewById(R.id.teamB_wideBalls);
        team_1hc=view.findViewById(R.id.teamA_hc);
        team_2hc=view.findViewById(R.id.teamB_hc);
        team_1c=view.findViewById(R.id.teamA_c);
        team_2c=view.findViewById(R.id.teamB_c);
        team_1bUsed=view.findViewById(R.id.teamA_bU);
        team_2bUsed=view.findViewById(R.id.teamB_bu);
        team1_totalcatches=view.findViewById(R.id.teamA_tcatches);
        team2_totalcatches=view.findViewById(R.id.teamB_tcatches);
        team1_result=view.findViewById(R.id.teamA_result);
        team2_result=view.findViewById(R.id.teamB_result);

        ivStad=view.findViewById(R.id.btnStadium);
        ivPlayer=view.findViewById(R.id.btnPlayer);
        ivTeam=view.findViewById(R.id.btnMatch);

        match1=view.findViewById(R.id.stadiumm1);
        match2=view.findViewById(R.id.stadiumm2);
        match3=view.findViewById(R.id.stadiumm3);

        loadData();
        match1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_1.setText(mDetail.get(mDetail.size()-1).team1);
                team_2.setText(mDetail.get(mDetail.size()-1).team2);
                team_1Opp.setText(mDetail.get(mDetail.size()-1).team1Opp);
                team_2Opp.setText(mDetail.get(mDetail.size()-1).team2Opp);
                team1_date.setText(mDetail.get(mDetail.size()-1).t1_date);
                team2_date.setText(mDetail.get(mDetail.size()-1).t2_date);
                team1_toss.setText(mDetail.get(mDetail.size()-1).t1_toss);
                team2_toss.setText(mDetail.get(mDetail.size()-1).t2_toss);
                team1_score.setText(mDetail.get(mDetail.size()-1).t1_score);
                team2_score.setText(mDetail.get(mDetail.size()-1).t2_score);
                team1_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_wicketTaken));
                team2_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_wicketTaken));
                team1_4s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_total4s));
                team2_4s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_total4s));
                team1_6s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_total6s));
                team2_6s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_total6s));
                team1_extras.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_extras));
                team2_extras.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_extras));
                team_1c.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_c));
                team_2c.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_c));
                team_1hc.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_hc));
                team_2hc.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_hc));
                team_1Wide.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_wideBalls));
                team_2Wide.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_wide_balls));
                team_1Noball.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_noBalls));
                team_2Noball.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_noBalls));
                team_1bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_bU));
                team_2bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_bU));
                team_1ROS.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_ros));
                team_2ROS.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_ros));
                team1_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_totalCatches));
                team2_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_totalCatches));
                team1_result.setText(mDetail.get(mDetail.size()-1).t1_result);
                team2_result.setText(mDetail.get(mDetail.size()-1).t2_result);
            }
        });
        match2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_1.setText(mDetail.get(mDetail.size()-2).team1);
                team_2.setText(mDetail.get(mDetail.size()-2).team2);
                team_1Opp.setText(mDetail.get(mDetail.size()-2).team1Opp);
                team_2Opp.setText(mDetail.get(mDetail.size()-2).team2Opp);
                team1_date.setText(mDetail.get(mDetail.size()-2).t1_date);
                team2_date.setText(mDetail.get(mDetail.size()-2).t2_date);
                team1_toss.setText(mDetail.get(mDetail.size()-2).t1_toss);
                team2_toss.setText(mDetail.get(mDetail.size()-2).t2_toss);
                team1_score.setText(mDetail.get(mDetail.size()-2).t1_score);
                team2_score.setText(mDetail.get(mDetail.size()-2).t2_score);
                team1_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_wicketTaken));
                team2_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_wicketTaken));
                team1_4s.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_total4s));
                team2_4s.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_total4s));
                team1_6s.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_total6s));
                team2_6s.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_total6s));
                team1_extras.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_extras));
                team2_extras.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_extras));
                team_1c.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_c));
                team_2c.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_c));
                team_1hc.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_hc));
                team_2hc.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_hc));
                team_1Wide.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_wideBalls));
                team_2Wide.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_wide_balls));
                team_1Noball.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_noBalls));
                team_2Noball.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_noBalls));
                team_1bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_bU));
                team_2bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_bU));
                team_1ROS.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_ros));
                team_2ROS.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_ros));
                team1_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-2).t1_totalCatches));
                team2_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-2).t2_totalCatches));
                team1_result.setText(mDetail.get(mDetail.size()-2).t1_result);
                team2_result.setText(mDetail.get(mDetail.size()-2).t2_result);
            }
        });
        match3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_1.setText(mDetail.get(mDetail.size()-3).team1);
                team_2.setText(mDetail.get(mDetail.size()-3).team2);
                team_1Opp.setText(mDetail.get(mDetail.size()-3).team1Opp);
                team_2Opp.setText(mDetail.get(mDetail.size()-3).team2Opp);
                team1_date.setText(mDetail.get(mDetail.size()-3).t1_date);
                team2_date.setText(mDetail.get(mDetail.size()-3).t2_date);
                team1_toss.setText(mDetail.get(mDetail.size()-3).t1_toss);
                team2_toss.setText(mDetail.get(mDetail.size()-3).t2_toss);
                team1_score.setText(mDetail.get(mDetail.size()-3).t1_score);
                team2_score.setText(mDetail.get(mDetail.size()-3).t2_score);
                team1_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_wicketTaken));
                team2_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_wicketTaken));
                team1_4s.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_total4s));
                team2_4s.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_total4s));
                team1_6s.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_total6s));
                team2_6s.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_total6s));
                team1_extras.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_extras));
                team2_extras.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_extras));
                team_1c.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_c));
                team_2c.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_c));
                team_1hc.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_hc));
                team_2hc.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_hc));
                team_1Wide.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_wideBalls));
                team_2Wide.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_wide_balls));
                team_1Noball.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_noBalls));
                team_2Noball.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_noBalls));
                team_1bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_bU));
                team_2bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_bU));
                team_1ROS.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_ros));
                team_2ROS.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_ros));
                team1_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-3).t1_totalCatches));
                team2_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-3).t2_totalCatches));
                team1_result.setText(mDetail.get(mDetail.size()-3).t1_result);
                team2_result.setText(mDetail.get(mDetail.size()-3).t2_result);
            }
        });
        ivStad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr[4].equals("2"))
                {

                    Bundle b = new Bundle();
                    b.putStringArray("details", arr);
                    StadiumStatsFragment frag = new StadiumStatsFragment();
                    frag.setArguments(b);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.transmanin, frag).commit();
                }
                else {

                    Bundle b = new Bundle();
                    b.putStringArray("details", arr);
                    StadiumStatsFragment frag = new StadiumStatsFragment();
                    frag.setArguments(b);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.tranFragPool, frag).commit();
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                arr[2]=s;
                loadData();
            }
        });
    }
    public void loadData()
    {
        FirebaseDatabase.getInstance().getReference().child("analytics").child("cricket").child("match").child(arr[3]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MatchDetailList matchDetaillist=dataSnapshot.getValue(MatchDetailList.class);
                mDetail=matchDetaillist.matchdetail;
                team_1.setText(mDetail.get(mDetail.size()-1).team1);
                team_2.setText(mDetail.get(mDetail.size()-1).team2);
                team_1Opp.setText(mDetail.get(mDetail.size()-1).team1Opp);
                team_2Opp.setText(mDetail.get(mDetail.size()-1).team2Opp);
                team1_date.setText(mDetail.get(mDetail.size()-1).t1_date);
                team2_date.setText(mDetail.get(mDetail.size()-1).t2_date);
                team1_toss.setText(mDetail.get(mDetail.size()-1).t1_toss);
                team2_toss.setText(mDetail.get(mDetail.size()-1).t2_toss);
                team1_score.setText(mDetail.get(mDetail.size()-1).t1_score);
                team2_score.setText(mDetail.get(mDetail.size()-1).t2_score);
                team1_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_wicketTaken));
                team2_wickettaken.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_wicketTaken));
                team1_4s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_total4s));
                team2_4s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_total4s));
                team1_6s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_total6s));
                team2_6s.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_total6s));
                team1_extras.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_extras));
                team2_extras.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_extras));
                team_1c.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_c));
                team_2c.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_c));
                team_1hc.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_hc));
                team_2hc.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_hc));
                team_1Wide.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_wideBalls));
                team_2Wide.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_wide_balls));
                team_1Noball.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_noBalls));
                team_2Noball.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_noBalls));
                team_1bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_bU));
                team_2bUsed.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_bU));
                team_1ROS.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_ros));
                team_2ROS.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_ros));
                team1_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-1).t1_totalCatches));
                team2_totalcatches.setText(String.valueOf(mDetail.get(mDetail.size()-1).t2_totalCatches));
                team1_result.setText(mDetail.get(mDetail.size()-1).t1_result);
                team2_result.setText(mDetail.get(mDetail.size()-1).t2_result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private static class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
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
        }
    }



}
