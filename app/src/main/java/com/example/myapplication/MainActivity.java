package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.views.grid.Grid;
public class MainActivity extends AppCompatActivity {


    private Chronometer timer;
    private TextView endStatus, endTime;
    LocalDatabase data;
    FrameLayout.LayoutParams layoutParams;
    Grid grid;
    Float scale = 1f;
    int screenWidth, screenHeight;
    int scrollX = 0;
    int scrollY = 0;
    boolean center = false;
    //ScaleGestureDetector SGD;
    int mines = 15;
    int width = 8;
    int height = 8;
    Context context = this;
    String gameType = "beginner";
    static int budgetTimer = 0;
    HorizontalScrollView scrollView;
    ScrollView vScroll;
    float startX = 0;
    float startY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new LocalDatabase(this);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            //gameType = intent.getExtras().getString("gameType");
            mines = intent.getExtras().getIntegerArrayList("DIFFICULTY").get(0);
            width = intent.getExtras().getIntegerArrayList("DIFFICULTY").get(1);
            height = intent.getExtras().getIntegerArrayList("DIFFICULTY").get(2);
            switch(intent.getExtras().getIntegerArrayList("DIFFICULTY").get(3)) {
                case 1:
                    gameType = "beginner";
                    break;
                case 2:
                    gameType = "intermediate";
                    break;
                case 3:
                    gameType = "expert";
                    break;
                case 4:
                    gameType = "custom";
                    break;
            }
        }
        data.setCurrentGameType(gameType);
        GameEngine.getInstance().setDimensions(mines, width, height, gameType);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);
        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.width = width*100;
        params.height = height*100;
        grid.setLayoutParams(params);

        timer = findViewById(R.id.timer);
        endStatus = findViewById(R.id.endStatus);
        endTime = findViewById(R.id.endTime);
        ProgressBar progress = findViewById(R.id.progress);
        GameEngine.getInstance().getWidgets(timer, progress);

        GameEngine.getInstance().createGrid(this, "standard");

        scrollView = findViewById(R.id.scrollView);
        vScroll = findViewById(R.id.verticalScroll);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        if(width*100 > screenWidth) {
            scrollX = ((width*100) - screenWidth)/2;
            center = true;
        }

        if(height*100 > screenHeight) {
            scrollY = ((height*100) - screenHeight)/2;
            center = true;
        }


        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                budgetTimer++;
                timer.setFormat(String.format("%03d", budgetTimer));
            }
        });

       // SGD = new ScaleGestureDetector(this, new ScaleListenter());

        SeekBar zoomBar = findViewById(R.id.zoomBar);
        zoomBar.setMax(190);
        zoomBar.setProgress(90);
        zoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scale = (progress+10)/100f;
                ViewGroup.LayoutParams params = grid.getLayoutParams();
                params.width = (int) (width*100*scale);
                params.height = (int) (height*100*scale);
                grid.setLayoutParams(params);
                GameEngine.getInstance().revalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(center) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(scrollX, scrollY);
                    vScroll.scrollTo(scrollX, scrollY);
                }
            });
        }

        vScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if(startX == 0 && startY == 0) {
                            startX = event.getX();
                            startY = event.getY();
                        }
                        scrollView.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        vScroll.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = event.getX();
                        startY = event.getY();
                        scrollView.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        vScroll.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        startX = 0;
                        startY = 0;
                        break;
                }
                return true;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if(startX == 0 && startY == 0) {
                            startX = event.getX();
                            startY = event.getY();
                        }
                        scrollView.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        vScroll.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = event.getX();
                        startY = event.getY();
                        scrollView.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        vScroll.scrollBy((int) (startX - event.getX()), (int) (startY - event.getY()));
                        startX = 0;
                        startY = 0;
                        break;
                }
                return true;
            }
        });

    }

    /*private class ScaleListenter extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale* detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            ViewGroup.LayoutParams params = grid.getLayoutParams();
            params.width = (int) (width*100*scale);
            grid.setLayoutParams(params);
            GameEngine.getInstance().resize(context);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }*/


    public static void resetBudgetTimer() {
        budgetTimer = 0;

    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        scrollView = findViewById(R.id.scrollView);

        return true;
    }

    public void restartGame(View v) {
        if(data.getConfirmRestart()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.confirm_restart_ask, null);
            Button selectBtn = view.findViewById(R.id.submitButton);
            Button cancelBtn = view.findViewById(R.id.cancelButton);
            builder.setView(view);
            final AlertDialog aD = builder.create();

            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameEngine.getInstance().resetGrid();
                    budgetTimer = 0;
                    timer.setText("000");
                    timer.stop();
                    aD.dismiss();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aD.dismiss();
                }
            });
            aD.show();
        } else {
            GameEngine.getInstance().resetGrid();
            budgetTimer = 0;
            timer.setText("000");
            timer.stop();
        }
    }

    public void goHome(View v) {
        GameEngine.getInstance().storeGame();
        budgetTimer = 0;
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    public void goSettings(View v) {
        budgetTimer = 0;
        GameEngine.getInstance().storeGame();
        Intent intent = new Intent(this, ShowSettings.class);
        intent.putExtra("fromGame", true);
        startActivity(intent);
    }

    public void gameSelect(View v) {
        budgetTimer = 0;
        GameEngine.getInstance().storeGame();
        Intent intent = new Intent(this, DisplayGameOptions.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        GameEngine.getInstance().storeGame();
        super.onBackPressed();
    }
}
