package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public abstract class Task4Activity extends AppCompatActivity {
    private boolean reloadable;
    private List<Call> calls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadable = false;
        calls = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadable = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        reloadable = true;

        for (Call call : calls) {
            call.cancel();
        }
        calls.clear();
    }

    public boolean isReloadable() {
        return reloadable;
    }

    public void addCall(Call call) {
        calls.add(call);
    }

    public void removeCall(Call call) {
        calls.remove(call);
    }
}
