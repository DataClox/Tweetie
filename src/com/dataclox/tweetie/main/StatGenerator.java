package com.dataclox.tweetie.main;

import com.dataclox.tweetie.config.IndexConfig;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by devilo on 20/8/14.
 */
public class StatGenerator {

    private HashMap<Long,Tweet> tweetMap = TweeStruct.getInstance().getTweetMap();

    private HashSet<Long> roots = TweeStruct.getInstance().getRoots();
    private TreeMap<Long, Long> tweetIdVsUserId = TweeStruct.getInstance().getTweetIdVsUserId();
    private TreeMap<Long, LinkedHashSet<Long>> adjacencyList = TweeStruct.getInstance().getAdjacencyList();

    private File statFolder = null;
    private File convFile   = null;
    private File numUsersVsFreqFile   = null;
    private File convLengthVsFreqFile   = null;
    private File minutesVsFreqFile   = null;

    private BufferedWriter convFileWriter = null;
    private BufferedWriter numUsersVsFreqFileWriter = null;
    private BufferedWriter conversationLengthVsFreqFileWriter = null;
    private BufferedWriter minutesVsFreqFileWriter = null;


    public void generateConversations() throws IOException {
        validate();
    }


    public void generateMinutesVsFreq() throws IOException {

        minutesVsFreqFileWriter = new BufferedWriter(new FileWriter(minutesVsFreqFile));

        TreeMap<Long, Long> minutesVsFrequency = new TreeMap<Long, Long>();

        for( Long rootId : roots ) {

            Long minId = Long.MAX_VALUE, maxId = Long.MIN_VALUE;

            Queue<Long> q = new LinkedList<Long>();
            q.add(rootId);

            while ( !q.isEmpty() ) {

                Long id = q.poll();

                if( id < minId )
                    minId = id;

                if( id > maxId )
                    maxId = id;

                if( adjacencyList.containsKey(id)) {
                    for (Long childId : adjacencyList.get(id)) {
                        q.add(childId);
                    }
                }
            }

            long minutes = (tweetMap.get(maxId).getTweetTimestamp().getTime() - tweetMap.get(minId).getTweetTimestamp().getTime())/6000;

            if( !minutesVsFrequency.containsKey(minutes) )
                minutesVsFrequency.put(minutes, 1L);
            else {
                long freq = minutesVsFrequency.get(minutes);
                minutesVsFrequency.put(minutes, freq + 1);
            }

        }

        for( Long numUsers: minutesVsFrequency.keySet() ) {
            minutesVsFreqFileWriter.write(numUsers + "-" + minutesVsFrequency.get(numUsers) + '\n');
        }

        minutesVsFreqFileWriter.close();


    }

    public void generateNumOfDistinctUsersVsFreq() throws IOException {

        numUsersVsFreqFileWriter = new BufferedWriter(new FileWriter(numUsersVsFreqFile));

        HashSet<Long> users = new HashSet<Long>();
        TreeMap<Integer, Integer> numberOfDistinctUserVsFrequency = new TreeMap<Integer, Integer>();

        for( Long rootId : roots ) {

            users.clear();

            /*for( Long childId : adjacencyList.get(rootId) ) {
                users.add(tweetIdVsUserId.get(childId));
            }*/

            Queue<Long> q = new LinkedList<Long>();
            q.add(rootId);

            while ( !q.isEmpty() ) {

                Long id = q.poll();
                users.add(tweetIdVsUserId.get(id));

                if( adjacencyList.containsKey(id)) {
                    for (Long childId : adjacencyList.get(id)) {
                        q.add(childId);
                    }
                }
            }


            if( !numberOfDistinctUserVsFrequency.containsKey(users.size()) )
                numberOfDistinctUserVsFrequency.put(users.size(), 1);
            else {
                int freq = numberOfDistinctUserVsFrequency.get(users.size());
                numberOfDistinctUserVsFrequency.put(users.size(), freq + 1);
            }

        }

        for( Integer numUsers: numberOfDistinctUserVsFrequency.keySet() ) {
            numUsersVsFreqFileWriter.write(numUsers + "-" + numberOfDistinctUserVsFrequency.get(numUsers) + '\n');
        }

        numUsersVsFreqFileWriter.close();


    }

