package com.umnix.transferbansko;

import android.app.Application;
import android.content.Context;

import com.umnix.transferbansko.dagger.ApplicationComponent;
import com.umnix.transferbansko.dagger.ApplicationModule;
import com.umnix.transferbansko.dagger.DaggerApplicationComponent;

public class TransferApplication extends Application {

    private static ApplicationComponent applicationComponent;

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ApplicationModule module = new ApplicationModule(this);
        setComponent(DaggerApplicationComponent.builder()
                .applicationModule(module)
                .build());

        getComponent().inject(this);
        module.bootstrap();
    }

    public static Context getContext() {
        return context;
    }

    public static ApplicationComponent getComponent() {
        return applicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
