package com.dataclox.tweetie.main;

import java.util.*;

/**
 * Created by devilo on 19/8/14.
 */
public class TweeTree {

    //private HashMap<Long, LinkedHashSet<Long>> parentToChildUnclassified = null;
    private TreeMap<Long, LinkedHashSet<Long>> root = null;

    public TweeTree() {

        root = new TreeMap<Long, LinkedHashSet<Long>>();
        //parentToChildUnclassified = new HashMap<Long, LinkedHashSet<Long>>();

    }

    public void add( Long child, Long parent ) {

        if( parent == 0 ) {
            root.put(child, new LinkedHashSet<Long>());
            return;
        }

        if( root.containsKey(parent) )
            root.get(parent).add(child);

        /*else {
            if( !parentToChildUnclassified.containsKey(parent))
                parentToChildUnclassified.put(parent, new LinkedHashSet<Long>());

            parentToChildUnclassified.get(parent).add(child);
        }*/

    }

}
