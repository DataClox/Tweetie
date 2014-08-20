package com.dataclox.tweetie.main;

import com.dataclox.tweetie.config.IndexConfig;
import com.dataclox.tweetie.parser.ConfigParser;
import com.dataclox.tweetie.parser.DumpParser;
import com.dataclox.tweetie.parser.IntermediateDumpParser;

import java.io.IOException;

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
        intermediateDumpParser = new IntermediateDumpParser(IndexConfig.interDump);
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
        else if( IndexConfig.useIntermediateIndex == true ) {

            System.out.println("Using intermediate file : " + IndexConfig.interDump);

            try {

                intermediateDumpParser.createTweetStruct();
                System.out.println("Total roots : " + TweeStruct.getInstance().getAdjacencyList().keySet().size());

                statGenerator.generateConversations();

            }
            catch (IOException e) {
                e.printStackTrace();
            }




        }
        else {
            System.out.println("Index at INDEX_LOC is used ...");
        }

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
