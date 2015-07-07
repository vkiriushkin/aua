package com.auto.load.db;

import com.auto.persistence.AbstractJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static UserManager userManager;
    private final AbstractJDBCTemplate jdbcTemplate;

    private static final String TABLE_NAME = "USER";
    private static final String ID_AUTOINCREMENT = "ID";
    private static final String USERNAME_COLUMN = "USERNAME";
    private static final String PASSWORD_COLUMN = "PASSWORD";

    private static final String SELECT_USER_ID = "SELECT ID FROM AUTOSTAT.USER WHERE USERNAME=? AND PASSWORD=?";
    private static final String INSERT_NEW_USER = "INSERT INTO AUTOSTAT.USER (USERNAME,PASSWORD) " +
            "VALUES(?,?)";


    private UserManager() {
        //TODO:change to spring injection
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        jdbcTemplate = (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");
    }

    public static UserManager getInstance() {
        if (userManager == null)
            userManager = new UserManager();

        return userManager;
    }

    public long getUserId(String username, String password) {
        long id = -1;
        try {
            id = jdbcTemplate.getJdbcTemplate().queryForObject(SELECT_USER_ID, new Object[]{username, password}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            //log warning that user wasn't found
        }
        return id;
    }

    public long createUser(String userName, String password) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_AUTOINCREMENT);
        Map<String, String> params = new HashMap();
        params.put(USERNAME_COLUMN, userName);
        params.put(PASSWORD_COLUMN, password);
        return insert.executeAndReturnKey(params).longValue();
    }

}
