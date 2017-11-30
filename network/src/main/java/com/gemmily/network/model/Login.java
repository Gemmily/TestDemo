package com.gemmily.network.model;

import com.gemmily.network.util.SystemUtil;

/**
 * Created by Administrator on 2017/5/16.
 */
public class Login {
    private String serialno;
    private String machine_type;
    private String resolution;
    private String android_ver;
    private String ksudi_ver;
    private String kernel_ver;
    private String logintype;
    private String logincode;
    private String password;
    private String apptype;

    public Login() {
        this.apptype = "courier";
        this.logintype = "1";
        this.kernel_ver = "1.0";
        this.ksudi_ver = "1.0";
        this.android_ver = SystemUtil.getSystemVersion();
        this.resolution = "resolution";
        this.machine_type = SystemUtil.getPhoneModel();
        this.serialno = SystemUtil.getSerialNumber();
    }

    public String getLogincode() {
        return logincode;
    }

    public void setLogincode(String logincode) {
        this.logincode = logincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
