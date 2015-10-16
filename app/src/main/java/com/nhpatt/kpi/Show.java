package com.nhpatt.kpi;

import java.util.Date;

/**
 * @author Javier Gamarra
 */
public class Show {

    public String name;
    public Date date;

    public Show(String name) {
        this.name = name;
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
