package com.leto.sandbox.sdk.demo;

import android.app.Application;
import androidx.multidex.MultiDex;

public class LetoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
    }
}
