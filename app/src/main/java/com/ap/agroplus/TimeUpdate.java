package com.ap.agroplus;

import java.util.Date;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class TimeUpdate {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;

    public final static long ONE_WEEK = ONE_DAY * 7;

    public final static long ONE_MONTH = ONE_WEEK * 4;

    public final static long ONE_YEAR = ONE_MONTH * 12;

    private TimeUpdate() {
    }

    public static String setAgo(long duration) {

        String outTime = "";

        Date currentDate = new Date();
        long currentTime = currentDate.getTime();

        long diff = currentTime - duration;

        double years = Math.floor((double) diff / ONE_YEAR);
        double months = Math.floor((double) diff / ONE_MONTH);
        double weeks = Math.floor((double) diff / ONE_WEEK);

        double days = Math.floor((double) diff / ONE_DAY);
        double hours = Math.floor((double) (diff % ONE_DAY) / ONE_HOUR);
        double minutes = Math.floor((double) (diff % ONE_HOUR) / ONE_MINUTE);
        double seconds = Math.floor((double) (diff % ONE_MINUTE) / ONE_SECOND);

        int yr = (int) years;
        int mt = (int) months;
        int w = (int) weeks;

        int d = (int) days;
        int h = (int) hours;
        int m = (int) minutes;
        int s = (int) seconds;

        if (years > 0) {
            if (yr == 1) {
                outTime = String.valueOf(yr) + "yr ago";
            } else {
                outTime = String.valueOf(yr) + "yrs ago";
            }
        } else if (months > 0) {
            if (mt == 1) {
                outTime = String.valueOf(mt) + "mon ago";
            } else {
                outTime = String.valueOf(mt) + "mons ago";
            }
        } else if (weeks > 0) {
            if (w == 1) {
                outTime = String.valueOf(w) + "wk ago";
            } else {
                outTime = String.valueOf(w) + "wks ago";
            }
        } else if (days > 0) {
            if (d == 1) {
                outTime = String.valueOf(d) + "day ago";
            } else {
                outTime = String.valueOf(d) + "days ago";
            }
        } else if (hours > 0) {
            if (h == 1) {
                outTime = String.valueOf(h) + "hr ago";
            } else {
                outTime = String.valueOf(h) + "hrs ago";
            }
        } else if (minutes > 0) {
            if (m == 1) {
                outTime = String.valueOf(m) + "min ago";
            } else {
                outTime = String.valueOf(m) + "mins ago";
            }
        } else if (seconds > 0) {
            outTime = String.valueOf(s) + "secs ago";
        } else {
            outTime = "moments ago";
        }

        return outTime;
    }
}
