package com.nhpatt.kpi.models;

import java.util.Date;

/**
 * @author Javier Gamarra
 */
public class Film implements TitleAndDate {

    private String title;
    private Date date;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
