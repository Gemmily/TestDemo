package com.gemmily.network.http;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Gemmily on 2017/5/17.
 */
public class CookieManager implements CookieJar {
    private final ArrayMap<String, List<Cookie>> cookieStore = new ArrayMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String cookieValue ="sss";
        if (TextUtils.isEmpty(cookieValue)){
            cookieStore.put(url.host(), cookies);
        }
        List<Cookie> nCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
            Cookie.Builder cookBuilder = new Cookie.Builder()
                    .domain(cookie.domain())
                    .path(cookie.path())
                    .name(cookie.name())
                    .value(cookieValue)
                    .expiresAt(cookie.expiresAt());
            if (cookie.httpOnly()) {
                cookBuilder.httpOnly();
            }
            if (cookie.secure()) {
                cookBuilder.secure();
            }
            Cookie newCookie = cookBuilder.build();
            nCookies.add(newCookie);
        }
        cookieStore.put(url.host(), nCookies);

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies == null ? new ArrayList<Cookie>() : cookies;
    }
}
