/*
 * システム名：Aquarea
 * 業務名：メッセージ情報の操作用のユーティリティ
 * ファイル名：DataSourceComConfig.java
 * 著作権表記：
 * Copyright (C) 2019 NCIT CORPORATION
 * 作成履歴
 * 20190902/NCIT/新規作成
 */
package club.dlblog.datasource.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源设定
 *
 * @author DavidLei
 */
@Configuration
@MapperScan(basePackages = {"club.dlblog.datasource.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    /**
     * 数据源设定
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * session工厂
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DynamicDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().
                        getResources("classpath:mapper/**/*.xml"));
        return sessionFactoryBean.getObject();

    }


    /**
     * session模板
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate ComSqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 动态数据源
     * @param dataSource
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("dataSource") DataSource dataSource){
        return  new DynamicDataSource(dataSource);
    }
}
