package com.vinciis.beTraDict;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class scorecard extends Fragment {
    String[] response;
    Button teama,teamb;
    TextView fow;
    private RecyclerView mRecycler;
    private RecyclerView mRecycler2;
    private RecyclerView mRecycler3;
    private Team1Adapter mAdapter;
    private Team1Adapter mAdapter3;
    private LinearLayoutManager mManager;
    public JSONObject jsonObj,jsonObj2,jsonObject3;
    public JSONArray jsonBattingOrder;
    int count=0;
    List<String> order=new ArrayList<>();
    public List<String> teambattingorder=new ArrayList<>();
    public scorecard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        response=getArguments().getStringArray("data");
        count= Integer.parseInt(response[1]);
       View view=inflater.inflate(R.layout.fragment_scorecard, container, false);
       teama=view.findViewById(R.id.btnteamaa);
       teamb=view.findViewById(R.id.btnteambb);
       fow=view.findViewById(R.id.fallofwickets);
       teamb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             //  if(jsonBattingOrder.length()==2) {
                   teamb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                   teama.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                   mAdapter= new Team1Adapter(getContext(), 1,0);
                   mAdapter3=new Team1Adapter(getContext(),0,1);
                   mRecycler3.setAdapter(mAdapter3);
                   mRecycler.setAdapter(mAdapter);
            /*   }
               else
               {
                   Toast.makeText(getContext(), "Yet to Bat", Toast.LENGTH_SHORT).show();
               }*/
           }
       });
       teama.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               teama.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               teamb.setBackgroundColor(getResources().getColor(R.color.colorAccent));
              mAdapter=new Team1Adapter(getContext(),0,0);
               mAdapter3=new Team1Adapter(getContext(),1,1);
               mRecycler.setAdapter(mAdapter);
               mRecycler3.setAdapter(mAdapter3);
           }
       });
        try {
            jsonObj=new JSONObject(response[0]);

            jsonObj2=jsonObj.getJSONObject("data");
            jsonObject3=jsonObj2.getJSONObject("card");
            jsonBattingOrder=jsonObject3.getJSONArray("batting_order");
            for(int l=0;l<jsonBattingOrder.length();l++)
            {
                teambattingorder.add(jsonObject3.getJSONObject("teams").getJSONObject(jsonBattingOrder.getJSONArray(l).getString(0)).getString("short_name"));
                order.add(jsonBattingOrder.getJSONArray(l).getString(0));
            }
            if(jsonBattingOrder.length()==0)
            {
                teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("b").getString("short_name"));
                teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("a").getString("short_name"));
                order.add("b");
                order.add("a");
            }
            else if(jsonBattingOrder.length()==1)
            {
                if ( jsonBattingOrder.getJSONArray(0).getString(0).equals("a"))
                {
                  teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("b").getString("short_name"));
                  order.add("b");
                }
                else
                {
                    teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("a").getString("short_name"));
                    order.add("a");
                }
            }
            teama.setText(teambattingorder.get(0));
            teamb.setText(teambattingorder.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRecycler = view.findViewById(R.id.scorecardRecycler);
        mRecycler3=view.findViewById(R.id.scorecardRecyclerb);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler3.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler3.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new Team1Adapter(getContext(),count,0);
        if(count==1) {
            mAdapter3 = new Team1Adapter(getContext(), 0, 1);
        }
        else {
            mAdapter3 = new Team1Adapter(getContext(), 1, 1);
        }
        mRecycler.setAdapter(mAdapter);
        mRecycler3.setAdapter(mAdapter3);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class Team1ViewHolder extends RecyclerView.ViewHolder {

        TextView batsman,run,balls,s4,s6,sr;

        public Team1ViewHolder(View itemView) {
            super(itemView);
            batsman=itemView.findViewById(R.id.batsman);
            run=itemView.findViewById(R.id.R);
            balls=itemView.findViewById(R.id.B);
            s4=itemView.findViewById(R.id.s4);
            s6=itemView.findViewById(R.id.s6);
            sr=itemView.findViewById(R.id.sr);
        }
    }


    private class Team1Adapter extends RecyclerView.Adapter<Team1ViewHolder> {


        private Context mContext;
        public JSONArray batting_order;
        public JSONArray bowling_order;
        protected JSONArray fo;
        public Integer mark;
        public Integer type;
        public Team1Adapter(final Context context,Integer count,Integer ty) {
            mContext = context;
            mark=count;
            type=ty;


            try {
               batting_order= jsonObject3.getJSONObject("innings").getJSONObject(order.get(mark)+"_1").getJSONArray("batting_order");
               bowling_order=jsonObject3.getJSONObject("innings").getJSONObject(order.get(mark)+"_1").getJSONArray("bowling_order");
               fo=jsonObject3.getJSONObject("innings").getJSONObject(order.get(mark)+"_1").getJSONArray("fall_of_wickets");
              String str="";
              if(type==0) {
                  for (int k = 0; k < fo.length(); k++) {
                      str = str + fo.getString(k) + "\n" + "\n";
                  }
                  fow.setText(str);
              }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @NonNull
        @Override
        public Team1ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.itemscorecard, viewGroup, false);

            return new Team1ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Team1ViewHolder friendViewHolder, int i) {
            try {
                if(type==0) {
                    JSONObject player = jsonObject3.getJSONObject("players").getJSONObject(batting_order.getString(i));
                    friendViewHolder.batsman.setText(player.getString("name"));
                    friendViewHolder.run.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("runs"));
                    friendViewHolder.balls.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("balls"));
                    friendViewHolder.s4.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("fours"));
                    friendViewHolder.s6.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("sixes"));
                    friendViewHolder.sr.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("strike_rate"));
                }
                else if(type==1) {
                    JSONObject bowler = jsonObject3.getJSONObject("players").getJSONObject(bowling_order.getString(i));
                    friendViewHolder.batsman.setText(bowler.getString("name"));
                    friendViewHolder.run.setText(bowler.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("bowling").getString("runs"));
                    friendViewHolder.balls.setText(bowler.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("bowling").getString("balls"));
                    friendViewHolder.s4.setText(bowler.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("bowling").getString("wickets"));
                    friendViewHolder.s6.setText(bowler.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("bowling").getString("maiden_overs"));
                    friendViewHolder.sr.setText(bowler.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("bowling").getString("economy"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        @Override
        public int getItemCount() {
            if (type == 0) {
                if (batting_order == null) {
                    return 0;
                }
                return batting_order.length();
            }
            else
            {  if (bowling_order == null) {
                return 0;
            }
                return bowling_order.length();
            }
        }



    }

}
