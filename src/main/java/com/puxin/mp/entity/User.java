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
    private Date createTime;
    @TableField(exist = false)
    private String remark;
    @TableLogic // 进阶 这里面可以局部设置删除的状态,一般不用设置
    @TableField(select = false)//用来设置该字段不会再查询中出现
    private Integer deleteStatus;

}
