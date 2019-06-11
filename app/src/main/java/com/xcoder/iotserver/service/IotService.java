package com.xcoder.iotserver.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.xcoder.iotserver.server.IHandler;
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
        this.iSwitcher = new Switcher(this);
        this.iotServer = new IotServer((IHandler) this.iSwitcher);
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

        if (null != this.iSwitcher) {
            this.iSwitcher.release();
        }

        if (null != this.iotServer) {
            this.iotServer.interrupt();
        }
    }
}
