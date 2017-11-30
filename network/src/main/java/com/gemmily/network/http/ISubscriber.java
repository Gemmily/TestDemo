package com.gemmily.network.http;

import java.net.ConnectException;
import java.net.SocketException;

import rx.Subscriber;

/**
 * Created by Gemmily on 2017/3/7.
 */
public abstract class ISubscriber<T> extends Subscriber<T> {

    //    private SubscriberListener listener;

    //    public ISubscriber(SubscriberListener listener) {
    //        this.listener = listener;
    //    }

    public abstract void onError(String error);

    public abstract void onResult(T t);


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof ApiException) {
            onError(((ApiException) e).getMsg());
        } else if (e instanceof ConnectException) {
            onError("网络中断，请检查您的网络状态");
        } else if (e instanceof SocketException) {
            onError("网络中断，请检查您的网络状态");
        } else {
            onError(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        onResult(t);
    }
}
