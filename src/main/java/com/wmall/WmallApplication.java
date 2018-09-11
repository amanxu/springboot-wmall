package com.wmall;

import com.wmall.config.WechatInfoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication
@Configuration
@ComponentScan("com.wmall")
// 一定要过滤HibernateJpaAutoConfiguration.class，否则如果开启Hibernate二级缓存报错找不到hibernate.cache.region.factory_class的配置类
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
@EnableConfigurationProperties({WechatInfoConfig.class}) // 注入属性配置文件
@EnableScheduling
public class WmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmallApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean OpenSessionInViewFilterRegitrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        OpenSessionInViewFilter openSessionFilter = new OpenSessionInViewFilter();
        openSessionFilter.setSessionFactoryBeanName("mySessionFactory");
        filterRegistration.setFilter(openSessionFilter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        filterRegistration.setName("OpenSessionInViewFilter");
        return filterRegistration;
    }
}
