package com.example.trialapp3.Models;

public class chat_model {
    String UserId, message, messageID,photoMsg;
    Long timestamp;


    public chat_model(String userId, String message, Long time_stamp) {
        UserId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }



    public chat_model(String userId, String message) {
        UserId = userId;
        this.message = message;
    }



    public chat_model(){

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String message_id) {
        this.messageID = message_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long time_stamp) {
        this.timestamp = time_stamp;
    }
}
