package com.dataclox.tweetie.parser;

import com.dataclox.tweetie.config.IndexConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by devilo on 16/8/14.
 */
public class ConfigParser {

    private String configFilePath = "config.txt";

    BufferedReader configReader;

    public void parse() throws IOException {

        String configLine = null;

        configReader = new BufferedReader(new FileReader(configFilePath));

        while( (configLine = configReader.readLine()) != null ) {

            configLine = configLine.trim();

            if( configLine.startsWith("#") || configLine.length() == 0 ) {
                continue;
            }

            String[] keyValue = configLine.split("=");

            if( keyValue[0].equalsIgnoreCase("TWITTER_DUMP") ) {
                IndexConfig.twitterDumpPath = keyValue[1];
            }
            else if( keyValue[0].equalsIgnoreCase("CREATE_INDEX") ) {
                IndexConfig.createIndex = Boolean.parseBoolean(keyValue[1]);
            }
            else if( keyValue[0].equalsIgnoreCase("INDEX_LOC") ) {
                IndexConfig.indexLoc = keyValue[1];
            }
            else {
                System.err.println("Invalid Key found : " + keyValue[0]);
                throw new IllegalArgumentException();
            }

        }


    }


}
