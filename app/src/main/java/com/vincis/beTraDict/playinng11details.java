package com.vincis.beTraDict;


import android.content.Context;
import android.content.Intent;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class playinng11details extends Fragment {
String[] response;

    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler2;
    private AnsweredAdapter mAdapter2;
    private LinearLayoutManager mManager2;
    String count;

    public playinng11details() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        response=getArguments().getStringArray("data");
        count= response[1];
        View view= inflater.inflate(R.layout.fragment_playinng11details, container, false);
        mRecycler = view.findViewById(R.id.playerlist);
        mRecycler2 = view.findViewById(R.id.playerlist2);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler2.setHasFixedSize(true);
        mRecycler2.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setLayoutManager(mManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AnsweredAdapter(getContext(),count);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlayer,tvPlayerRole;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
            tvPlayer=itemView.findViewById(R.id.tvPlayer);
            tvPlayerRole=itemView.findViewById(R.id.tvPlayerRole);
        }
    }


    private class AnsweredAdapter extends RecyclerView.Adapter<AnsweredViewHolder> {
        public JSONObject jsonObj,jsonObj2,jsonObject3;

        private Context mContext;
        public  JSONArray playing11;
        public String mark;
        public AnsweredAdapter(final Context context,String count) {
            mContext = context;
            mark=count;



            try {
               jsonObj=new JSONObject(response[0]);
                jsonObj2=jsonObj.getJSONObject("data");
                 jsonObject3=jsonObj2.getJSONObject("card");
                playing11=jsonObject3.getJSONObject("teams").getJSONObject(mark).getJSONObject("match").getJSONArray("playing_xi");
                if(jsonObject3.getJSONObject("teams").getJSONObject(mark).getJSONObject("match").getString("playing_xi").equals("null"))
                {
                    playing11=jsonObject3.getJSONObject("teams").getJSONObject(mark).getJSONObject("match").getJSONArray("players");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @NonNull
        @Override
        public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.itemplayerss, viewGroup, false);

            return new AnsweredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsweredViewHolder friendViewHolder, int i) {

                try {
                    friendViewHolder.tvPlayer.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11.getString(i)).getString("name"));
                    friendViewHolder.tvPlayerRole.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11.getString(i)).getString("seasonal_role"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }





        }



        @Override
        public int getItemCount()
        {
            if(playing11==null)
            {


                try {
                    playing11=jsonObject3.getJSONObject("teams").getJSONObject(mark).getJSONObject("match").getJSONArray("players");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return playing11.length();
        }


    }

}
