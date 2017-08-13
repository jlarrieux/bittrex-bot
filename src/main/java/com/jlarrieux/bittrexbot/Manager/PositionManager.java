package com.jlarrieux.bittrexbot.Manager;

import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.REST.BittrexRestClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log
@Component
public class PositionManager {

    private BittrexRestClient client;
    private Positions positionBooks;

    @Autowired
    private Markets markets;



    @Autowired
    public PositionManager(BittrexRestClient client, Positions positions){
        this.client = client;
        this.positionBooks = positions;
        getAllPositions();
    }


    private void getAllPositions(){
        Response response = client.getBalances();
        if(JsonParserUtil.isAsuccess(response)){
            iterateJsonArrayPositions(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
        }
    }

    private void iterateJsonArrayPositions(JsonArray array){
        Position position;
        for(int i=0; i<array.size(); i++){
            position = positionBooks.add(array, i);
            if(position!=null) log.info(String.format("%nPosition: %s", position.toString()));
        }

    }


}
