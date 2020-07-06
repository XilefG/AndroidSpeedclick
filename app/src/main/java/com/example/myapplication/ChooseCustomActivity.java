package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseCustomActivity extends AppCompatActivity {

    private TextView widthNo, heightNo, mineNo;
    private SeekBar widthBar, heightBar, mineBar;
    private int max = 45;
    private int min = 5;
    private int mineValue;
    private int widthValue = 5;
    private int heightValue = 5;
    private int mineMax = 23;
    private int mineProgress = 0;
    public boolean fromSettings;
    public LocalDatabase data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_custom);

        data = new LocalDatabase(this);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            fromSettings = intent.getExtras().getBoolean("fromSettings");
        }

        widthNo = findViewById(R.id.widthNo);
        heightNo = findViewById(R.id.heightNo);
        mineNo = findViewById(R.id.mineNo);
        widthBar = findViewById(R.id.widthBar);
        heightBar = findViewById(R.id.heightBar);
        mineBar = findViewById(R.id.mineBar);

        widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                widthValue = Math.round((progress*max)/100)+min;
                widthNo.setText(String.valueOf(widthValue));
                mineMax = Math.round(widthValue*heightValue-(widthValue*heightValue/10));
                mineValue = Math.round((mineProgress*mineMax)/100);
                mineNo.setText(String.valueOf(mineValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightValue = Math.round((progress*max)/100)+min;
                heightNo.setText(String.valueOf(heightValue));
                mineMax = Math.round(widthValue*heightValue-(widthValue*heightValue/10));
                mineValue = Math.round((mineProgress*mineMax)/100);
                mineNo.setText(String.valueOf(mineValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mineBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mineProgress = progress;
                mineMax = Math.round(widthValue*heightValue-(widthValue*heightValue/10));
                mineValue = Math.round((progress*mineMax)/100);
                mineNo.setText(String.valueOf(mineValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void startCustom(View v) {
        Intent intent;

        if(fromSettings) {
            intent = new Intent(this, ShowSettings.class);
            data.setDefaultStart(mineValue, widthValue, heightValue, "custom");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(this, MainActivity.class);
            Bundle data = new Bundle();
            ArrayList<Integer> dimensions = new ArrayList<>();
            dimensions.add(mineValue);
            dimensions.add(widthValue);
            dimensions.add(heightValue);
            dimensions.add(4);
            data.putIntegerArrayList("DIFFICULTY", dimensions);
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    public void gameSelect(View v) {
        Intent intent;

        if(fromSettings) {
            intent = new Intent(this, ShowSettings.class);
            data.setDefaultStart(mineValue, widthValue, heightValue, "custom");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(this, DisplayGameOptions.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);

    }

}
