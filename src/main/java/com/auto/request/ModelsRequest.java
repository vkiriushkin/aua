package com.auto.request;

import org.apache.http.HttpEntity;
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
public class ModelsRequest {

    private static final String REQUEST_TEMPLATE = "http://auto.ria.com/api/categories/1/marks/%d/models/_with_count";
    private int markId;

    public ModelsRequest(int markId) {
        this.markId = markId;
    }

    public String getModels() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.format(REQUEST_TEMPLATE, markId));
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

        return responseBody;
    }
}
