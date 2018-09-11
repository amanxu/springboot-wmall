package com.wmall.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//@Configuration
public class HibernateConfig {

    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }
}
