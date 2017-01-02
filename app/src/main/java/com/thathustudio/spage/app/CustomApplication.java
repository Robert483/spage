package com.thathustudio.spage.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.thathustudio.spage.BuildConfig;
import com.thathustudio.spage.service.SpageService;
import com.thathustudio.spage.service.SpageServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomApplication extends Application {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private SpageService spageService;

    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();

        setUpLeakCanary();

        setUpSpageService();

    }

    private void setUpSpageService() {
        spageService = new SpageServiceImpl(this);
    }

    private void setUpLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        CustomApplication application = (CustomApplication)context.getApplicationContext();
        return application.refWatcher;
    }

    public static boolean isInDebugMode() {
        return BuildConfig.DEBUG;
    }

    public SpageService getSpageService() {
        return spageService;
    }
}
