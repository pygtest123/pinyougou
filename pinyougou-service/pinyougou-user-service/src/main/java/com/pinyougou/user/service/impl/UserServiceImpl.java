package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${address.address}")
    private String address;
    @Value("${address.profession}")
    private String profession;


    @Override
    public void save(User user) {
        try{
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            // 添加数据
            userMapper.insertSelective(user);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /** 发送短信验证码 */
    public boolean sendSmsCode(String phone){
        try{
            // 1. 随机生成6位数字的验证码 95db9eb9-94e8-48e7-a5b2-97c622644e70
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-zA-Z]", "").substring(0,6);
            System.out.println("code= " + code);


            // 2. 调用短信发送接口(HttpClientUtils)
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 定义Map集合封装请求参数 18502903967
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("signName", signName);
            params.put("templateCode", templateCode);
            params.put("templateParam", "{'number' : '"+ code +"'}");
            // 发送post请求
            String content = httpClientUtils.sendPost(smsUrl, params);
            System.out.println("content = " + content);

            // 3. 判断短信是否发送成功，如果发送成功，就需要把验证存储到Redis(时间90秒)
            // {success : true}
            Map map = JSON.parseObject(content, Map.class);
            boolean success = (boolean)map.get("success");
            if (success){
                // 把验证存储到Redis(时间90秒)
                redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            }

            return success;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码是否正确 */
    public boolean checkSmsCode(String phone, String code){
        try{
            String oldCode = (String)redisTemplate.boundValueOps(phone).get();
            return code.equals(oldCode);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询省份信息 */
    public List<Provinces> findProvinces(){
            return userMapper.findProvinces();
    }

    /** 根据省份ID查询城市名称 */
    @Override
    public List<Cities> findCityByParentId(Long parentId) {
        try {
           return userMapper.findCityByParentId(parentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 根据父级cityId查询,得到区级分类名称 */
    @Override
    public List<Areas> findAreaByCityId(Long cityId) {
        return userMapper.findAreaByCityId(cityId);
    }

    /**　完善用户信息添加到tb_user表中　*/
    @Override
    public boolean savePersonToUser(User user) {
        try {
            //创建条件对象
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            //创建条件
            criteria.andEqualTo("username",user.getUsername());

            //判断数据库表是否存在用户地址字段
            int row = userMapper.isExists(address);
            if (row == 0){//表中没有该字段
                //动态添加字段
                userMapper.saveColumnName(address);
            }

            //判断数据库表是否存在用户职业字段
            int num = userMapper.isExists(profession);
            if (num == 0){//表中没有该字段
                //动态添加字段
                userMapper.saveColumnName(profession);
            }
            //执行修改,完善个人信息
            userMapper.updateByExampleSelective(user,example);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 更新用户密码
    @Override
    public User updatePassword(String userName, String newPassword) {
        try {
            User user = new User();
            user.setUsername(userName);
            User user1 = userMapper.selectOne(user);

            user1.setPassword(DigestUtils.md5Hex(newPassword));
            userMapper.updateByPrimaryKeySelective(user1);

            return user1;
        } catch (Exception e){
           throw  new RuntimeException(e);
        }
    }

    @Override
    public User UserInfo(String userName) {
        try {
            User user = new User();
            user.setUsername(userName);
            return userMapper.selectOne(user);

        } catch (Exception e){
           throw new RuntimeException(e);
        }

    }

    /**
     * 发送短信验证码
     */
    @Override
    public boolean sendCode(String phone) {
        try {
            /** 生成6位随机数 */
            String code = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]","")
                    .substring(0, 6);
            System.out.println("验证码：" + code);
            /** 调用短信发送接口 */
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 创建Map集合封装请求参数
            Map<String, String> param = new HashMap<>();
            param.put("phone", phone);
            param.put("signName", signName);
            param.put("templateCode", templateCode);
            param.put("templateParam", "{\"code\":\"" + code + "\"}");
            // 发送Post请求
            String content = httpClientUtils.sendPost(smsUrl, param);
            // 把json字符串转化成Map  content  = {"success":true\false}
            Map<String, Object> resMap = JSON.parseObject(content, Map.class);
            /** 存入Redis中(90秒) */
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean)resMap.get("success");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updatePhone(String userName,String phone) {
        try {
            // 查询到用户对象
            User user = new User();
            user.setUsername(userName);
            User user1 = userMapper.selectOne(user);
            //修改手机号
            user1.setPhone(phone);
            userMapper.updateByPrimaryKeySelective(user1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
