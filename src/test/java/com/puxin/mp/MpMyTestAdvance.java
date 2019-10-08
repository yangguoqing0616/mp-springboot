package com.puxin.mp;


import com.puxin.mp.dao.UserMapper;
import com.puxin.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpMyTestAdvance {

    @Autowired
    private UserMapper userMapper;


    @Test
    public void test_01(){
        int rows = userMapper.deleteById(1180407105012961282L);
        System.out.println("------------>>>>>"+rows);
    }

    /*
    查询的sql语句会自动过滤掉状态是已删除的数据 更新同理
    但是自定义的sql语句不会自动在where后面增加任何条件
     */
    @Test
    public void test_02(){
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }
    /*
    自动填充测试
     */

    @Test
    public void test_insert03(){
        User user = new User();
        user.setEmail("3");
        user.setRealName("汪汪汪");
        user.setRemark("小狗");
        user.setAge(3);
        boolean insert = user.insert();
        System.out.println("-------->>"+insert);
        //int insert1 = userMapper.insert(user);
        //System.out.println("---------------"+insert1);
    }
    @Test
    public void test_update03(){
        User user = new User();
        user.setUserId(1180407105012961287L);
        user.setAge(33);
        int row = userMapper.updateById(user);
        System.out.println("---------------"+row);
    }

    @Test
    public void test_insert04(){
        User user = new User();
        user.setEmail("32");
        user.setRealName("汪2汪");
        user.setRemark("小狗");
        user.setAge(32);
        int insert1 = userMapper.insert(user);
        System.out.println("---------------"+insert1);
    }


}