package com.jlarrieux.bittrexbot.REST;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.java.Log;





@Log
public abstract class RestClient {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();




}
