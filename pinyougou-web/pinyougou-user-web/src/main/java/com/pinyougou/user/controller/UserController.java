package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    /** 查询省份信息 */
    @GetMapping("/findProvinces")
    public List<Provinces> findProvinces(){
        return userService.findProvinces();
    }

    /** 根据省份ID查询城市名称 */
    @GetMapping("/findCityByParentId")
    public List<Cities> findCityByParentId(Long parentId){
            return userService.findCityByParentId(parentId);
    }

    /** 根据父级cityId查询,得到区级分类名称 */
    @GetMapping("/findAreaByCityId")
    public List<Areas> findAreaByCityId(Long cityId){
        return userService.findAreaByCityId(cityId);
    }


    /**　完善用户信息添加到tb_user表中　*/
    @PostMapping("/savePersonToUser")
    public boolean savePersonToUser(@RequestBody User user){
        try {
            //获取登录的用户名
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();

            //设置用户名
            user.setUsername(username);

          return   userService.savePersonToUser(user);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping("/sendMsg")
    public boolean sendMsg(@RequestBody Map<String,String> phoneInfo,HttpServletRequest request){
        // 1. 判断验证码
        // 1.1 从Session中获取验证码
        String oldCode = (String) request.getSession()
                .getAttribute(VerifyController.VERIFY_CODE);
        System.out.println("oldCode = " + oldCode);
        if (phoneInfo.get("inputCode").equalsIgnoreCase(oldCode)) {
            /** 发送验证码 */
            boolean success = userService.sendCode(phoneInfo.get("phone"));
            return success;
        }else {
            return false;
        }
    }

    @PostMapping("/msgCodeVerify")
    public boolean msgCodeVerify(@RequestBody Map<String,String> Info){
        // 调用 用户服务层  进行 短信验证码验证
        boolean success = userService.checkSmsCode(Info.get("phone"), Info.get("msgCode"));
        return success;
    }

    @PostMapping("/newPhoneMsgCodeVerify")
    public boolean newPhoneMsgCodeVerify(@RequestBody Map<String,String> Info){
        // 调用 用户服务层  进行 短信验证码验证
        boolean success = userService.checkSmsCode(Info.get("phone"), Info.get("msgCode"));
        // 验证成功
        if (success){
            // 将新手机绑定到对应的用户中
            userService.updatePhone(Info.get("userName"),Info.get("phone"));
        }
        return success;

    }
}
