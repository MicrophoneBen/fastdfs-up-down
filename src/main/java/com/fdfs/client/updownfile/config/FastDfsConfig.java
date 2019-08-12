package com.fdfs.client.updownfile.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @author zhangbingquan
 * @version 2019年08月12日
 * @since 2019年08月12日
 **/
@Configuration
@ComponentScan(value = "com.github.tobato.fastdfs.service")
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDfsConfig {
}
