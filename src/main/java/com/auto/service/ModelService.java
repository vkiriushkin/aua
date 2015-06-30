package com.auto.service;

import com.auto.data.Mark;
import com.auto.data.Model;
import com.auto.persistence.AbstractJDBCTemplate;
import com.auto.request.ModelsRequest;
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
 * Created by vkiriushkin on 6/27/15.
 */
public class ModelService {

    private static final String INSERT_MODEL = "INSERT INTO AUTOSTAT.MODEL (ID,MARKID,NAME,COUNT) VALUES(?,?,?,?);";
    private static final String GET_MODELS_BY_MARK = "SELECT * FROM AUTOSTAT.MODEL WHERE MARKID=?;";


    public void updateModelsForMark(int markId, JdbcTemplate jdbcTemplate) throws IOException {
        ModelsRequest request = new ModelsRequest(markId);
        String models = request.getModels();
        System.out.println(models);

        Gson gson = new Gson();
        Type modelListType = new TypeToken<List<Model>>(){}.getType();
        List<Model> modelsList = gson.fromJson(models, modelListType);
        for (Model model : modelsList) {
            jdbcTemplate.update(INSERT_MODEL, model.getId(), markId, model.getName(), model.getCount());
        }
    }

    public void updateAll(JdbcTemplate jdbcTemplate) {

    }

    public List<Model> getModelsForMark(int markId, JdbcTemplate jdbcTemplate) {
        List<Model> models  = jdbcTemplate.query(GET_MODELS_BY_MARK,new BeanPropertyRowMapper(Model.class),markId);
        return models;
    }

    public static void main(String[] args) throws IOException {
        String requestedmark = "Acura";
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        AbstractJDBCTemplate abstractJDBCTemplate = (AbstractJDBCTemplate) context.getBean("autoJDBCTemplate");

        MarkService markService = new MarkService();
        List<Mark> markList = markService.getAllMarks(abstractJDBCTemplate.getJdbcTemplate());
        Mark m = markList.stream().filter(mark -> mark.getName().equalsIgnoreCase(requestedmark)).findFirst().get();

        System.out.println(m.toString());

        ModelService modelService = new ModelService();
        modelService.updateModelsForMark(m.getId(), abstractJDBCTemplate.getJdbcTemplate());

        List<Model> modelsForMark = modelService.getModelsForMark(m.getId(), abstractJDBCTemplate.getJdbcTemplate());
        System.out.println(modelsForMark);

    }


}
