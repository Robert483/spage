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

        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putInt("role", user.getRole());
        editor.commit();
        return true;
    }

    static public User getCurrentUser(Context context){
        SharedPreferences prefs = context.getSharedPreferences("USER", context.MODE_PRIVATE);

        int id = prefs.getInt("id", -1);
        String userName = prefs.getString("username", null);
        String passWord = prefs.getString("password", null);
        String email = prefs.getString("email", null);
        int role = prefs.getInt("role",-1);
        if(id==-1 || userName==null || passWord==null)
            return null;
        User user = new User(id,userName,passWord, email,role);
        return  user;
    }

    static public User getTempUser(Context context){

        User user = new User(3,"sonpham","123456","sonpham@gmail.com",1);
        return  user;
    }

}
