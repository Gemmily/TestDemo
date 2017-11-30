package com.gemmily.network.http;

import android.content.Context;

import com.gemmily.network.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Gemmily on 2017/5/17.
 */
public class RetrofitUtil {
    private static Retrofit.Builder mRetrofitBuilder = null;
    private static OkHttpClient okHttpClient = null;
    protected Context mContext;
    protected CasApi mCasApi;
    protected Api mApi;

    public void init(Context context) {
        this.mContext = context;
        initOkHttp();
        initRetrofit();
        if (mApi == null) {
            mApi = createApi(Constants.URL_SERVER, Api.class);
        }
        if (mCasApi == null){
            mCasApi = createApi(Constants.SERVER_URL_CAS, CasApi.class);
        }

    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //打印请求log日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        CookieManager cookieManager = new CookieManager();
        builder.cookieJar(cookieManager);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();

    }

    private void initRetrofit() {
        mRetrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

    }

    private  <S> S createApi(String baseUrl, Class<S> apiClass) {
        return mRetrofitBuilder
                .baseUrl(baseUrl)
                .build()
                .create(apiClass);
    }


    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
