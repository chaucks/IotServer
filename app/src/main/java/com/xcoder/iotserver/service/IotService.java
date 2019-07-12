package com.xcoder.iotserver.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xcoder.iotserver.mqtt.MQTTHolder;
import com.xcoder.iotserver.switcher.ISwitcher;
import com.xcoder.iotserver.switcher.Switcher;
import com.xcoder.iotserver.utensil.Ctx;

import static android.content.ContentValues.TAG;

/**
 * Iot Service
 *
 * @author Chuck Lee
 * @date 2019-06-03
 */
public class IotService extends Service {

    private IBinder iBinder = new Binder();

    private ISwitcher iSwitcher;

    private MQTTHolder mqttHolder;

//    private IotServer iotServer;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = super.getApplicationContext();

        this.iSwitcher = new Switcher();
        Ctx.CONTEXT.put("iSwitcher", this.iSwitcher);

        this.mqttHolder = new MQTTHolder(context);
        this.mqttHolder.start();

//        this.iotServer = new IotServer();
//        this.iotServer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.iSwitcher.release();
        this.mqttHolder.interrupt();
//        this.iotServer.interrupt();
        Ctx.CONTEXT.clear();
        Log.v(TAG, "Destroy......");
    }
}
