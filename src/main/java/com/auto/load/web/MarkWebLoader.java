package com.auto.load.web;

import com.auto.data.Mark;
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

public class MarkWebLoader {

    private static MarkWebLoader webLoader;

    private static final String LOAD_MARKS_REQUEST = "http://auto.ria.com/api/categories/1/marks/_with_active_ads/_with_count";

    private Gson gson = new Gson();

    private MarkWebLoader() {}

    public static MarkWebLoader getInstance() {
        if (webLoader == null)
            webLoader = new MarkWebLoader();

        return webLoader;
    }

    public List<Mark> loadMarks() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(LOAD_MARKS_REQUEST);
        System.out.println("Executing request " + httpget.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String marksJSON = httpclient.execute(httpget, responseHandler);

        Type marksListType = new TypeToken<List<Mark>>(){}.getType();
        List<Mark> marksList = gson.fromJson(marksJSON, marksListType);

        return marksList;
    }


}
