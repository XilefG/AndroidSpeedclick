package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class onlineIntermediateTab extends Fragment {

    ArrayList<OnlineFinish> intermediateList;
    ListView intermediateHighscores;

    public static onlineIntermediateTab newInstance(int position) {
        onlineIntermediateTab fragment = new onlineIntermediateTab();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public onlineIntermediateTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        intermediateList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_intermediate_online_tab, container, false);
        intermediateHighscores = view.findViewById(R.id.onlineIntermediateHighscores);
        DatabaseReference highscores = FirebaseDatabase.getInstance().getReference("intermediate");
        Query query = highscores.limitToFirst(50);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getActivity() != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        double getTime = data.child("time").getValue(Double.class);
                        String getUsername = data.child("username").getValue(String.class);
                        String getDate = data.child("dateSet").getValue(String.class);
                        OnlineFinish getData = new OnlineFinish(getTime, getDate, getUsername);
                        intermediateList.add(getData);
                    }
                    Collections.sort(intermediateList, new scoreComparator());
                    CustomAdapter intermediateAdapter = new CustomAdapter(getActivity(), intermediateList, true);
                    intermediateHighscores.setAdapter(intermediateAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        return view;
    }



}

