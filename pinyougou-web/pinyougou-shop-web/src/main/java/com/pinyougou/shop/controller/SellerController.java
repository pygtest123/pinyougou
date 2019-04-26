package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 商家控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 商家申请入驻 */
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller){
        try{
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查询商家信息 */
    @GetMapping("/findSeller")
    public Seller findSeller(){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return sellerService.findOne(sellerId);
    }

    /** 保存修改后的商家信息 */
    @PostMapping("/update")
    public boolean update(@RequestBody Seller seller){
        try {
            sellerService.update(seller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 修改商家密码 */
    @PostMapping("/updatePassword")
    public boolean updatePassword(@RequestBody Map<String,String> map){
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        String newWord = passwordEncoder.encode(newPassword);

        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerService.findOne(sellerId);
        String password = seller.getPassword();
        try {
            if (passwordEncoder.matches(oldPassword,password)){
                seller.setPassword(newWord);
                sellerService.update(seller);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
