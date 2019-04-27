package com.pinyougou.service;

import com.pinyougou.pojo.Address;
import java.util.List;
import java.io.Serializable;
/**
 * AddressService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface AddressService {

	/** 新建用户地址 */
	void save(Address address);

	/** 修改方法 */
	void update(Address address);

	/** 根据主键id删除地址 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Address findOne(Serializable id);

	/** 根据登录用户名查询全部 */
	List<Address> findAll(String userId);

	/** 多条件分页查询 */
	List<Address> findByPage(Address address, int page, int rows);

	/** 根据登录用户名获取收件地址列表 */
    List<Address> findAddressByUser(String userId);

	/**  修改默认地址(修改isDefault状态码) */
	void updateDefaultStatus(Long id);

}