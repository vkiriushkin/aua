package com.auto.service;

import com.auto.data.Model;
import com.auto.load.db.ModelDbLoader;
import com.auto.load.web.ModelWebLoader;

import java.io.IOException;
import java.util.List;

/**
 * Created by vkiriushkin on 6/27/15.
 */
public class ModelService {

    private static final String INSERT_MODEL = "INSERT INTO AUTOSTAT.MODEL (ID,MARKID,NAME,COUNT) VALUES(?,?,?,?);";
    private static final String GET_MODELS_BY_MARK = "SELECT * FROM AUTOSTAT.MODEL WHERE MARKID=?;";

    private ModelDbLoader dbLoader;
    private ModelWebLoader webLoader;

    public ModelService() {
        initLoaders();
    }

    public void initLoaders() {
        dbLoader = ModelDbLoader.getInstance();
        webLoader = ModelWebLoader.getInstance();
    }

    public List<Model> getModelsByMarkId(int markId) {
        return dbLoader.loadModelsByMarkId(markId);
    }

    public List<Model> getModelsByMarkName(String markName) {
        return dbLoader.loadModelsByMarkName(markName);
    }

    public List<Model> loadAllModels(int markId) throws IOException {
        return webLoader.loadModels(markId);
    }

}
