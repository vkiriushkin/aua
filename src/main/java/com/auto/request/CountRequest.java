package com.auto.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by vkiriushkin on 6/15/15.
 */
public class CountRequest {

    private static final String REQUEST_TEMPLATE = "http://auto.ria.com/blocks_search_ajax/count/?category_id=1&currency=1&marka_id[0]=%d&model_id[0]=%d&state[0]=0&countpage=10";
    private int modelId;
    private int markId;

    public CountRequest(int modelId, int markId) {
        this.modelId = modelId;
        this.markId = markId;
    }

    public int getIdsCount() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.format(REQUEST_TEMPLATE, markId, modelId));
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
        String responseBody = httpclient.execute(httpget, responseHandler);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        return jsonObject.get("count").getAsInt();
    }
}
