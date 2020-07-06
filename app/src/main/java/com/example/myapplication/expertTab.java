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
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class expertTab extends Fragment {

    ArrayList<OnlineFinish> expertList;

    public static expertTab newInstance(int position) {
        expertTab fragment = new expertTab();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }


    public expertTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        expertList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_expert_tab, container, false);
        ListView expertHighscores = view.findViewById(R.id.expertHighscores);
        LocalDatabase data = new LocalDatabase(getActivity());

        HashMap<Double, String> timeList = data.getTimes("expert");
        for(Double i : timeList.keySet()) {
            OnlineFinish timeData = new OnlineFinish(i, timeList.get(i), data.getUsername());
            expertList.add(timeData);
        }
        Collections.sort(expertList, new scoreComparator());
        CustomAdapter expertAdapter = new CustomAdapter(getActivity(), expertList, false);
        expertHighscores.setAdapter(expertAdapter);

        return view;
    }

}
