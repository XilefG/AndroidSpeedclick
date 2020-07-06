package com.example.myapplication.views.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.myapplication.GameEngine;
import com.example.myapplication.R;

public class Cell extends BaseCell implements View.OnClickListener {

    public Cell(Context context, int x, int y) {
        super(context);

        setPosition(x, y);

        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isClicked()) {
            if(isMine()) {
                drawButton(canvas, R.drawable.badbutton);
            } else {
                drawButton(canvas, R.drawable.button2);
            }
        } else if(isRevealed()){
            switch(getValue()) {
                case -1:
                    drawButton(canvas, R.drawable.badbutton);
                    break;
                case -2:
                    drawButton(canvas, R.drawable.blueupbutton);
                    break;
                case -3:
                    drawButton(canvas, R.drawable.blueacrossbutton);
                    break;
                case -4:
                    drawButton(canvas, R.drawable.blackbutton);
                    break;
                case -5:
                    drawButton(canvas, R.drawable.orangebutton);
                    break;
                case -6:
                    drawButton(canvas, R.drawable.darkgreenbutton);
                    break;
                default:
                    drawButton(canvas, R.drawable.goodbutton);
            }
        } else {
            drawButton(canvas, R.drawable.buttonblue);
        }
    }

    private void drawButton(Canvas canvas, int type) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), type);
        drawable.setBounds(0,0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    @Override
    public void onClick(View v) {
        GameEngine.getInstance().clickCell(getXPos(), getYPos(), true, true);
    }
}
