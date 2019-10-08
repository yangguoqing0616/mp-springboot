package com.puxin.mp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 设置自动填充时间 或者 修改人,
 */

@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //一个参数是实体类的属性名
        System.out.println("------insert方法");
        setInsertFieldValByName("createTime", new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("-------update方法");
        setUpdateFieldValByName("updateTime",new Date(),metaObject);

    }
}
