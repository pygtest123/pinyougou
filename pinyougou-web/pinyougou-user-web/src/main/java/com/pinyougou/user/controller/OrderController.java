package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Order;
import com.pinyougou.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Reference(timeout = 10000)
    private OrderService orderService;

    /**
     * 查询订单
     */
    @GetMapping("/order")
    public List<Order> order(HttpServletRequest request) {
        //获取登录用户名
        String userId = request.getRemoteUser();
        System.out.println(" 登录用户名 :" + userId);

        /** 调用服务层方法查询 */
        return orderService.findOrder(userId);
    }

    //分页查询
    @PostMapping("/findByPage")
    public PageResult findByPage (HttpServletRequest request,
    @RequestBody Map<String, Object> params){
        // 获取登录用户名
        String userId = request.getRemoteUser();
        return orderService.findByPage(userId, params);
    }

}
