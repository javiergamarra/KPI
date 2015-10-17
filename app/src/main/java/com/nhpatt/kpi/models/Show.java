package com.nhpatt.kpi.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * @author Javier Gamarra
 */
@Root(name = "item", strict = false)
public class Show {

    @Element
    public String title;
    public Date date;

    public Show() {
        super();
    }

    public Show(String title) {
        this.title = title;
        this.date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
