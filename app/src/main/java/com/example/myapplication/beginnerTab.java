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
import java.util.HashMap;


public class beginnerTab extends Fragment {

    ArrayList<OnlineFinish> beginnerList;

    public static beginnerTab newInstance(int position) {
        beginnerTab fragment = new beginnerTab();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public beginnerTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         beginnerList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_beginner_tab, container, false);
        ListView beginnerHighscores = view.findViewById(R.id.beginnerHighscores);
        LocalDatabase data = new LocalDatabase(getActivity());

        HashMap<Double, String> timeList = data.getTimes("beginner");
        for(Double i : timeList.keySet()) {
            OnlineFinish timeData = new OnlineFinish(i, timeList.get(i), data.getUsername());
            beginnerList.add(timeData);
        }
        Collections.sort(beginnerList, new scoreComparator());
        CustomAdapter beginnerAdapter = new CustomAdapter(getActivity(), beginnerList, false);
        beginnerHighscores.setAdapter(beginnerAdapter);

        return view;
    }



}

