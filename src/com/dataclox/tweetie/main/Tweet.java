package com.dataclox.tweetie.main;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilo on 19/8/14.
 */
public class Tweet {

    String tweetText = null;
    Date tweetTimestamp = null;
    Long tweetId = null;
    Long tweetUserId = null;
    Long tweetInReplyToStatusId = null;

    boolean isEnglish = false;

    public Tweet() {

    }

    public Tweet(String tweetText, Date tweetTimestamp, Long tweetId, Long tweetUserId, Long tweetInReplyToStatusId) {
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

    public Date getTweetTimestamp() {
        return tweetTimestamp;
    }

    public void setTweetTimestamp(Date tweetTimestamp) {
        this.tweetTimestamp = tweetTimestamp;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getTweetUserId() {
        return tweetUserId;
    }

    public void setTweetUserId(Long tweetUserId) {
        this.tweetUserId = tweetUserId;
    }

    public Long getTweetInReplyToStatusId() {
        return tweetInReplyToStatusId;
    }

    public void setTweetInReplyToStatusId(Long tweetInReplyToStatusId) {
        this.tweetInReplyToStatusId = tweetInReplyToStatusId;
    }
}
