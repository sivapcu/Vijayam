package com.avisit.vijayam.model;

/**
 * Created by User on 4/16/2015.
 */
public class Topic {
    private int topicId;
    private String topicName;

    public Topic(){

    }

    public Topic(int topicId, String topicName) {
        this.topicName = topicName;
        this.topicId = topicId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String toString() {
        return this.topicName;
    }
}
