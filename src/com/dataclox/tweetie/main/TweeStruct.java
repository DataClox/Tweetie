package com.dataclox.tweetie.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Created by devilo on 19/8/14.
 */
public class TweeStruct {

    /* This is a Singleton class */

    private static TweeStruct instance = null;

    private HashMap<Long,Tweet> tweetMap = null;
    private TreeMap<Long,Long> parentChildMap = null;
    private HashSet<Long> roots = null;

    TreeMap<Long, LinkedHashSet<Long>> adjacencyList = null;

    protected TweeStruct() {

        roots = new HashSet<Long>();
        tweetMap = new HashMap<Long, Tweet>();
        parentChildMap = new TreeMap<Long, Long>();
        adjacencyList = new TreeMap<Long, LinkedHashSet<Long>>();
    }

    public static TweeStruct getInstance() {

        if( instance == null )
            instance = new TweeStruct();

        return instance;
    }

    public void insert( Tweet tweet ) {

        tweetMap.put(tweet.getTweetId(), tweet);
        parentChildMap.put(tweet.getTweetId(), tweet.getTweetInReplyToStatusId());

        if( tweet.getTweetInReplyToStatusId() == 0 ) {
            roots.add(tweet.getTweetId());
        }

    }

    public int getSize() {
        return tweetMap.size();
    }

    public HashMap<Long, Tweet> getTweetMap() {
        return tweetMap;
    }

    public TreeMap<Long, Long> getParentChildMap() {
        return parentChildMap;
    }

    public HashSet<Long> getRoots() {
        return roots;
    }

    public TreeMap<Long, LinkedHashSet<Long>> getAdjacencyList() {
        return adjacencyList;
    }
}
