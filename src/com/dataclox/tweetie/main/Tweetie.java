package com.dataclox.tweetie.main;

import com.dataclox.tweetie.config.IndexConfig;
import com.dataclox.tweetie.parser.ConfigParser;
import com.dataclox.tweetie.parser.DumpParser;

import java.io.IOException;

/**
 * Created by devilo on 16/8/14.
 */
public class Tweetie {

    public void start() {
        ConfigParser configParser = new ConfigParser();

        try {
            configParser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------- Variables Set --------------------");
        System.out.println("TWITTER_DUMP = " + IndexConfig.twitterDumpPath);
        System.out.println("INDEX_LOC = " + IndexConfig.indexLoc);
        System.out.println("CREATE_INDEX = " + IndexConfig.createIndex);
        System.out.println("USE_INTERMEDIATE_INDEX = " + IndexConfig.useIntermediateIndex);

        if( IndexConfig.createIndex == true ) {
            DumpParser dumpParser = new DumpParser();

            try {
                dumpParser.createIndex();

                System.out.println("Index at INDEX_LOC created ...");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if( IndexConfig.useIntermediateIndex == true ) {

        }
        else {
            System.out.println("Index at INDEX_LOC is used ...");
        }

    }


    public static void main(String[] args) {
        Tweetie tweetie = new Tweetie();
        tweetie.start();
    }

}
