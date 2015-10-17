package com.nhpatt.kpi;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Javier Gamarra
 */
@Root(name = "rss", strict = false)
public class XML {

    @Element
    private Channel channel;
}
