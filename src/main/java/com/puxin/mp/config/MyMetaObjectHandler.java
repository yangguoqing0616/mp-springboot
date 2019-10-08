package com.puxin.mp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 设置自动填充时间 或者 修改人,
 * 改处理器在目标方法执行之前都会执行,,为了节约耗时及性能 可以用判断的方法进行处理
 */

@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        //如果当前插入的对象包含改字段就进行插入 如果不包含就不插入
        boolean createTime = metaObject.hasSetter("createTime");
        //一个参数是实体类的属性名
        if (createTime) {
            System.out.println("------insert方法");
            setInsertFieldValByName("createTime", new Date(), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //判断该属性是否已经设置我值,如果已经设置过就不用在设置了
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime != null) {
            System.out.println("-------update方法");
            setUpdateFieldValByName("updateTime", new Date(), metaObject);
        }

    }
}
