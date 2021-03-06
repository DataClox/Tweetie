package com.dataclox.tweetie.main;

import com.dataclox.tweetie.config.IndexConfig;
import com.dataclox.tweetie.parser.ConfigParser;
import com.dataclox.tweetie.parser.DumpParser;
import com.dataclox.tweetie.parser.IntermediateDumpParser;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by devilo on 16/8/14.
 */
public class Tweetie {

    DumpParser dumpParser = null;
    ConfigParser configParser = null;
    StatGenerator statGenerator = null;
    IntermediateDumpParser intermediateDumpParser = null;

    public Tweetie() {

        dumpParser = new DumpParser();
        configParser = new ConfigParser();
        statGenerator = new StatGenerator();
    }

    public void start() {

        try {
            configParser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------- Variables Set --------------------");
        System.out.println("CREATE_INDEX = " + IndexConfig.createIndex);
        System.out.println("USE_INTERMEDIATE_INDEX = " + IndexConfig.useIntermediateIndex);
        System.out.println("TWITTER_DUMP = " + IndexConfig.twitterDumpPath);
        System.out.println("INDEX_LOC = " + IndexConfig.indexLoc);
        System.out.println("INTER_DUMP = " + IndexConfig.interDump);
        System.out.println("NULL_DUMP = " + IndexConfig.nullDump);


        if( IndexConfig.createIndex == true ) {

            try {
                dumpParser.createIndex();

                System.out.println("Index at INDEX_LOC created ...");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if( IndexConfig.createIndex == true || IndexConfig.useIntermediateIndex == true ) {

            intermediateDumpParser = new IntermediateDumpParser(IndexConfig.interDump);
            System.out.println("Using intermediate file : " + IndexConfig.interDump);

            try {

                intermediateDumpParser.createTweetStruct();

                System.out.println("Total roots : " + TweeStruct.getInstance().getAdjacencyList().keySet().size());

                statGenerator.generateConversations();
                statGenerator.generateNumOfDistinctUsersVsFreq();
                statGenerator.generateConversationLengthVsFreq();
                statGenerator.generateMinutesVsFreq();

                //statGenerator.dumpTweets();

                statGenerator.printConversationTree();


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else {
            System.out.println("Index at INDEX_LOC is used ...");
        }


        TweeChat tweeChat = new TweeChat();
        tweeChat.initialize();
        tweeChat.start();

    }


    public static void main(String[] args) {

        long s,e;

        s = System.currentTimeMillis();

        Tweetie tweetie = new Tweetie();
        tweetie.start();

        e = System.currentTimeMillis();

        System.out.println("Time elapsed : " + (e-s)/1000 + " sec.");

    }

}
