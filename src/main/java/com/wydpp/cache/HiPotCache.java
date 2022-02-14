package com.wydpp.cache;

import com.wydpp.controller.model.HiPotQueue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class HiPotCache {

    private static final Map<String, HiPotQueue> CACHE_MAP = new ConcurrentHashMap<>();

    public static void set(String callId,HiPotQueue hiPotQueue){
        CACHE_MAP.put(callId,hiPotQueue);
    }

    public static HiPotQueue get(String callId){
        return CACHE_MAP.get(callId);
    }

}
