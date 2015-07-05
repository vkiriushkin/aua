package com.auto.load.db;

import com.auto.data.Mark;
import com.auto.persistence.AbstractJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class MarkDbManager {

    private static final String INSERT_MARK = "INSERT INTO AUTOSTAT.MARK (ID,NAME,COUNT) VALUES(?,?,?);";
    private static final String SELECT_ALL_MARKS = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK";
    private static final String SELECT_MARK_BY_ID = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK where ID=?";
    private static final String SELECT_MARK_BY_NAME = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK where NAME=?";

    private static MarkDbManager dbManager;

    private AbstractJDBCTemplate jdbcTemplate;

    public static MarkDbManager getInstance() {
        if (dbManager == null)
            dbManager = new MarkDbManager();

        return dbManager;
    }

    private MarkDbManager() {
        //TODO:change to spring injection
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        jdbcTemplate = (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");
    }

    public List<Mark> loadAll() {
        List<Mark> markList  = jdbcTemplate.getJdbcTemplate().query(SELECT_ALL_MARKS, new BeanPropertyRowMapper(Mark.class));
        return markList;
    }

    public Mark loadMarkById(int markId) {
        Mark mark  = (Mark) jdbcTemplate.getJdbcTemplate().queryForObject(SELECT_MARK_BY_ID, new BeanPropertyRowMapper(Mark.class), markId);
        return mark;
    }

    public Mark loadMarkByName(String markName) {
        Mark mark  = (Mark) jdbcTemplate.getJdbcTemplate().queryForObject(SELECT_MARK_BY_NAME, new BeanPropertyRowMapper(Mark.class), markName);
        return mark;
    }

}
