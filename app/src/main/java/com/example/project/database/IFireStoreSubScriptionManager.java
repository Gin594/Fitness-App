package com.example.cse110project.database;

public interface IFireStoreSubScriptionManager {

    void subscribeAll();

    void subscribeToNewTopic(String topicName);

    void unsubscribe(String topicName);

}
