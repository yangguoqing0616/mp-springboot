package com.puxin.mp.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxin.mp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from tb_user ${ew.customSqlSegment}") // ${ew.customSqlSegment} 为固定写法
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper wrapper);//@Param(Contants.WRAPPER) 为固定写法

    List<Map<String,Object>> selectAlla(@Param(Constants.WRAPPER) Wrapper wrapper);//@Param(Contants.WRAPPER) 为固定写法

    IPage<Map<String,Object>> selectMyUserPage(Page<User> page,@Param(Constants.WRAPPER) Wrapper wrapper);
}
