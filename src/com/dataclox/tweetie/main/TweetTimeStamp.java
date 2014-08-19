package com.dataclox.tweetie.main;

/**
 * Created by devilo on 19/8/14.
 */
public class TweetTimeStamp {

    private String day = null;
    private String month = null;
    private int date = 0;
    private int hh = 0 , mm = 0 , ss = 0;
    private int year = 0;

    private String timestampStr = null;

    private boolean isNull = false;

    public TweetTimeStamp( String t ) {

        timestampStr = t;

        String[] str = t.split(" ");

        if( str.length != 6 ) {
            isNull = true;
            return;
        }

        day = str[0];
        month = str[1];
        date = Integer.parseInt(str[2]);

        String[] time = str[3].split(":");

        hh = Integer.parseInt(time[0]);
        mm = Integer.parseInt(time[1]);
        ss = Integer.parseInt(time[2]);


        year = Integer.parseInt(str[5]);

    }

    public boolean isNull() {
        return isNull;
    }

}
