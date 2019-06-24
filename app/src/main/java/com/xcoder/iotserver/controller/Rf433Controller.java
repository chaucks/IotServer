package com.xcoder.iotserver.controller;

import com.xcoder.iotserver.switcher.ISwitcher;
import com.xcoder.iotserver.utensil.Ctx;

import java.util.Map;

public class Rf433Controller {

    public void control(Map<String, String> requestMap) throws Throwable {
        ISwitcher iSwitcher = (ISwitcher) Ctx.CONTEXT.get("iSwitcher");
        String Uri = requestMap.get("Uri");
        iSwitcher.play(Uri);
    }
    
}
