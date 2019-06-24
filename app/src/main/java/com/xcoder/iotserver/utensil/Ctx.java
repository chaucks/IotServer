package com.xcoder.iotserver.utensil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Ctx {
    public static final Map CONTEXT = new ConcurrentHashMap(8);
}
