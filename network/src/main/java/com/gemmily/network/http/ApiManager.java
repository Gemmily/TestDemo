package com.gemmily.network.http;


import com.gemmily.network.model.Login;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Gemmily on 2017/3/7.
 */
public class ApiManager extends RetrofitUtil {

    public static ApiManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ApiManager instance = new ApiManager();
    }


    public <T> void getload(Login login, ISubscriber<ResponseBody> subscriber) {
        Observable observable = mCasApi.getTicket(login);
        toSubscribe(observable, subscriber);
    }

    public <T> void login(String ticket, ISubscriber<ResponseBody> subscriber) {
        Observable observable = mApi.login(ticket);
        toSubscribe(observable, subscriber);
    }
}
