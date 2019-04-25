package com.pinyougou.mapper;

import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * UserMapper 数据访问接口
 * @date 2019-03-28 09:54:28
 * @version 1.0
 */
public interface UserMapper extends Mapper<User>{


    /** 查询省份信息 */
    List<Provinces> findProvinces();

    /** 根据父级ID查询城市名称 */
    @Select("select * from tb_cities where  provinceId= #{parentId}")
    List<Cities> findCityByParentId(Long parentId);

    /** 根据父级cityId查询,得到区级分类名称 */
    @Select("select * from tb_areas where cityId = #{cityId}")
    List<Areas> findAreaByCityId(Long cityId);
}