    public void generateConversationLengthVsFreq() throws IOException{

        conversationLengthVsFreqFileWriter = new BufferedWriter(new FileWriter(convLengthVsFreqFile));

        TreeMap<Integer, Integer> conversationLengthVsFrequency = new TreeMap<Integer, Integer>();

        for( Long rootId : roots ) {

            //int conversationLength = adjacencyList.get(rootId).size();
            int conversationLength = bfs(rootId);

            if( !conversationLengthVsFrequency.containsKey(conversationLength) )
                conversationLengthVsFrequency.put(conversationLength, 0);

            int freq = conversationLengthVsFrequency.get(conversationLength);
            conversationLengthVsFrequency.put(conversationLength, freq+1);
        }

        for( Integer conversationLength: conversationLengthVsFrequency.keySet() ) {

            conversationLengthVsFreqFileWriter.write(conversationLength + "-" + conversationLengthVsFrequency.get(conversationLength) + '\n');
        }

        conversationLengthVsFreqFileWriter.close();

    }

    private void validate() throws IOException, NullPointerException {

        if(IndexConfig.statLoc == null )
            throw new NullPointerException();

        statFolder = new File(IndexConfig.statLoc);

        if( statFolder.isDirectory() ) {
            System.err.println("Directory at STAT_LOC already exists, please delete it and run again.");
            throw new IOException();
        }

        if( statFolder.isFile() ) {
            System.err.println("File found at STAT_LOC while expecting nothing, please delete it and run again.");
            throw new IOException();
        }

        boolean isDirectoryCreated = statFolder.mkdir();

        if( isDirectoryCreated == false ) {
            System.err.println("Something went wrong with directory creation; please check STAT_LOC once again.");
            throw new IOException();
        }

        IndexConfig.convFilePath = IndexConfig.statLoc + File.separator + "conv.txt";
        convFile = new File(IndexConfig.convFilePath);

        IndexConfig.numUsersVsFreqFilePath = IndexConfig.statLoc + File.separator + "num_users_vs_freq.txt";
        numUsersVsFreqFile = new File(IndexConfig.numUsersVsFreqFilePath);

        IndexConfig.convLengthVsFreqFilePath = IndexConfig.statLoc + File.separator + "conv_length_vs_freq.txt";
        convLengthVsFreqFile = new File(IndexConfig.convLengthVsFreqFilePath);

        IndexConfig.minutesVsFreqFilePath = IndexConfig.statLoc + File.separator + "minutes_vs_freq.txt";
        minutesVsFreqFile = new File(IndexConfig.minutesVsFreqFilePath);

    }


    int bfs( Long rootId ) {

        int numOfNodes = 0;

        Queue<Long> q = new LinkedList<Long>();

        q.add(rootId);

        while( !q.isEmpty() ) {
            numOfNodes ++;

            Long id = q.poll();

            if( adjacencyList.containsKey(id)) {
                for (Long childId : adjacencyList.get(id)) {
                    q.add(childId);
                }
            }
        }

        return numOfNodes;
    }



    public void dumpTweets() throws IOException {

        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(IndexConfig.statLoc + File.separator + "tweets.txt"));

        for( Long rootId : roots ) {

            Stack<Pair<Long,Integer>> s = new Stack<Pair<Long,Integer>>();

            s.add(new Pair<Long, Integer>(rootId,0));

            while ( !s.isEmpty() ) {

                Pair<Long,Integer> id = s.pop();

                for( int i = 0 ; i <= id.getValue() ; i++ )
                    fileWriter.write("---");
                fileWriter.write(id.getKey() + " : " + tweetMap.get(id.getKey()).getTweetText());
                fileWriter.write('\n');

                if( adjacencyList.containsKey(id.getKey())) {
                    for (Long childId : adjacencyList.get(id.getKey())) {
                        s.push(new Pair<Long, Integer>(childId,id.getValue()+1));
                    }
                }

            }

        }

        fileWriter.close();

    }

}
