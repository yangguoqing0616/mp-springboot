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
     */
    @Test
    public void test_02(){
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }


}