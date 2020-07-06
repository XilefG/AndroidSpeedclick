package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DisplayGameOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game_options);
    }

    public void startBeginner(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle data = new Bundle();
        ArrayList<Integer> dimensions = new ArrayList<>();
        dimensions.add(15);
        dimensions.add(8);
        dimensions.add(8);
        dimensions.add(1);
        data.putIntegerArrayList("DIFFICULTY", dimensions);
        intent.putExtras(data);
        startActivity(intent);
    }

    public void startIntermediate(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle data = new Bundle();
        ArrayList<Integer> dimensions = new ArrayList<>();
        dimensions.add(50);
        dimensions.add(15);
        dimensions.add(15);
        dimensions.add(2);
        data.putIntegerArrayList("DIFFICULTY", dimensions);
        intent.putExtras(data);
        startActivity(intent);
    }

    public void startExpert(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle data = new Bundle();
        ArrayList<Integer> dimensions = new ArrayList<>();
        dimensions.add(99);
        dimensions.add(15);
        dimensions.add(30);
        dimensions.add(3);
        data.putIntegerArrayList("DIFFICULTY", dimensions);
        intent.putExtras(data);
        startActivity(intent);
    }

    public void customOptions(View v) {
        Intent intent = new Intent(this, ChooseCustomActivity.class);
        startActivity(intent);
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
