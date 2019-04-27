package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
*   登录用户地址管理控制器
* */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference(timeout = 10000)
    private AddressService addressService;

    /** 根据登录用户名查询全部 */
    @GetMapping("/findAll")
    public List<Address> findAll(){
        try {
            //获取用户登录名
            String userId =
                    SecurityContextHolder.getContext().getAuthentication().getName();

            return addressService.findAll(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 新建用户地址 */
    @PostMapping("/saveAddress")
    public boolean saveAddress(@RequestBody Address address){
        try {
            //获取登录用户ID
            String userId =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            //设置用户名
            address.setUserId(userId);
            //设置新地址is_Default状态码为0(非默认)
            address.setIsDefault("0");
            //调用添加地址
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**  修改默认地址(修改isDefault状态码) */
    @GetMapping("/updateDefaultStatus")
    public boolean updateDefaultStatus(Long id){
        try {
            addressService.updateDefaultStatus(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**  删除地址 */
    @GetMapping("/deleteAddress")
    public boolean deleteAddress(Long id){
        try {
            addressService.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }





}
