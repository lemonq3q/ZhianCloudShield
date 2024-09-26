package org.example.service;

public interface LoginService {

    public String Login(String username,String phone);

    public String autoLogin(String token);

    public String upload(String username,String password);

    public String getCaptcha(String phone,int isVerify);

    public String exitLogin(String token);

}
