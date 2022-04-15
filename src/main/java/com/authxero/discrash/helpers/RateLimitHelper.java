package com.authxero.discrash.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RateLimitHelper {

    public static List<UserInfo> userInfoArrayList = new ArrayList<UserInfo>();

    public static final int TIME_OFFSET = 40;

    public static final int ALLOWED_REQUESTS = 5;

    public static boolean isBeingRateLimited(String ip) {
        boolean isNotInArray = findObjectByIp(ip) == null;
        long currentTime = System.currentTimeMillis();
        if (isNotInArray) {
            userInfoArrayList.add(new UserInfo(ip, currentTime + (TIME_OFFSET * 1000)));
            return false;
        }
        UserInfo userInfo = findObjectByIp(ip);
        int reqsSent = userInfo.getRequestsSent();
        userInfo.setRequestsSent(reqsSent + 1);
        if (userInfo.getTimestamp() < currentTime) {
            userInfo.setTimestamp(currentTime + (TIME_OFFSET * 1000));
            userInfo.setRequestsSent(1);
            return false;
        }
        if (userInfo.getRequestsSent() > ALLOWED_REQUESTS && userInfo.getTimestamp() > currentTime) {
            return true;
        }
        return false;
    }

    private static UserInfo findObjectByIp(String name) {
        for (UserInfo userInfo : userInfoArrayList) {
            if (userInfo.getIpAddr().equals(name)) {
                return userInfo;
            }
        }
        return null;
    }
}
