package com.thathustudio.spage.util;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by apple on 1/19/17.
 */

public class TaskManager {
    ArrayList<Call> lstCall =  new ArrayList<>();
    public void pushTaskToQueue(Call call){
        lstCall.add(call);
    }

    public void cancelAllTask(){
        for(int i=0;i<lstCall.size();++i){
            lstCall.get(i).cancel();
        }
    }

}
