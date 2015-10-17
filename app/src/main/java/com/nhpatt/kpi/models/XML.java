package com.nhpatt.kpi.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Javier Gamarra
 */
@Root(name = "rss", strict = false)
public class XML {

    @Element
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
