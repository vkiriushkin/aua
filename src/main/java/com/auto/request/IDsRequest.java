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

    private String REQUEST_TEMPLATE = "http://auto.ria.com/blocks_search_ajax/search/?" +
            "page=%d" +
            "&countpage=10" +
            "&category_id=1" +
            "&marka_id[0]=%d" +
            "&model_id[0]=%d" +
            "&currency=1" +
            "&state[0]=0" +
            "&city[0]=0" +
            "&fuelRatesType=city";

    private String FROM_YEAR_TEMPLATE = "&s_yers[0]=%d";
    private String TO_YEAR_TEMPLATE =  "&po_yers[0]=%d";

    private int markId;
    private int modelId;
    private int page;
    private int fromYear;
    private int toYear;

    private IDsRequest(Builder builder) {
        markId = builder.markId;
        modelId = builder.modelId;
        page = builder.page;
        fromYear = builder.fromYear;
        toYear = builder.toYear;
    }

    public String getIDs() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if (fromYear != 0 && !REQUEST_TEMPLATE.contains(FROM_YEAR_TEMPLATE))
            REQUEST_TEMPLATE = REQUEST_TEMPLATE.concat(String.format(FROM_YEAR_TEMPLATE, fromYear));
        if (toYear != 0 && !REQUEST_TEMPLATE.contains(TO_YEAR_TEMPLATE))
            REQUEST_TEMPLATE = REQUEST_TEMPLATE.concat(String.format(TO_YEAR_TEMPLATE, toYear));

        HttpGet httpget = new HttpGet(String.format(REQUEST_TEMPLATE, page, markId, modelId));
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


    public static final class Builder {
        private int markId;
        private int modelId;
        private int page;
        private int fromYear;
        private int toYear;

        public Builder() {
        }

        public Builder markId(int markId) {
            this.markId = markId;
            return this;
        }

        public Builder modelId(int modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder fromYear(int fromYear) {
            this.fromYear = fromYear;
            return this;
        }

        public Builder toYear(int toYear) {
            this.toYear = toYear;
            return this;
        }

        public IDsRequest build() {
            return new IDsRequest(this);
        }
    }
}
