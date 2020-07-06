package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowHighscores extends AppCompatActivity {

    ArrayList<Double> beginnerList = new ArrayList<Double>();
    ArrayList<Double> intermediateList = new ArrayList<Double>();
    ArrayList<Double> expertList = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_highscores);

       /* ListView beginnerHighscores = findViewById(R.id.beginnerHighscores);
        ListView intermediateHighscores = findViewById(R.id.intermediateHighscores);
        ListView expertHighscores = findViewById(R.id.expertHighscores);

        highscoreDatabase highscores = new highscoreDatabase(this);
        Cursor beginnerData = highscores.getData("beginner");
        while(beginnerData.moveToNext()) {
            beginnerList.add(beginnerData.getDouble(0));
        }
        Cursor intermediateData = highscores.getData("intermediate");
        while(intermediateData.moveToNext()) {
            intermediateList.add(intermediateData.getDouble(0));
        }
        Cursor expertData = highscores.getData("expert");
        while(expertData.moveToNext()) {
            expertList.add(expertData.getDouble(0));
        }

        CustomAdapter beginnerAdapter = new CustomAdapter(this, beginnerList);
        beginnerHighscores.setAdapter(beginnerAdapter);
        CustomAdapter intermediateAdapter = new CustomAdapter(this, intermediateList);
        intermediateHighscores.setAdapter(intermediateAdapter);
        CustomAdapter expertAdapter = new CustomAdapter(this, expertList);
        expertHighscores.setAdapter(expertAdapter);

        TabHost tabs = findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tab1");
        spec.setContent(R.id.beginnerHighscores);
        spec.setIndicator("BEGINNER");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tab2");
        spec.setContent(R.id.intermediateHighscores);
        spec.setIndicator("INTERMEDIATE");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tab3");
        spec.setContent(R.id.expertHighscores);
        spec.setIndicator("EXPERT");
        tabs.addTab(spec);

    }
}

class CustomAdapter extends ArrayAdapter<Double> {
    Context context;
    ArrayList<Double> data;


    CustomAdapter(Context c, ArrayList<Double> data) {
        super(c, R.layout.listview, data);
        this.context = c;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DecimalFormat formatter = new DecimalFormat("#0.00");

        View row = vi.inflate(R.layout.listview, parent, false);
        TextView dataRow = row.findViewById(R.id.textView);
        int pos = position+1;
        dataRow.setText(+pos + ".   " + formatter.format(data.get(position)));
        pos++;
        return row;
    }

  */
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
