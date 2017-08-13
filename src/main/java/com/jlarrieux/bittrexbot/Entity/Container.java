package com.jlarrieux.bittrexbot.Entity;

import com.google.gson.JsonArray;

public interface Container<T> {
    public T add(JsonArray array, int i);
}
