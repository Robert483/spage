package com.thathustudio.spage.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Phung on 10/01/2017.
 */

public class DateTimeUtil {
    public static String timestampToString(long postDate){
        Date dateNow = new Date();
        long diff = dateNow.getTime() - postDate;
        long diffInMinute = diff / 1000;
        long diffInHour = diff / 1000 / 60;
        long diffInDate = diff / 1000 / 60 / 24;

        if(diffInDate > 5){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, 'at' hh:mm");
            Date date = new Date(postDate);
            return simpleDateFormat.format(date);
        }
        else if(diffInDate >= 1){
            return diffInDate + " days ago";
        }
        else if(diffInHour >= 1){
            return diffInHour + " hours ago";
        }
        else if(diffInMinute >= 1){
            return diffInMinute + " minutes ago";
        }
        else {
            return "Now";
        }
    }
}
