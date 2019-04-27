package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * 地址服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-20<p>
 */
@Service(interfaceName = "com.pinyougou.service.AddressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /** 新建用户地址 */
    @Override
    public void save(Address address) {
        try {
            //判断数据库表是否存在用户邮箱字段
            int row = addressMapper.isExists("email");
            if (row == 0){//表中没有该字段
                //动态添加字段
                addressMapper.saveColumnName("email");
            }
            addressMapper.insertSelective(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 修改用户地址 */
    @Override
    public void update(Address address) {
        try {
            addressMapper.updateByPrimaryKeySelective(address);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**  根据主键id删除地址  */
    @Override
    public void delete(Serializable id) {
        try {
            addressMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /**  根据ID查询地址数据回显 */
    @Override
    public Address findOne(Serializable id) {
        try {
         return  addressMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /** 根据登录用户名查询全部地址 */
    @Override
    public List<Address> findAll(String userId) {
        //创建条件对象
        Example example = new Example(Address.class);
        Example.Criteria criteria = example.createCriteria();

        //创建查询条件,user_id = userId
        criteria.andEqualTo("userId",userId);
        //  ORDER BY is_default DESC
        example.orderBy("isDefault").desc();

        //查询并返回
        return addressMapper.selectByExample(example);

    }

    @Override
    public List<Address> findByPage(Address address, int page, int rows) {
        return null;
    }

    @Override
    public List<Address> findAddressByUser(String userId) {
        try{
            // SELECT * FROM tb_address WHERE user_id = 'itcast' ORDER BY is_default DESC
            // 创建示范对象
            Example example = new Example(Address.class);
            // 创建条件对象
            Example.Criteria criteria = example.createCriteria();
            // user_id = 'itcast'
            criteria.andEqualTo("userId", userId);
            // ORDER BY is_default DESC
            example.orderBy("isDefault").desc();
            // 条件查询
            return addressMapper.selectByExample(example);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**  修改默认地址(修改isDefault状态码) */
    @Override
    public void updateDefaultStatus(Long id) {
        try {
            //根据is_default状态码取消原来的默认地址改为:is_default = 0;
            addressMapper.updateOldStatus();

            /** 重新设置选中的ID项为默认地址 */
            addressMapper.updateNewStatus(id);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
