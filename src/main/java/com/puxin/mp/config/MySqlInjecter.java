package com.puxin.mp.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该类是设置逻辑删除的相关内容
 */
@Configuration
public class MySqlInjecter {

    /**
     * 进阶 逻辑删除的实现 3.1.1以下的版本需要设置.我用的3.1.0
     *
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

}
