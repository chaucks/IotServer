package com.xcoder.iotserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

/**
 * Main Activity
 *
 * @author Chuck Lee
 * @date 2019-05-31
 */
public class MainActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serviceIntentName = "com.xcoder.iotserver.service.IotService";
        Context context = this.getApplicationContext();

        this.serviceConnection = this.getServiceConnection();
        Intent intent = new Intent().setClassName(context, serviceIntentName);
        this.bindService(intent, this.serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != this.serviceConnection) {
            this.unbindService(this.serviceConnection);
        }
    }

    private ServiceConnection getServiceConnection() {
        final ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        return serviceConnection;
    }
}
