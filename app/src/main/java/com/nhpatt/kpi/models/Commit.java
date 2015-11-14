package com.nhpatt.kpi.models;

/**
 * @author Javier Gamarra
 */
public class Commit {

    public static final String TABLE_NAME = "COMMITS";
    public static final String WEEK = "WEEK";
    public static final String TOTAL = "TOTAL";

    private int total;
    private int week;

    public Commit(int week, int total) {
        this.week = week;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}
