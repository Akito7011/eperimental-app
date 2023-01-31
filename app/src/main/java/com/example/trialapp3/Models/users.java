package com.example.trialapp3.Models;

public class users {
    String pfp, USERNAME, MAIl, PASSWD, userID, lastMessage, Status;

    public users() {

    }

    public users(String pfp, String USERNAME, String MAIl, String PASSWD, String userID, String lastMessage, String status) {
        this.pfp = pfp;
        this.USERNAME = USERNAME;
        this.MAIl = MAIl;
        this.PASSWD = PASSWD;
        this.userID = userID;
        this.lastMessage = lastMessage;
        Status = status;
    }

    public users(String USERNAME, String MAIl, String PASSWD) {
        this.USERNAME = USERNAME;
        this.MAIl = MAIl;
        this.PASSWD = PASSWD;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getMAIl() {
        return MAIl;
    }

    public void setMAIl(String MAIl) {
        this.MAIl = MAIl;
    }

    public String getPASSWD() {
        return PASSWD;
    }

    public void setPASSWD(String PASSWD) {
        this.PASSWD = PASSWD;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

