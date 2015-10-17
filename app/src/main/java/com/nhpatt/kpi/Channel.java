package com.nhpatt.kpi;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Javier Gamarra
 */
@Root(name = "channel", strict = false)
public class Channel {

    @Element(required = false)
    public String title;

    @ElementList(entry = "item", inline = true)
    public List<Show> shows;

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
