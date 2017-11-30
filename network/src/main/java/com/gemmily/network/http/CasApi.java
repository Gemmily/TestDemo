package com.gemmily.network.http;


import com.gemmily.network.model.Login;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Gemmily on 2017/3/6.
 */
public interface CasApi {

    @POST("login")
    Observable<ResponseBody> getTicket(@Body Login login);
}


