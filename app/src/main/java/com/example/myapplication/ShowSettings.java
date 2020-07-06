package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

public class ShowSettings extends AppCompatActivity {

    LocalDatabase data;
    public boolean fromGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            fromGame = intent.getExtras().getBoolean("fromGame");
        }

        final LinearLayout buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setVisibility(View.GONE);

        data = new LocalDatabase(this);
        RadioGroup radioGroup = findViewById(R.id.radio);
        RadioButton beginnerRadio = findViewById(R.id.beginnerRadio);
        RadioButton intermediateRadio = findViewById(R.id.intermediateRadio);
        RadioButton expertRadio = findViewById(R.id.expertRadio);
        RadioButton customRadio = findViewById(R.id.customRadio);

        if(data.getGameType().equals("beginner")) {
            beginnerRadio.toggle();
        } else if(data.getGameType().equals("intermediate")) {
            intermediateRadio.toggle();
        } else if(data.getGameType().equals("expert")) {
            expertRadio.toggle();
        } else {
            customRadio.toggle();
        }

        final Switch startSwitch = findViewById(R.id.startSwitch);
        if(data.getDefaultStartBool()) {
            startSwitch.setChecked(true);
            buttonSelect.setVisibility(View.VISIBLE);
        }
        startSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    data.setDefaultStartBool(1);
                    data.setDefaultStart(15, 8, 8, "beginner");
                    expand(buttonSelect);
                } else {
                    data.setDefaultStartBool(0);
                    collapse(buttonSelect);
                }
            }
        });

        final Spinner themeSpinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setSelection(data.getStylePos());
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                data.setStyle(themeSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        final Switch confirmSwitch = findViewById(R.id.confirmSwitch);
        if(data.getConfirmRestart()) {
            confirmSwitch.setChecked(true);
        }

        confirmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                data.setConfirmRestartBool(1);
            } else {
                data.setConfirmRestartBool(0);
            }
        }
        });

        final Switch saveSwitch = findViewById(R.id.saveSwitch);
        if(data.getSaveGame()) {
            saveSwitch.setChecked(true);
        }
        saveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    data.setSaveGame(1);
                } else {
                    data.setSaveGame(0);
                }
            }
        });
    }

    public void beginnerDefault(View v) {
        data.setDefaultStart(15, 8, 8, "beginner");
    }

    public void intermediateDefault(View v) {
        data.setDefaultStart(50, 15, 15, "intermediate");
    }

    public void expertDefault(View v) {
        data.setDefaultStart(99, 15, 30, "expert");
    }

    public void customDefault(View v) {
        Intent intent = new Intent(this, ChooseCustomActivity.class);
        intent.putExtra("fromSettings", true);
        startActivity(intent);
    }

    public void showAbout(View v) {
        Intent intent = new Intent(this, ShowAbout.class);
        startActivity(intent);
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void goBack(View v) {
        Intent intent;
        if(fromGame) {
            data.setFromSettings(1);
            intent = new Intent(this, MainActivity.class);
            //intent.putExtra("GAMETYPE", data.getGameType());
        } else {
            intent = new Intent(this, HomePageActivity.class);
        }
        startActivity(intent);
    }

    public void showHelp(View v) {
        Intent intent = new Intent(this, ShowHelp.class);
        startActivity(intent);
    }
}
