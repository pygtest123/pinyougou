package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    /** 用户注册 */
    @PostMapping("/save")
    public boolean save(@RequestBody User user, String code){
        try{
            // 检验验证码是否正确
            boolean flag = userService.checkSmsCode(user.getPhone(), code);
            if (flag) {
                userService.save(user);
            }
            return flag;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    /** 发送短信验证码 */
    @GetMapping("/sendSmsCode")
    public boolean sendSmsCode(String phone){
        try{
            return userService.sendSmsCode(phone);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }



    // 更新用户密码

    @PostMapping("/updatePassword")
    public boolean updatePassword(@RequestBody Map<String,String> updateUserPassword){
        try {

            userService.updatePassword(updateUserPassword.get("userName"),updateUserPassword.get("newPassword"));
            return true;

        } catch (Exception e){
           e.printStackTrace();
        }

        return false;
    }


    // 获取用户号码
    @PostMapping("/UserInfo")
    @ResponseBody
    public User UserInfo(String userName){
        try {

            User user = userService.UserInfo(userName);
            return user;
        } catch (Exception e){
          throw new RuntimeException(e);
        }


    }

}
