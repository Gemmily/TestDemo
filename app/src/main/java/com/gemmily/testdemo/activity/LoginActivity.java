package com.gemmily.testdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gemmily.network.http.ISubscriber;
import com.gemmily.network.model.Login;
import com.gemmily.testdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * Created by Gemmily on 2017/5/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private android.widget.Button login;
    private android.widget.TextView tvcontent;
    private Button cookie;
    private String ticket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cookie = (Button) findViewById(R.id.cookie);
        tvcontent = (TextView) findViewById(R.id.tv_content);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        cookie.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                getTicket();
                break;
            case R.id.cookie:
                login();
                break;
        }
    }

    private void getTicket() {
        Login params = new Login();
        params.setLogincode("13655554444");
        params.setPassword("123456");
        apiManager.getload(params, new ISubscriber<ResponseBody>() {
            @Override
            public void onError(String error) {
                tvcontent.setText(error);
            }

            @Override
            public void onResult(ResponseBody responseBody) {
                try {
                    tvcontent.setText(responseBody.string());
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    ticket = jsonObject.getString("tgt");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void login(){
        apiManager.login(ticket, new ISubscriber<ResponseBody>() {
            @Override
            public void onError(String error) {
                tvcontent.setText(error);

            }

            @Override
            public void onResult(ResponseBody responseBody) {
                try {
                    tvcontent.setText(responseBody.string());
                    startActivity( new Intent(LoginActivity.this, MainActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
