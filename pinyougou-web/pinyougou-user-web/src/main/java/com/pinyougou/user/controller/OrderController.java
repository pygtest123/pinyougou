package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Order;
import com.pinyougou.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class OrderController {

    @Reference(timeout = 10000)
    private OrderService orderService;

    /** 分页查询订单 */
    @GetMapping("/findByPage")
    public PageResult findByPage(Order order,Integer page,Integer rows){
            /** 获取登录商家编号 */
            String sellerId = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            /** 添加查询条件 */
            order.setSellerId(sellerId);

            /** 调用服务层方法查询 */
            return orderService.findByPage(order, page, rows);
        }


}
