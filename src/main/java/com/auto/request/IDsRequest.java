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
 * Created by vkiriushkin on 6/17/15.
 */
public class IDsRequest {

    private static final String REQUEST_TEMPLATE = "http://auto.ria.com/blocks_search_ajax/search/?page=%d&countpage=10&category_id=1&marka_id[0]=%d&model_id[0]=%d&currency=1&state[0]=0&city[0]=0&fuelRatesType=city";
    private int markId;
    private int modelId;
    private int page;

    public IDsRequest(int page, int markId, int modelId) {
        this.page = page;
        this.markId = markId;
        this.modelId = modelId;
    }

    public String getIDs() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.format(REQUEST_TEMPLATE, page, markId, modelId));
//        System.out.println("Executing request " + httpget.getRequestLine());

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
