package com.dataclox.tweetie.parser;

import com.dataclox.tweetie.main.TweeStruct;
import com.dataclox.tweetie.main.Tweet;
import com.dataclox.tweetie.main.TweetTimeStamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    }
}
