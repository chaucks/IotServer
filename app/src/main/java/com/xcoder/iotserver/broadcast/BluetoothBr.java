package com.xcoder.iotserver.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xcoder.iotserver.utensil.X;

public class BluetoothBr extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        boolean actionMatch = X.actionMatch(intent, BluetoothDevice.ACTION_ACL_CONNECTED);
        boolean extraMatch = X.extraMatch(intent, BluetoothAdapter.EXTRA_STATE
                , BluetoothAdapter.ERROR, BluetoothAdapter.STATE_ON);


    }

}
