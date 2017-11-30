package com.gemmily.network.http;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Gemmily on 2017/5/17.
 */
public interface Api {
    @GET("android/test/login/1")
    Observable<ResponseBody> login(@Query("ticket") String ticket);

    @GET("android/user/userInfo/1")
    Observable<ResponseBody>getUSerInfo();

}
