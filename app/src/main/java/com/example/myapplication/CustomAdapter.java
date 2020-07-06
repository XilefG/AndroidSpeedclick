package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter<OnlineFinish> {
    Context context;
    ArrayList<OnlineFinish> data;
    boolean online;


    CustomAdapter(Context c, ArrayList<OnlineFinish> data, boolean online) {
        super(c, R.layout.listview, data);
        this.context = c;
        this.data = data;
        this.online = online;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DecimalFormat formatter = new DecimalFormat("#0.00");

        View row;
        if(online) {
            row = vi.inflate(R.layout.listview, parent, false);
        } else {
            row = vi.inflate(R.layout.listview_personal, parent, false);
        }
        int pos = position + 1;
        TextView positionText = row.findViewById(R.id.positionText);
        positionText.setText(pos + ".  ");
        if(online) {
            TextView username = row.findViewById(R.id.usernameText);
            username.setText(data.get(position).getUsername());
        }
        TextView score = row.findViewById(R.id.scoreText);
        score.setText(formatter.format(data.get(position).getTime()));
        TextView date = row.findViewById(R.id.dateText);
        date.setText(data.get(position).getDateSet());
        pos++;
        return row;
    }
}
