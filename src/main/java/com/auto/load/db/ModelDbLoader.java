package com.auto.load.db;

import com.auto.data.Mark;
import com.auto.data.Model;
import com.auto.persistence.AbstractJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class ModelDbLoader {

    private static final String INSERT_MARK = "INSERT INTO AUTOSTAT.MARK (ID,NAME,COUNT) VALUES(?,?,?);";

    private static final String SELECT_MODELS_BY_MARK_ID = "SELECT * FROM AUTOSTAT.MODEL WHERE MARKID=?;";
    private static final String SELECT_MODELS_BY_MARK_NAME = "SELECT * from AUTOSTAT.MODEL " +
                                                             "WHERE MARKID=(SELECT ID FROM AUTOSTAT.MARK WHERE NAME=?);";

    private static final String SELECT_MARK_BY_ID = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK where ID=?";
    private static final String SELECT_MARK_BY_NAME = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK where NAME=?";

    private static ModelDbLoader dbLoader;

    private AbstractJDBCTemplate jdbcTemplate;

    public static ModelDbLoader getInstance() {
        if (dbLoader == null)
            dbLoader = new ModelDbLoader();

        return dbLoader;
    }

    private ModelDbLoader() {
        //TODO:change to spring injection
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        jdbcTemplate = (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");
    }

    public List<Model> loadModelsByMarkId(int markId) {
        List<Model> models  = jdbcTemplate.getJdbcTemplate().query(SELECT_MODELS_BY_MARK_ID, new BeanPropertyRowMapper(Model.class), markId);
        return models;
    }

    public List<Model> loadModelsByMarkName(String markName) {
        List<Model> models  = jdbcTemplate.getJdbcTemplate().query(SELECT_MODELS_BY_MARK_NAME, new BeanPropertyRowMapper(Model.class), markName);
        return models;
    }
}
