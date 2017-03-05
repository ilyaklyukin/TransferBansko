package com.umnix.transferbansko.dagger;

import android.content.Context;

import com.umnix.transferbansko.TransferApplication;
import com.umnix.transferbansko.model.OrderValidator;
import com.umnix.transferbansko.utils.ServiceBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class ApplicationModule {

    private final TransferApplication transferApplication;

    public ApplicationModule(TransferApplication transferApplication) {
        this.transferApplication = transferApplication;
    }

    public void bootstrap() {
        Timber.plant(new Timber.DebugTree());
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.transferApplication;
    }

    @Provides
    @Singleton
    OrderValidator provideOrderValidator() {
        return new OrderValidator();
    }

    @Provides
    @Singleton
    ServiceBus provideServiceBus() {
        return new ServiceBus();
    }
}
