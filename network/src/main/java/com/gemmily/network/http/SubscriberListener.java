package com.gemmily.network.http;

/**
 * Created by Gemmily on 2017/3/7.
 */
public interface SubscriberListener<T> {

    void onNext(T t);
    void onError(String error);
}
