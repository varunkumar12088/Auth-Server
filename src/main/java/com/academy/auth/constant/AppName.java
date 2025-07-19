package com.academy.auth.constant;


import org.apache.commons.lang3.StringUtils;

public enum AppName {
    MY_RIDE_DNA,
    DMS,
    SECTIONS,
    DEFAULT,
    ACADEMY;

    public static boolean isValidAppName(String appName) {
        if(StringUtils.isBlank(appName)){
            return false;
        }
        for (AppName app : AppName.values()) {
            if (app.name().equalsIgnoreCase(appName)) {
                return true;
            }
        }
        return false;
    }
}
