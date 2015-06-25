package com.auto.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by vkiriushkin on 6/25/15.
 */
public class AbstractJDBCTemplate {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private static AbstractJDBCTemplate instance;

//    public static AbstractJDBCTemplate getInstance() {
//        if (instance == null) {
//            instance = new AbstractJDBCTemplate();
//        }
//        return instance;
//    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
