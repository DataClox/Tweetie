package com.dataclox.tweetie.main;

import java.util.HashMap;

/**
 * Created by devilo on 19/8/14.
 */
public class TweeStruct {

    /* This is a Singleton class */

    private static TweeStruct instance = null;
    private HashMap<Long,Tweet> tweetMap = null;
    private TweeTree tweeTree = null;

    protected TweeStruct() {
        tweetMap = new HashMap<Long, Tweet>();
        tweeTree = new TweeTree();
    }

    public static TweeStruct getInstance() {

        if( instance == null )
            instance = new TweeStruct();

        return instance;
    }

    public void insert( Tweet tweet ) {
        tweetMap.put(tweet.getTweetId(), tweet);
        tweeTree.add(tweet.getTweetId() , tweet.getTweetInReplyToStatusId());
    }


    public TweeTree getTweeTree() {
        return this.tweeTree;
    }

    public int getSize() {
        return tweetMap.size();
    }

}
