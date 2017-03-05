package com.umnix.transferbansko.utils;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ServiceBus {
    private PublishSubject<Boolean> emailSentStateSubject = PublishSubject.create();

    public void postSentEmailState(Boolean isConnectionEstablished) {
        emailSentStateSubject.onNext(isConnectionEstablished);
    }

    public Observable<Boolean> getSentEmailState() {
        return emailSentStateSubject;
    }

}