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


public class intermediateTab extends Fragment {

    ArrayList<OnlineFinish> intermediateList;

    public static intermediateTab newInstance(int position) {
        intermediateTab fragment = new intermediateTab();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public intermediateTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        intermediateList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_intermediate_tab, container, false);
        ListView intermediateHighscores = view.findViewById(R.id.intermediateHighscores);
        LocalDatabase data = new LocalDatabase(getActivity());

        HashMap<Double, String> timeList = data.getTimes("intermediate");
        for(Double i : timeList.keySet()) {
            OnlineFinish timeData = new OnlineFinish(i, timeList.get(i), data.getUsername());
            intermediateList.add(timeData);
        }
        Collections.sort(intermediateList, new scoreComparator());
        CustomAdapter intermediateAdapter = new CustomAdapter(getActivity(), intermediateList, false);
        intermediateHighscores.setAdapter(intermediateAdapter);

        return view;
    }

}
