package com.thathustudio.spage.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.thathustudio.spage.BuildConfig;
import com.thathustudio.spage.service.SpageService;
import com.thathustudio.spage.service.SpageServiceImpl;
import com.thathustudio.spage.service.retrofit.AddHeaderInterceptor;
import com.thathustudio.spage.service.retrofit.Task4Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomApplication extends Application {

    private Task4Service task4Service;
    private SpageService spageService;
    private RefWatcher refWatcher;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static RefWatcher getRefWatcher(Context context) {
        CustomApplication application = (CustomApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static boolean isInDebugMode() {
        return BuildConfig.DEBUG;
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

    @Override
    public void onCreate() {
        super.onCreate();

        setUpLeakCanary();

        setUpSpageService();

    }

    public SpageService getSpageService() {
        return spageService;
    }

    public Task4Service getTask4Service() {
        if (task4Service == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new AddHeaderInterceptor())
                    .addInterceptor(httpLoggingInterceptor)
                    /*.readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)*/
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Task4Service.SERVICE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            task4Service = retrofit.create(Task4Service.class);
        }
        return task4Service;
    }
}
