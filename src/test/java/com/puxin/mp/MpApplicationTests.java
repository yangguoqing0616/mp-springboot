package com.puxin.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.puxin.mp.dao.UserMapper;
import com.puxin.mp.entity.User;
import com.puxin.mp.service.impl.UserServiceImpl;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserServiceImpl userserviceImpl;


    /**
     * 注解讲解
     * @TableName();
     * 作用: 如果表名与entity或者beans 名不一致 则需要用改注解进行标识
     * vo 与dto 表示参数承载类
     *
     * @TableId()
     * 用来设置数据库表自增属性有5个枚举类型的类型, , , 一般都是自增的0
     *
     * @TableFiled()
     * 作用:1 value 用来映射实体属性名与表字段不一致的情况
     * 作用:2 exsit = false 用来标识该属性在表中是否存在
     * 作用:3 condition = condition = SqlCondition.LIKE 在用构造器创建条件查询对象时所有的都是等值
     *        查询,如果设置非等值的情况就用啊改属性
     *  作用:4 strategy =FieldStrategy.IGNORED 自动是空的时候是否被忽略
     */


    /**
     * 无条件全部查询 返回list
     */
    @Test
    public void testSelectList01() {
        //查询
        List<User> users = userMapper.selectList(null);
        Assert.assertEquals(5, users.size());
    }

    /**
     * 普通插入
     * serRemark数据库不存在的字段需要用注解tableField(exist = false)标注
     */

    @Test
    public void testInsert02() {
        //普通插入
        User user = new User();
        user.setRealName("小李");
        user.setCreateTime(LocalDateTime.now());
        user.setAge(16);
        user.setManagerId(1088248166370832385L);
        user.setRemark("我是一个标记不是表字段");
        int insert = userMapper.insert(user);
        Assert.assertEquals(1, insert);
    }


    /**
     * 创建条件查询器的两种方法
     * 1,QueryWrapper query = new QueryWrapper();
     * 2, QueryWrapper<User> query  = Wrapper.query();
     * <p>
     * 条件查询起查询 姓名中有雨并且年龄在20 到40之间
     */

    @Test
    public void test3() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();

        QueryWrapper<User> query = Wrappers.query();
        query.like("real_name", "雨").between("age", 20, 40).isNotNull("email");
        List<User> users = userMapper.selectList(query);
        for (User user : users) {
            System.out.print(user);
        }
        Assert.assertEquals(2, users.size());
    }

    /**
     * 需求: 姓王或者年龄大于等于25,按照年龄降序,年龄相同按照id升序
     * <p>
     * mybitis-plus 默认所有的条件都是and关键字进行连接的,如果是或的需要用
     * 查询器的方法or(),最下面有关于什么时候有用and()方法与or()方法的解释
     */

    @Test
    public void test4() {

        QueryWrapper<User> query = Wrappers.query();
        query.likeRight("real_name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("user_id");

        List<User> users = userMapper.selectList(query);
        for (User user : users) {
            System.out.println(user);
        }
        //Assert.assertEquals(2,users.size());
    }

    /**
     * sql函数 sql apply()方法 子查询 用insql()方法
     * 需求: 创建日期为2019年2月14日并且直属上级为名字为王姓
     * 注意在用函数是需要用index 防止 sql注入
     */
    @Test
    public void test5() {
        QueryWrapper<User> query = Wrappers.query();
        query.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14").inSql("manager_id", "select user_id from tb_user where real_name like concat('王','%')");
        List<User> users = userMapper.selectList(query);
        for (User user : users) {
            System.out.println(user);
        }
    }


    /**
     * 展示条件查询器and()方法的用法
     * 需求:姓名是王姓并且(年龄小于40 或者邮箱不为空)
     */

    @Test
    public void test6() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.likeRight("real_name", "王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 展示: 条件查询器or()的用法
     * 需求:姓名是王姓或者(年龄小于40并且年龄大于20并且邮箱不为空)
     */

    @Test
    public void test7() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("real_name", "王").or(qw -> qw.between("age", 20, 40).isNotNull("email"));

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 展示 条件查询器  nested()方法
     * nested正常嵌套下没有and or
     * <p>
     * 需求: (年龄小于40或邮箱不为空) 并且名字为王姓
     */
    @Test
    public void test8() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.nested(wr -> wr.lt("age", 40).or().isNotNull("email")).likeRight("real_name", "王");
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 需求:年龄为 31 32 34 35
     */

    @Test
    public void test9() {
        //带有lamda表达式的查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        List<Integer> integers = Arrays.asList(31, 32, 34, 35);
        wrapper.in("age", integers);
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 需求:年龄为 31 32 34 35 只返回一条数据
     * 注意last()方法有sql注入危险
     */

    @Test
    public void test10() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        List<Integer> listAge = Arrays.asList(31, 32, 34, 35);
        wrapper.in("age", Arrays.asList(31, 32, 34, 35)).last(true, "limit 1");
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 需求: 姓名包含雨 并且年龄小于40 (查询指定字段)
     */
    @Test
    public void test11() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.select("user_id", "age").like("real_name", "雨").lt("age", 40);

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 需求: 姓名包含雨 并且年龄小于40 (排除字段查询)
     */

    @Test
    public void test12() {


        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id")).like("real_name", "雨").lt("age", 40);
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }


    /**
     * 创建条件构造器时传入实体提对象
     * 就是在创建QueryWrapper()对象是传入相应的实体,,这样直接把传入得实体的值
     * 当成where条件里面等值条件,
     * 例如: 实体设置了name属性值where 条件后是ranl_name = "XXX"
     * 如果需要模糊查询则需要用标签@TableField(condition = SqlCondition.LIKE)
     * 使相应的值变为不等值查询...
     * 创建构造器时写入的值与使用对象的相应方法设置的值可以同时使用互不影响
     * <p>
     * 个人感觉这样写并不太好,,,因为这样写需要改相应的实体类
     * <p>
     * 需求: 姓名包含雨 并且年龄小于40 (排除字段查询)
     */
    @Test
    public void test13() {

        User us = new User();
        us.setRealName("中国");
        QueryWrapper<User> wrapper = new QueryWrapper<>(us);

        wrapper.like("real_name", "雨").lt("age", 40);

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test14() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.like("real_name", "雨").lt("age", 40);

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 方法 allEq()用法
     */
    @Test
    public void test15AllEq() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        HashMap<String, Object> param = new HashMap<>();

        param.put("real_name", "王天风03");
        param.put("age", 299);
        //以上是等值查询
        param.put("email", null);//如果是空,在生成的sql语句中是is null

        wrapper.allEq(param, false);//如果是true是则null为is null ,false 时忽略null
        //wrapper.allEq((k, v) -> k.equals("real_name"), param, true); // 过滤函数,是否允许字段传入比对条件中,也可以通过v进行过滤

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 查询方法selectMaps();
     * 返回list<map<String,Object>>> String 是数据库表字段的名称
     * 带有下划线的情况下需要别名处理
     */

    @Test
    public void test16SelectMaps() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.select("user_id as id", "age").like("real_name", "雨").eq("age", 32);

        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        for (Map<String, Object> user : maps) {
            System.out.println(user);
        }
    }

    @Test
    public void test17SelectMaps() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.select("avg(age) age,min(age) minage").groupBy("manager_id").having("sum(age)<{0}", 500);

        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        for (Map<String, Object> user : maps) {
            System.out.println(user);
        }
    }

    /**
     * lambda 表达式构造器 共4种,前三种
     * 需求: (年龄小于40或邮箱不为空) 并且名字为王姓
     */
    @Test
    public void testLambda18() {

        //创建lambda表达式的三种方式
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.nested(wr -> wr.lt(User::getAge, 40).or().isNotNull(User::getEmail)).likeRight(User::getRealName, "王");
        List<Map<String, Object>> maps = userMapper.selectMaps(objectLambdaQueryWrapper);
        for (Map<String, Object> user : maps) {
            System.out.println(user);
        }
    }

    /**
     * lambda 表达式构造器 共4中 第4种
     * 需求: (年龄小于40或邮箱不为空) 并且名字为王姓
     */
    @Test
    public void testLambda18_1() {

        //创建lambda表达式的第4种方式
        List<User> list = new LambdaQueryChainWrapper<>(userMapper).likeRight(User::getRealName, "王").gt(User::getAge, "20").list();
        for (User user : list) {
            System.out.println(user);
        }
    }

    /**
     * 总结 什么时候用and() 什么时候用or() 内写表达式
     * 默认从左向右依次走自动拼接and, 如果条件需要分开几个条件单独在一起时查询需要and()方法,,,
     * 不用and的: 姓名中有雨并且年龄在20 到40之间 and 自动给拼接
     * 用and: 姓名是王姓并且(年龄小于40 或者邮箱不为空) 括号内的条件是一个整体时用and() 方法
     *
     * 依次从左向右默认循序往下走就是条件是or就用or,,如果需要条件单独在一个时就用or(写参数)
     *  用or()但是不写表达式的: 姓王或者年龄大于等于25,按照年龄降序,年龄相同按照id升序
     *  用or()需要写表达式的: 姓名是王姓或者(年龄小于40并且年龄大于20并且邮箱不为空)
     */

    /**
     * 自定义sql
     */
    @Test
    public void testSql19() {
        LambdaQueryWrapper<User> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.nested(wr -> wr.lt(User::getAge, 40).or().isNotNull(User::getEmail)).likeRight(User::getRealName, "王");
        List<User> list = userMapper.selectAll(objectLambdaQueryWrapper);

        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testSql20() {
        LambdaQueryWrapper<User> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.nested(wr -> wr.lt(User::getAge, 40).or().isNotNull(User::getEmail)).likeRight(User::getRealName, "王");
        List<Map<String, Object>> maps = userMapper.selectAlla(objectLambdaQueryWrapper);

        for (Map<String, Object> user : maps) {
            System.out.println(user);
        }
    }

    /**
     * 分页查询 返回实体类型
     */
    @Test
    public void testSql21() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();

        wrapper.gt(User::getAge, 20);

        Page<User> page = new Page<>(1, 2);

        IPage<User> userIPage = userMapper.selectPage(page, wrapper);
        List<User> records = userIPage.getRecords();
        long total = userIPage.getTotal();
        long current = userIPage.getCurrent();

        for (User user : records) {
            System.out.println(user);
        }
        //如果不想查询总条数可以在new Page<>(3,2);的第三个参数写false表示不查总条数
        Page<User> page1 = new Page<>(3, 2, false);
    }

    /**
     * 分页查询 返回Map类型
     */
    @Test
    public void testSql21_1() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(User::getAge, 10);
        Page<User> page = new Page<>(1, 2);
        IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(page, wrapper);
        List<Map<String, Object>> records = userIPage.getRecords();
        long total = userIPage.getTotal();
        long current = userIPage.getCurrent();
        for (Map<String, Object> user : records) {
            System.out.println(user);
        }
        //如果不想查询总条数可以在new Page<>(3,2);的第三个参数写false表示不查总条数
        Page<User> page1 = new Page<>(3, 2, false);
    }
    /**
     * 自定义sql分页查询 返回User类型
     */
    @Test
    public void testSql21_2() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(User::getAge, 5);
        Page<User> page = new Page<>(1, 2,false);
        IPage<Map<String, Object>> userIPage = userMapper.selectMyUserPage(page, wrapper);
        List<Map<String, Object>> records = userIPage.getRecords();
        long total = userIPage.getTotal();
        long current = userIPage.getCurrent();
        for (Map<String, Object> user : records) {
            System.out.println(user);
        }
        //如果不想查询总条数可以在new Page<>(3,2);的第三个参数写false表示不查总条数
        Page<User> page1 = new Page<>(3, 2, false);
    }

    /**
     * 更行操作
     * 普通根据id更新
     */

    @Test
    public void testUpdate_01(){
        User user = new User();
        user.setUserId(1088248166370832385L);
        user.setAge(26);
        user.setRealName("王天风02");
        int i = userMapper.updateById(user);

    }

    /**
     * updateByWrapper
     *
     */
    @Test
    public void testUpdateByRapper(){
        User user = new User();
        user.setRealName("王天风03");
        user.setAge(277);
        UpdateWrapper<User> update = Wrappers.update();
        update.eq("real_name","王天风02").eq("user_id",1088248166370832385l);
        // user 里面的参数会放到set后面,,update里面的参数会放到where后面
        int update1 = userMapper.update(user, update);

        //可以在创建wrapper里面直接放置实体
        // 这样实体的内容会直接出现在where后面默认都是""等于的条件
        UpdateWrapper<Object> objectUpdateWrapper = new UpdateWrapper<>(user);

    }

    /**
     * 更新一个字段时的写法
     */
    @Test
    public void testUpdate(){

        UpdateWrapper<User> wrapper = Wrappers.update();
        wrapper.eq("real_name","王天风03").eq("age",277).set("age",288);
        int update = userMapper.update(null, wrapper);
        System.out.println(update);
    }

    @Test
    public void testUpdateLambda(){

        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getRealName,"王天风03").eq(User::getAge,288).set(User::getAge,299);
        int update = userMapper.update(null, wrapper);
        System.out.println(update);
    }

    /**
     * lambda 表达式调用
     */
    @Test
    public void testUpdateLambdaChain(){

        boolean boolea = new LambdaUpdateChainWrapper<User>(userMapper).eq(User::getRealName, "王天风03").set(User::getAge, 299).update();
        System.out.println(boolea);
    }

    // 删除方法

    //根据id删除记录
    @Test
    public void deleteById_1(){
        int i = userMapper.deleteById(1177520764803375106l);
        System.out.println(i);

    }
    //根据Map删除记录
    @Test
    public void deleteByMap_2(){
        Map<String, Object> obj = new HashMap<>();
        obj.put("real_name","小李");
        int i = userMapper.deleteByMap(obj);
        System.out.println(i);

    }

    //根据集合删除记录
    @Test
    public void deleteByMap_3(){
        List<Long> longs = Arrays.asList(1177508223112994818L, 1177504612903600129L);
        int i = userMapper.deleteBatchIds(longs);
        System.out.println(i);
    }
    //根据集合删除记录
    @Test
    public void deleteByWrapper_4(){
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getRealName,"刘红雨");
        int delete = userMapper.delete(wrapper);
        System.out.println(delete);
    }

    //AR 模式

    /**
     * AR模式 : 就是通过操作实体类 对该对象进行增删改查
     *
     * 1,实体类要继承一个model类泛型是当前继承对象
     * 2,要有mapper 并且继承BaseMapper
     */

    @Test
    public void testAR(){
        User user = new User();
        user.setRealName("王五吗");
        user.setAge(12);
        user.setRemark("假的吗");
        user.setManagerId(1088248166370832385L);
        user.setEmail("1414");
        boolean insert = user.insert();
        System.out.println(insert);

        // 删除 方法 这个id是对象里面的id
        //user.deleteById();
        // 插入或更新,,会根据id进行查询,,如果存在就是更新,如果不存在就是更新
        //user.insertOrUpdate();
    }

    //主键策略 如果全局策略不改变就是雪花算法
    /**
     * 主键定义在一个枚举类(IdType)里面就是共定义了5中类型
     * 全局配置id类型 注:局部配置高于全局配置
     */



    //通用service
    //如果有接口的话接口需要继承 Iservice
    //接口的实现类需要继承继承 ServiceImpl 然后实现 自己定义的接口

    @Test
    public void testService(){
        userserviceImpl.list();
        //接口的实现类需要继承继承 ServiceImpl 然后实现 自己定义的接口
    }

}