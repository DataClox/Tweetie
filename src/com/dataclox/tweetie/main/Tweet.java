package com.dataclox.tweetie.main;

import java.text.SimpleDateFormat;

/**
 * Created by devilo on 19/8/14.
 */
public class Tweet {

    String tweetText = null;
    TweetTimeStamp tweetTimestamp = null;
    Long tweetId = null;
    String tweetUserId = null;
    Long tweetInReplyToStatusId = null;

    public Tweet() {

    }

    public Tweet(String tweetText, TweetTimeStamp tweetTimestamp, Long tweetId, String tweetUserId, Long tweetInReplyToStatusId) {
        this.tweetText = tweetText;
        this.tweetTimestamp = tweetTimestamp;
        this.tweetId = tweetId;
        this.tweetUserId = tweetUserId;
        this.tweetInReplyToStatusId = tweetInReplyToStatusId;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public TweetTimeStamp getTweetTimestamp() {
        return tweetTimestamp;
    }

    public void setTweetTimestamp(TweetTimeStamp tweetTimestamp) {
        this.tweetTimestamp = tweetTimestamp;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweetUserId() {
        return tweetUserId;
    }

    public void setTweetUserId(String tweetUserId) {
        this.tweetUserId = tweetUserId;
    }

    public Long getTweetInReplyToStatusId() {
        return tweetInReplyToStatusId;
    }

    public void setTweetInReplyToStatusId(Long tweetInReplyToStatusId) {
        this.tweetInReplyToStatusId = tweetInReplyToStatusId;
    }
}
