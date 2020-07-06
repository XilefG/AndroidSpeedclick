package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ShowProfile extends AppCompatActivity {

    TextView username, beginnerRecord, beginnerAverage, beginnerPlayed, beginnerWinPercent, userLevel;
    TextView intermediateRecord, intermediateAverage, intermediatePlayed, intermediateWinPercent;
    TextView expertRecord, expertAverage, expertPlayed, expertWinPercent;
    LocalDatabase data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);


        data = new LocalDatabase(this);

        DecimalFormat formatter = new DecimalFormat("#0.00");
        DecimalFormat levelFormatter = new DecimalFormat("#0");

        username = findViewById(R.id.username);
        username.setText(data.getUsername());

        userLevel = findViewById(R.id.userLevel);
        userLevel.setText("" + levelFormatter.format(Math.floor(data.getLevel()/10)));

        beginnerRecord = findViewById(R.id.beginnerRecord);
        beginnerAverage = findViewById(R.id.beginnerAverage);
        beginnerPlayed = findViewById(R.id.beginnerPlayed);
        beginnerWinPercent = findViewById(R.id.beginnerWinPercent);
        beginnerRecord.setText(formatter.format(data.getHighscore("beginner")));
        beginnerAverage.setText(formatter.format(data.getAverageTime("beginner")));
        beginnerPlayed.setText("" + data.getTotalPlayed("beginner"));
        beginnerWinPercent.setText((data.getWinPercent("beginner")*100) + "%");

        intermediateRecord = findViewById(R.id.intermediateRecord);
        intermediateAverage = findViewById(R.id.intermediateAverage);
        intermediatePlayed = findViewById(R.id.intermediatePlayed);
        intermediateWinPercent = findViewById(R.id.intermediateWinPercent);
        intermediateRecord.setText(formatter.format(data.getHighscore("intermediate")));
        intermediateAverage.setText(formatter.format(data.getAverageTime("intermediate")));
        intermediatePlayed.setText("" + (data.getTotalPlayed("intermediate")));
        intermediateWinPercent.setText((data.getWinPercent("intermediate")*100) + "%");

        expertRecord = findViewById(R.id.expertRecord);
        expertAverage = findViewById(R.id.expertAverage);
        expertPlayed = findViewById(R.id.expertPlayed);
        expertWinPercent = findViewById(R.id.expertWinPercent);
        expertRecord.setText(formatter.format(data.getHighscore("expert")));
        expertAverage.setText(formatter.format(data.getAverageTime("expert")));
        expertPlayed.setText("" + (data.getTotalPlayed("expert")));
        expertWinPercent.setText((data.getWinPercent("expert")*100) + "%");

    }

    public void goHome(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
