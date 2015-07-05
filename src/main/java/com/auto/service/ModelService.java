package com.auto.service;

import com.auto.data.Model;
import com.auto.load.db.ModelDbManager;
import com.auto.load.web.ModelWebLoader;

import java.io.IOException;
import java.util.List;

/**
 * Created by vkiriushkin on 6/27/15.
 */
public class ModelService {

    private static final String INSERT_MODEL = "INSERT INTO AUTOSTAT.MODEL (ID,MARKID,NAME,COUNT) VALUES(?,?,?,?);";
    private static final String GET_MODELS_BY_MARK = "SELECT * FROM AUTOSTAT.MODEL WHERE MARKID=?;";

    private ModelDbManager dbManager;
    private ModelWebLoader webLoader;

    public ModelService() {
        initLoaders();
    }

    public void initLoaders() {
        dbManager = ModelDbManager.getInstance();
        webLoader = ModelWebLoader.getInstance();
    }

    public List<Model> getModelsByMarkId(int markId) {
        return dbManager.loadModelsByMarkId(markId);
    }

    public List<Model> getModelsByMarkName(String markName) {
        return dbManager.loadModelsByMarkName(markName);
    }

    public List<Model> loadAllModels(int markId) throws IOException {
        return webLoader.loadModels(markId);
    }

}
