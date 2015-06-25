package com.auto.service;

import com.auto.data.Mark;
import com.auto.persistence.AbstractJDBCTemplate;
import com.auto.request.MarksRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vkiriushkin on 6/24/15.
 */
public class MarkService {

    private static final String INSERT_MARK = "INSERT INTO AUTOSTAT.MARK (ID,NAME,COUNT) VALUES(?,?,?);";
    private static final String SELECT_ALL_MARKS = "SELECT ID,NAME,COUNT FROM AUTOSTAT.MARK";

    public void updateAll(JdbcTemplate jdbcTemplate) throws IOException {

        MarksRequest marksRequest = new MarksRequest();
        String marksJSON = marksRequest.getModels();
        System.out.println(marksJSON);

        Gson gson = new Gson();
        Type marksListType = new TypeToken<List<Mark>>(){}.getType();
        List<Mark> marksList = gson.fromJson(marksJSON, marksListType);

        for (Mark mark : marksList) {
            jdbcTemplate.update(INSERT_MARK, mark.getId(), mark.getName(), mark.getCount());
        }
    }

    public List<Mark> getAllMarks(JdbcTemplate jdbcTemplate) {
        List<Mark> markList  = jdbcTemplate.query(SELECT_ALL_MARKS,
                new BeanPropertyRowMapper(Mark.class));
        return markList;
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("ApplicationContext.xml");

        AbstractJDBCTemplate abstractJDBCTemplate =
                (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");


        MarkService service = new MarkService();
        service.updateAll(abstractJDBCTemplate.getJdbcTemplate());

        List<Mark> allMarks = service.getAllMarks(abstractJDBCTemplate.getJdbcTemplate());
        for (Mark mark : allMarks) {
            System.out.println(mark.toString());
        }
    }
}
