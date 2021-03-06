package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * OrderService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface OrderService {

	/** 添加方法 */
	void save(Order order);

	/** 修改方法 */
	void update(Order order);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Order findOne(Serializable id);

	/** 查询全部 */
	List<Order> findAll();

	/** 从Redis数据库中获取支付日志 */
    PayLog findPayLogFromRedis(String userId);

    /** 支付成功，修改支付日志的状态、订单的状态 */
    void updatePayStatus(String outTradeNo, String transactionId);

	//查询订单
	List<Order> findOrder(String userId);

	//分页查询
	PageResult findByPage(String userId, Map<String, Object> params);
}