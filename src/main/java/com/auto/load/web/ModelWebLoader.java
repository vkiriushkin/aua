package com.auto.load.web;

import com.auto.data.Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ModelWebLoader {

    private static ModelWebLoader webLoader;

    private static final String LOAD_MODELS_REQUEST = "http://auto.ria.com/api/categories/1/marks/%d/models/_with_count";

    private Gson gson = new Gson();

    private ModelWebLoader() {}

    public static ModelWebLoader getInstance() {
        if (webLoader == null)
            webLoader = new ModelWebLoader();

        return webLoader;
    }

    public List<Model> loadModels(int markId) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.format(LOAD_MODELS_REQUEST, markId));
        System.out.println("Executing request " + httpget.getRequestLine());

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String modelsJSON = httpclient.execute(httpget, responseHandler);

        Type modelsListType = new TypeToken<List<Model>>(){}.getType();
        List<Model> modelsList = gson.fromJson(modelsJSON, modelsListType);

        return modelsList;
    }

    public static void main(String[] args) throws IOException {
        ModelWebLoader modelWebLoader = new ModelWebLoader();
        List<Model> models = modelWebLoader.loadModels(98);
        System.out.println(models.toString());
    }
}
