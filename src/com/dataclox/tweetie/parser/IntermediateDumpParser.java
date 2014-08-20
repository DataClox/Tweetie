package com.dataclox.tweetie.parser;

import com.dataclox.tweetie.main.TweeStruct;
import com.dataclox.tweetie.main.Tweet;
import com.dataclox.tweetie.main.TweetTimeStamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Created by devilo on 19/8/14.
 */
public class IntermediateDumpParser {

    private TweeStruct tweeStruct       = null;
    private String intermediateDumpPath = null;
    private File intermediateFile       = null;

    public IntermediateDumpParser(String interDump) {

        this.intermediateDumpPath = interDump;
        tweeStruct = TweeStruct.getInstance();
        intermediateFile = new File(this.intermediateDumpPath);
    }


    public void createTweetMap() throws IOException {

        String line = null;

        BufferedReader intermediateDumpReader = new BufferedReader(new FileReader(intermediateFile));

        int count = 0;

        while((line = intermediateDumpReader.readLine()) != null) {

            Tweet t = new Tweet();

            t.setTweetId(new Long(line.substring(1)));

            line = intermediateDumpReader.readLine();
            t.setTweetTimestamp(new TweetTimeStamp(line.substring(1)));

            line = intermediateDumpReader.readLine();
            t.setTweetText(line.substring(1));

            line = intermediateDumpReader.readLine();
            t.setTweetUserId(line.substring(1));

            line = intermediateDumpReader.readLine();

            if( line.equals("$null"))
                line = "$0";

            t.setTweetInReplyToStatusId(new Long(line.substring(1)));

            line = intermediateDumpReader.readLine();
            tweeStruct.insert(t);
            count++;

        }

        System.out.println("insertion count : " + count);
        
        intermediateDumpReader.close();

        postProcess();

    }

    private void postProcess() {

        HashSet<Long> roots = tweeStruct.getRoots();
        HashMap<Long, Tweet> tweetMap = tweeStruct.getTweetMap();
        TreeMap<Long, Long> parentChildMap = tweeStruct.getParentChildMap();    //Child - Parent

        TreeMap<Long, LinkedHashSet<Long>> adjacencyList = new TreeMap<Long, LinkedHashSet<Long>>();

        HashSet<Long> toBeRemoved = new HashSet<Long>();


        /* Removing all children whose parents do not exist in the data-set */
        for( Long childId : parentChildMap.keySet() ) {

            Long parentId = parentChildMap.get(childId);

            if(parentId != 0 && !tweetMap.containsKey(parentId)) {
                toBeRemoved.add(childId);
            }

        }

        System.out.println("Whose parent were not found in the datase : " + toBeRemoved.size());

        for( Long id: toBeRemoved ) {
            parentChildMap.remove(id);
        }
        toBeRemoved.clear();



        int count = 0;
        for( Long childId : parentChildMap.keySet() ) {

            Long parentId = parentChildMap.get(childId);

            if( parentId == 0 ) {
                adjacencyList.put(childId, new LinkedHashSet<Long>());
            }
            else {
                if( adjacencyList.containsKey(parentId) )
                    adjacencyList.get(parentId).add(childId);
                else
                    count++;
            }

        }

        System.out.println("Unsorted count : " + count);
        System.out.println("Total roots : " + adjacencyList.keySet().size());

        count = 0;
        int max = 0;

        for( Long rootId : adjacencyList.keySet()) {
            if( adjacencyList.get(rootId).size() > 0 )
                count++;

            if( adjacencyList.get(rootId).size() > max )
                max = adjacencyList.get(rootId).size();
        }
        System.out.println("Total conversations = " + count);
        System.out.println("Max length conversation = " + max);

    }
}
