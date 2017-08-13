package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.extern.java.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Log
@Component
public class JLarrieuxRestClient extends RestClient {


    public JLarrieuxRestClient(){

    }

    public String getCount(){

        return getJsonAsString(Constants.BITTREX_JLARRIEUX_COUNT);
    }

    public String getMarketById(long id){
        log.info( Constants.BITTREX_JLARRIEUX_COM_MARKET_ID+id);
        return getJsonAsString(Constants.BITTREX_JLARRIEUX_COM_MARKET_ID+id);

    }

    @Override
    protected Response getResponseBody(String url) {
        Response response = null;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse httpResponse = client.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuffer resultBuffer = new StringBuffer();
            String line ;
            while((line = reader.readLine())!= null) resultBuffer.append(line);
            response = createResposeFromUrlResponse(resultBuffer.toString());
            response.setResponseCode(responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
