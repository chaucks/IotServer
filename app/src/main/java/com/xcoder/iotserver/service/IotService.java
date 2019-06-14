package com.xcoder.iotserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.xcoder.iotserver.server.IotServer;
import com.xcoder.iotserver.switcher.ISwitcher;
import com.xcoder.iotserver.switcher.Switcher;

/**
 * Iot Service
 *
 * @author Chuck Lee
 * @date 2019-06-03
 */
public class IotService extends Service {

    private ISwitcher iSwitcher;

    private IotServer iotServer;

    private IBinder iBinder = new Binder();

    @Override
    public void onCreate() {
        super.onCreate();
        this.iotServer = new IotServer();
        this.iSwitcher = new Switcher();
        this.iotServer.start();
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
        this.iotServer.interrupt();
    }
}
