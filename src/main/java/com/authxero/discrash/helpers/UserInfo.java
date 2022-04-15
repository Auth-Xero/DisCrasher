package com.authxero.discrash.helpers;

import java.math.BigInteger;

public class UserInfo {

    private String ipAddr;

    private int requestsSent = 1;

    private long timestamp;

    public UserInfo(String ip, long timestamp){
        this.ipAddr = ip;
        this.timestamp = timestamp;
    }

    public void setRequestsSent(int requestsSent) {
        this.requestsSent = requestsSent;
    }

    public int getRequestsSent() {
        return requestsSent;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
