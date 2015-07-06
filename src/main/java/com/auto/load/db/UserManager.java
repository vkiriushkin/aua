package com.auto.load.db;

import com.auto.persistence.AbstractJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static UserManager userManager;
    private final AbstractJDBCTemplate jdbcTemplate;

    private static final String SELECT_CAR_BY_ID = "SELECT * FROM AUTOSTAT.CAR WHERE ID=?";
    private static final String SELECT_ALL_CARS = "SELECT * FROM AUTOSTAT.CAR";

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
        return -1;
    }

    public long createUser(String userName, String password) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
//                .withSchemaName("AUTOSTAT")
                .withTableName("USER")
                .usingGeneratedKeyColumns("ID");
        Map<String, String> params = new HashMap();
        params.put("USERNAME", userName);
        params.put("PASSWORD", password);
        return insert.executeAndReturnKey(params).longValue();
    }

}
