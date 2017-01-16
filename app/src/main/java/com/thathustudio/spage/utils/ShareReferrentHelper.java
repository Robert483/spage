package com.thathustudio.spage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.thathustudio.spage.model.User;

/**
 * Created by SonPham on 1/16/2017.
 */
public class ShareReferrentHelper {

    static public boolean setCurrentUser(Context context, User user){
        SharedPreferences.Editor editor = context.getSharedPreferences("USER", context.MODE_PRIVATE).edit();

        editor.putInt("idName", user.getId());
        editor.putString("userName", user.getName());
        editor.putString("passWord", user.getName());
        editor.commit();
        return true;
    }

    static public User getCurrentUser(Context context){
        SharedPreferences prefs = context.getSharedPreferences("USER", context.MODE_PRIVATE);

        int id = prefs.getInt("id", -1);
        String userName = prefs.getString("userName", null);
        String passWord = prefs.getString("passWord", null);
        if(id==-1 || userName==null || passWord==null)
            return null;
        User user = new User(id,userName,passWord);
        return  user;
    }

    static public User getTempUser(Context context){

        User user = new User(3,"sonpham","123456");
        return  user;
    }

}
