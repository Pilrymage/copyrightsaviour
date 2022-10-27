package com.ltj.springbootlogin.controller;

import com.ltj.springbootlogin.domain.User;
import com.ltj.springbootlogin.service.UserService;
import com.ltj.springbootlogin.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.sql.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/showWorkList")
    public Result<String> showWorkList(@RequestParam String uname){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        else {
            String res=userService.showWorkList(user);
            if(res==null){
                return Result.error("8","还未有作品");
            }
            return Result.success(res,"找到作品！");
        }
    }//用户作品列表
    @PostMapping("/addWork")
    public Result<String>addWork(@RequestParam String uname,int id){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        else {
            userService.addWork(user,id);
            String res=userService.showWorkList(user);
            return Result.success(res,"添加成功！");
        }
    }

    @PostMapping("/checkVip")
    public Result checkVipController(@RequestParam String uname){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        else {
            int flag=userService.checkVip(user);
            if(flag==0){
                return  Result.success(0,"不是VIP！");
            }
            else return Result.success(1,"是VIP！");
        }
    }//vip还需要判断endTime是否已经过了，不能简单看boolean

    @PostMapping("/recharge")
    public Result<String> rechargeController(@RequestParam String uname, int days){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        else {
            userService.rechargeVip(user,days);
            return Result.success(user.getEndTime(),"充值成功！");
        }
    }
    @PostMapping("/setBlack")
    public Result setBlackController(@RequestParam String uname){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        int flag=userService.checkBlack(user);
        if(flag==1){
            return Result.success(0,"已经在黑名单中！");
        }
        else {
            //如果正处于登录状态将被强制logout
            int flag2=userService.ifLoginService(uname);
            if(flag2==1){
                userService.logoutService(uname);
            }
            userService.setBlack(user);
            return Result.success(1,"成功设置为黑名单用户！");
        }
    }//设置用户进入黑名单

    @PostMapping("/setWhite")
    public Result setWhiteController(@RequestParam String uname){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("7","未找到该用户！");
        }
        int flag=userService.checkBlack(user);
        if(flag==0){
            return Result.success(0,"未进入黑名单！");
        }
        else {
            userService.setWhite(user);
            return Result.success(1,"成功将该用户移出黑名单！");
        }
    }//移除黑名单中的用户

    @PostMapping("/checkLogin")
    public Result checkController(@RequestParam String uname){
        long res = userService.ifLoginService(uname);
        if(res==1){
            return Result.success(1,"已经登陆！");
        }
        else {
            return Result.success(0,"未登录！");
        }
    }//检查是否登录

    @PostMapping("/login")
    public Result<User> loginController(@RequestParam String uname,@RequestParam String password){
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("0","用户名不存在！");
        }
        else {
            int flag=userService.checkBlack(user);
            if(flag==0){
                User res=userService.loginService(uname,password);
                if(res==null){
                    return Result.error("1","密码错误！");
                }
                else
                    return Result.success(res,"登录成功！");
            }
            else {
                return Result.error("6","用户在黑名单中！");
            }
        }
    }//登录

    @PostMapping("/logout")
    public Result logoutController(@RequestParam String uname){
        int flag=userService.ifLoginService(uname);
        if(flag==0){
            return Result.error("2","登出失败！");
        }
        userService.logoutService(uname);
        return Result.success(1,"登出成功！");
    }//登出

    @PostMapping("/register")
    public Result<User> registController(@RequestBody User newUser){
        User user = userService.registService(newUser);
        if(user!=null){
            return Result.success(user,"注册成功！");
        }
        else{
            return Result.error("3","用户名重复！");
        }
    }//注册

    @PostMapping("/update")
    public Result<User> updateController(@RequestBody User newUser){
        User user = userService.updateService(newUser);
        if(user!=null){
            return Result.success(user,"修改成功！");
        }
        else {
            return Result.error("4","修改失败！");
        }
    }//用户在此更新自己的数据

    @RequestMapping("/delete")
    public Result deleteController(@RequestParam String uname){
        int res=userService.deleteService(uname);
        if(res==1){
            return Result.success(1,"删除成功！");
        }
        else {
            return Result.error("5","未找到该用户！");
        }
    }//删除用户


}
