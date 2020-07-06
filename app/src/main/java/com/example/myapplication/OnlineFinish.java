package com.example.myapplication;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class OnlineFinish {

    double time;
    String username;
    String dateSet;

    public OnlineFinish(){}

    public OnlineFinish(double time, String dateSet, String username) {
        this.time = time;
        this.dateSet = dateSet;
        this.username = username;
    }


    public double getTime() {
        return time;
    }

    public String getDateSet() {
        return dateSet;
    }

    public String getUsername() {
        return username;
    }
}

class scoreComparator implements Comparator<OnlineFinish> {
    public int compare(OnlineFinish score1, OnlineFinish score2) {
        return Double.compare(score1.getTime(), score2.getTime());
    }
}