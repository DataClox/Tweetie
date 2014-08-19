package com.dataclox.tweetie.parser;

import com.dataclox.tweetie.config.IndexConfig;

import java.io.*;

/**
 * Created by devilo on 16/8/14.
 */
public class DumpParser {

    private File indexFolder    = null;
    private File dumpFile       = null;

    public void createIndex() throws IOException {

        validate();

        String tweet = null;

        TweetProcessor tweetProcessor = new TweetProcessor(indexFolder, dumpFile);

        //BufferedReader dumpReader = new BufferedReader(new InputStreamReader(new FileInputStream(dumpFile), "UTF-8"));

        BufferedReader dumpReader = new BufferedReader(new FileReader(dumpFile));

        while((tweet = dumpReader.readLine()) != null) {

            String processedTweet = tweet.trim();
            if( processedTweet.length() > 0 )
                tweetProcessor.process(processedTweet);

        }

        dumpReader.close();
        tweetProcessor.finish();

    }

    private void validate() throws NullPointerException, IOException {

        if(IndexConfig.indexLoc == null )
            throw new NullPointerException();

        indexFolder = new File(IndexConfig.indexLoc);

        if( indexFolder.isDirectory() ) {
            System.err.println("Directory at INDEX_LOC already exists, please delete it and run again.");
            throw new IOException();
        }

        if( indexFolder.isFile() ) {
            System.err.println("File found at INDEX_LOC while expecting nothing, please delete it and run again.");
            throw new IOException();
        }

        boolean isDirectoryCreated = indexFolder.mkdir();

        if( isDirectoryCreated == false ) {
            System.err.println("Something went wrong with directory creation; please check INDEX_LOC once again.");
            throw new IOException();
        }

        dumpFile = new File(IndexConfig.twitterDumpPath);

        if( dumpFile.isDirectory() ) {
            System.err.println("Expecting file found directory, please check the TWITTER_DUMP and run again.");
            throw new IOException();
        }

        if( dumpFile.exists() == false ) {
            System.err.println("File at TWITTER_DUMP does not exists, check TWITTER_DUMP and run again.");
            throw new IOException();
        }

    }

}
