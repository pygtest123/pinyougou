package com.pinyougou.service;

import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;

import java.io.Serializable;
import java.util.List;

/**
 * UserService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface UserService {

	/** 添加方法 */
	void save(User user);

	/** 修改方法 */
	void update(User user);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	User findOne(Serializable id);

	/** 查询全部 */
	List<User> findAll();

	/** 多条件分页查询 */
	List<User> findByPage(User user, int page, int rows);

	/** 发送短信验证码 */
	boolean sendSmsCode(String phone);

	/** 检验验证码是否正确 */
	boolean checkSmsCode(String phone, String code);


	// 更新用户密码
    User updatePassword(String userName, String newPassword);

	// 获取用户号码
	User UserInfo(String loginName);

	/** 查询省份信息 */
    List<Provinces> findProvinces();

	/** 根据省份ID查询城市名称 */
	List<Cities> findCityByParentId(Long parentId);

	/** 根据父级cityId查询,得到区级分类名称 */
	List<Areas> findAreaByCityId(Long cityId);

	/**　完善用户信息添加到tb_user表中　*/
    boolean savePersonToUser(User user);
}