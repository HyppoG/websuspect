package com.test.vulnerableapp;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication(exclude= { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class, ErrorMvcAutoConfiguration.class })
@EntityScan ( basePackages = {"com.test.vulnerableapp"} )
public class TestVulnerableAppSsApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(TestVulnerableAppSsApplication.class, args);
	}
	
	@Autowired
	private Environment env;

	@Bean(name="entityManagerFactory")
	public LocalSessionFactoryBean sessionFactory() {
	   LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	   sessionFactory.setDataSource(restDataSource());
	   sessionFactory.setPackagesToScan(
	       new String[] { "com.test.vulnerableapp.model" }
	   );
	   sessionFactory.setHibernateProperties(hibernateProperties());

	   return sessionFactory;
	}

	@Bean(name = "dataSource")
	public DataSource restDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName("org.h2.Driver");
	    dataSource.setUrl("jdbc:h2:mem:testdb;MODE=MYSQL");
	    dataSource.setUsername("db_user");
	    dataSource.setPassword("db_password");
	    return dataSource;
	}

	Properties hibernateProperties() {
	    return new Properties() {
	        {
	            setProperty("hibernate.hbm2ddl.auto", "create");
	            setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	            setProperty("hibernate.hbm2ddl.import_files", "/data.sql");
	        }
	    };
	}

}

