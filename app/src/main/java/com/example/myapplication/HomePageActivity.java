package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    LocalDatabase data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //deleteDatabase("appData");
        data = new LocalDatabase(this);
        data.addStatus();
        data.addSavedStatus();
        if(data.getUsername().equals("Invalid")) {
            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);
        }
    }

    public void select(View v) {
        Intent intent;

        if(data.getDefaultStartBool()) {
            if(data.getGameType().equals("beginner")) {
                intent = new Intent(this, MainActivity.class);
                Bundle data = new Bundle();
                ArrayList<Integer> dimensions = new ArrayList<>();
                dimensions.add(15);
                dimensions.add(8);
                dimensions.add(8);
                dimensions.add(1);
                data.putIntegerArrayList("DIFFICULTY", dimensions);
                intent.putExtras(data);
            } else if(data.getGameType().equals("intermediate")) {
                intent = new Intent(this, MainActivity.class);
                Bundle data = new Bundle();
                ArrayList<Integer> dimensions = new ArrayList<>();
                dimensions.add(50);
                dimensions.add(15);
                dimensions.add(15);
                dimensions.add(2);
                data.putIntegerArrayList("DIFFICULTY", dimensions);
                intent.putExtras(data);
            } else if(data.getGameType().equals("expert")) {
                intent = new Intent(this, MainActivity.class);
                Bundle data = new Bundle();
                ArrayList<Integer> dimensions = new ArrayList<>();
                dimensions.add(99);
                dimensions.add(15);
                dimensions.add(30);
                dimensions.add(3);
                data.putIntegerArrayList("DIFFICULTY", dimensions);
                intent.putExtras(data);
            } else {
                intent = new Intent(this, MainActivity.class);
                Bundle passData = new Bundle();
                ArrayList<Integer> dimensions = new ArrayList<>();
                dimensions.add(data.getDefaultMines());
                dimensions.add(data.getDefaultX());
                dimensions.add(data.getDefaultY());
                dimensions.add(4);
                passData.putIntegerArrayList("DIFFICULTY", dimensions);
                intent.putExtras(passData);
            }
        } else {
            intent = new Intent(this, DisplayGameOptions.class);
        }
        startActivity(intent);
    }

    public void goHighscores(View v) {
        Intent intent = new Intent(this, ShowHighscores2.class);
        startActivity(intent);
    }

    public void goSettings(View v) {
        Intent intent = new Intent(this, ShowSettings.class);
        startActivity(intent);
    }

    public void showOnline(View v) {
        Intent intent = new Intent(this, onlineSelect.class);
        startActivity(intent);
    }

    public void showPuzzle(View v) {
        Intent intent = new Intent(this, PuzzleActivity.class);
        startActivity(intent);
    }

    public void showProfile(View v) {
        final Intent intent = new Intent(this, ShowProfile.class);
        startActivity(intent);
    }
}
