package com.puxin.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_user")
public class User extends Model<User> {
    @TableId(type = IdType.AUTO)
    private Long userId;
    @TableField(value = "real_name",condition = SqlCondition.LIKE,strategy =FieldStrategy.IGNORED)
    private String realName;
    private Integer age;
    private String email;
    private Long managerId;

    //用来标注 该属性不是表里的字段
    @TableField(exist = false)
    private String remark;

    /*
      进阶篇
     */
    /*
    @TableLogic用改注解标识该属性是逻辑删除
    这里面可以局部设置删除的状态,一般不用设置
    需要写一个配置类,SqlInJecter
     */
    @TableLogic
    @TableField(select = false)//用来设置该字段不会再查询中出现
    private Integer deleteStatus;
    // 跟新时直接更新时间 需要写配置类进行配置,写一个类实现 MetaObjectHandler
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    //插入时直接插入时间 需要写配置类进行配置 需要写一个类实现MetaObjectHandler
    @TableField(fill = FieldFill.INSERT)
    private Object createTime;

}
