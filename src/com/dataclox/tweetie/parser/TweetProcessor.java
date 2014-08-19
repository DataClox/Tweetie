package com.dataclox.tweetie.parser;

import com.dataclox.tweetie.config.IndexConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.Buffer;

/**
 * Created by devilo on 17/8/14.
 */
public class TweetProcessor {

    private final String PREFIX = "INTER";

    File dumpFile = null;
    File indexFolder = null;
    File nullFile = null;
    File intermediateFile = null;

    BufferedWriter fileWriter = null;
    BufferedWriter nullWriter = null;

    public TweetProcessor(File inFolder, File dFile) {

        this.dumpFile = dFile;
        this.indexFolder = inFolder;

        intermediateFile = new File(indexFolder.getAbsolutePath() + File.separator + PREFIX + "_" + dumpFile.getName());
        nullFile = new File(IndexConfig.nullDump);

        System.out.println("Intermediate File : " + intermediateFile.getAbsolutePath());

        try {

            intermediateFile.createNewFile();

            //fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(intermediateFile) , "UTF-8"));
            fileWriter = new BufferedWriter(new FileWriter(intermediateFile));
            nullWriter = new BufferedWriter(new FileWriter(nullFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void process(String tweet) throws IOException {

        JSONParser jsonParser = new JSONParser();

        String tweetText = null;
        String tweetTimestamp = null;
        String tweetId = null;
        String tweetUserId = null;
        String tweetInReplyToStatusId = null;

        try {

            JSONObject tweetJO = (JSONObject) jsonParser.parse(tweet);
            JSONObject tweetDataJO = (JSONObject) jsonParser.parse((String) tweetJO.get("Data"));

            tweetId = (String) tweetDataJO.get("IdStr");
            tweetTimestamp = (String) tweetDataJO.get("CreatedAt");
            tweetInReplyToStatusId = (String) tweetDataJO.get("InReplyToStatusIdStr");
            tweetText = ((String) tweetDataJO.get("Text"));
            tweetUserId = (String) ((JSONObject)tweetDataJO.get("User")).get("IdStr");

            if(tweetText == null) {

                nullWriter.write("$" + tweetId + "\n");
                nullWriter.write("$" + tweetTimestamp + "\n");
                nullWriter.write("$" + tweetText + "\n");
                nullWriter.write("$" + tweetUserId + "\n");
                nullWriter.write("$" + tweetInReplyToStatusId + "\n");
                nullWriter.write("$\n");
                return;
            }

            tweetText = tweetText.replaceAll("[\\n\\r\\t]" , " ");

            fileWriter.write("$" + tweetId + "\n");
            fileWriter.write("$" + tweetTimestamp + "\n");
            fileWriter.write("$" + tweetText + "\n");
            fileWriter.write("$" + tweetUserId + "\n");
            fileWriter.write("$" + tweetInReplyToStatusId + "\n");
            fileWriter.write("$\n");

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void finish() throws IOException {

        if( fileWriter != null )
            fileWriter.close();

        if( nullWriter != null )
            nullWriter.close();
    }


}
