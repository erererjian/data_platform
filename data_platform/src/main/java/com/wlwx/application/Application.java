package com.wlwx.application;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.wlwx.back.system.SystemInit;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * spring boot 初始化文件初始化文件
 * @author zjj
 * @date 2017年7月11日 下午4:42:48
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.wlwx"})
@MapperScan({"com.wlwx.dao"})
public class Application {
	public static final Logger LOGGER = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * 配置数据源
	 * @date 2017年7月11日 下午4:44:09
	 * @return
	 */
	@Bean(name = {"dataSource"})
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

	/**
	 * 配置session工厂
	 * @date 2017年7月11日 下午4:44:28
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Interceptor[] plugins = {pageHelper()};
		sqlSessionFactoryBean.setPlugins(plugins);
		sqlSessionFactoryBean.setMapperLocations(resolver
				.getResources("classpath:/mybatis/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	/**
	 * 配置事务
	 * @date 2017年7月11日 下午4:44:45
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	/**
	 * pageHelpper框架
	 * @date 2017年7月11日 下午4:43:42
	 * @return
	 */
	public static PageHelper pageHelper() {
		System.out.println("MyBatisConfiguration.pageHelper()");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

	/**
	 * 系统初始化
	 * @date 2017年7月11日 下午4:45:12
	 * @return
	 */
	@Bean
	public int systemInit() {
		System.out.println("systemInit");
		SystemInit.start();
		return 0;
	}
}
