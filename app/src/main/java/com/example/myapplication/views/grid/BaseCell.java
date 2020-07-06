package com.example.myapplication.views.grid;

import android.content.Context;
import android.view.View;

import com.example.myapplication.GameEngine;

public abstract class BaseCell extends View {

    private int value;
    private boolean isMine;
    private boolean isClicked;
    private boolean isRevealed;

    private int x, y;
    private int position;

    public BaseCell(Context context) {
        super(context);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        isMine = false;
        isClicked = false;
        isRevealed = false;

        if(value == -1) {
            isMine = true;
        }

        this.value = value;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void defuseMine() {isMine = false;}

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
        invalidate();
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked() {
        this.isClicked = true;
        this.isRevealed = true;

        invalidate();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.position = y * GameEngine.getInstance().getWidth() + x;

        invalidate();
    }
}
