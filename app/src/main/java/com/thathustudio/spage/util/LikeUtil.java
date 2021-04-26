package com.thathustudio.spage.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.User;

/**
 * Created by apple on 1/19/17.
 */

public class LikeUtil {
    public static void saveLikeToSharePre(Context context,int userId, int postId){

        //Luu key idUser_idPost =>value true: Liked : False: Chua like
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = userId+"_"+postId;
        prefs.edit().putBoolean(key, !isLikedPost(context,userId,postId)).commit();
    }

    public static boolean isLikedPost(Context context,int userId, int postId){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = userId+"_"+postId;
        return prefs.getBoolean(key, false);
    }
}
