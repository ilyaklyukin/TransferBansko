package com.umnix.transferbansko.dagger;

import com.umnix.transferbansko.TransferApplication;
import com.umnix.transferbansko.model.OrderValidator;
import com.umnix.transferbansko.ui.MainActivity;
import com.umnix.transferbansko.ui.fragment.OrderFragment;
import com.umnix.transferbansko.ui.fragment.TextFragment;
import com.umnix.transferbansko.utils.ServiceBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(TransferApplication application);

    void inject(MainActivity activity);

    void inject(TextFragment fragment);

    void inject(OrderFragment fragment);

    void inject(ServiceBus serviceBus);

    void inject(OrderValidator validator);
}