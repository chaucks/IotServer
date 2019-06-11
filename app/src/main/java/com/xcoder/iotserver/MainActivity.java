package com.xcoder.iotserver;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xcoder.iotserver.broadcast.BluetoothBr;
import com.xcoder.iotserver.service.IotService;
import com.xcoder.iotserver.service.ServiceConn;

/**
 * Main Activity
 *
 * @author Chuck Lee
 * @date 2019-05-31
 */
public class MainActivity extends AppCompatActivity {

    private final ServiceConn serviceConn = new ServiceConn(IotService.class, Context.BIND_AUTO_CREATE);

    private final BluetoothBr bluetoothBr = new BluetoothBr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.serviceConn.bindService(this);
        this.bluetoothBr.registerReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.serviceConn.bindService(this);
        this.bluetoothBr.registerReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(this.serviceConn);
        this.unregisterReceiver(this.bluetoothBr);
    }
}
