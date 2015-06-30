package com.auto.service;

import com.auto.data.Mark;
import com.auto.load.db.MarkDbLoader;
import com.auto.load.web.MarkWebLoader;
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

    private MarkDbLoader dbLoader;
    private MarkWebLoader webLoader;

    public MarkService() {
        initLoaders();
    }

    public void initLoaders() {
        dbLoader = MarkDbLoader.getInstance();
        webLoader = MarkWebLoader.getInstance();
    }

    public void updateAll(JdbcTemplate jdbcTemplate) throws IOException {

//        MarksRequest marksRequest = new MarksRequest();
//        String marksJSON = marksRequest.getModels();
//        System.out.println(marksJSON);
//
//        Gson gson = new Gson();
//        Type marksListType = new TypeToken<List<Mark>>(){}.getType();
//        List<Mark> marksList = gson.fromJson(marksJSON, marksListType);
//
//        for (Mark mark : marksList) {
//            jdbcTemplate.update(INSERT_MARK, mark.getId(), mark.getName(), mark.getCount());
//        }
    }

    public List<Mark> getAllMarks() {
        List<Mark> markList  = dbLoader.loadAll();
        return markList;
    }

    public Mark getMarkById(int markId) {
        return dbLoader.loadMarkById(markId);
    }

    public Mark getMarkByName(String markName) {
        return dbLoader.loadMarkByName(markName);
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("ApplicationContext.xml");

        AbstractJDBCTemplate abstractJDBCTemplate =
                (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");


        MarkService service = new MarkService();
//        service.updateAll(abstractJDBCTemplate.getJdbcTemplate());

        Mark infinitiByName = service.getMarkByName("Infiniti");
        System.out.println(infinitiByName);

        Mark audiById = service.getMarkById(6);
        System.out.println(audiById);


        List<Mark> allMarks = service.getAllMarks();
        for (Mark mark : allMarks) {
            System.out.println(mark.toString());
        }
    }
}
