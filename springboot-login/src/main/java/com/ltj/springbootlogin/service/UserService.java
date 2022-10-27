package com.ltj.springbootlogin.service;

import com.ltj.springbootlogin.domain.User;

public interface UserService {

    User nameFindService(String uname);
    User ifExist(String uname);
    User loginService(String uname,String password);
    User registService(User user);
    User updateService(User newUser);
    int deleteService(String uname);
    int ifLoginService(String uname);
    void logoutService(String uname);
    int checkBlack(User user);
    void setBlack(User user );
    void setWhite(User user );
    int checkVip(User user);
    void rechargeVip(User user,int days);
    String showWorkList(User user);
    void addWork(User user,int id);
}
