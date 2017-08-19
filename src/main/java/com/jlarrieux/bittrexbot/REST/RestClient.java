package com.jlarrieux.bittrexbot.REST;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;




public abstract class RestClient {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected abstract Response getResponseBody(String url);

    protected Response createResposeFromUrlResponse(String urlResponse) { // Creates a new Response object with the fields found in the result

        String successString = "\"success\":";
        int indexOfSuccessString = urlResponse.indexOf(successString) + successString.length();
        String strSuccess = urlResponse.substring(indexOfSuccessString, urlResponse.indexOf(",\"", indexOfSuccessString));


        String resultString = "\"result\":";
        int indexOfResultString = urlResponse.indexOf(resultString) + resultString.length();
        String result = urlResponse.substring(indexOfResultString, urlResponse.lastIndexOf("}"));

        String messageString = "\"message\":\"";
        int indexOfMessageString = urlResponse.indexOf(messageString) + messageString.length();
        String message = urlResponse.substring(indexOfMessageString, urlResponse.indexOf("\"", indexOfMessageString));

        boolean success = Boolean.parseBoolean(strSuccess);

        return new Response(success, result, message);
    }

    protected static String getJsonAsString(String url){
//        log.info("URL: "+ url);
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);
        client.destroy();

        return handleResponse(response);

    }


    private static String handleResponse(ClientResponse response){
        if(response.getStatus()!=200) throw  new RuntimeException("Failed: HTTP error code: "+ response.getStatus());
        else     return response.getEntity(String.class);

    }


}
