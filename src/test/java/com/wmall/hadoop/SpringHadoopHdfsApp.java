package com.wmall.hadoop;

import org.apache.hadoop.fs.FileSystem;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: niexx <br>
 * @date: 2018-04-01 17:03 <br>
 */
public class SpringHadoopHdfsApp {

    private ApplicationContext ctx;
    private FileSystem fileSystem;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("spring-hadoop.xml");
        fileSystem = (FileSystem) ctx.getBean("fileSystem");
    }

    public void tearDown() {
    }
}
