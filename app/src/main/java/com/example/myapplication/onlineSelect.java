package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class onlineSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_select);
    }

    public void findMatch(View v) {
        Intent intent = new Intent(this, findMatch.class);
        startActivity(intent);
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
