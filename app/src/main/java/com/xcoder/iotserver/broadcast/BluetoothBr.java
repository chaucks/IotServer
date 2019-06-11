package com.xcoder.iotserver.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.xcoder.iotserver.utensil.X;

/**
 * Bluetooth broadcast
 *
 * @author Chuck Lee
 * @date 2019-06-11
 */
public class BluetoothBr extends BroadcastReceiver {

    private static final String[] BLUETOOTH_ACTION_INTENT_FILTER = new String[]{BluetoothDevice.ACTION_FOUND, BluetoothAdapter.ACTION_DISCOVERY_FINISHED};

    /**
     * 注册蓝牙事件广播
     *
     * @param context context
     */
    public final void registerReceiver(final Context context) {
        context.registerReceiver(this, X.getIntentFilter(BLUETOOTH_ACTION_INTENT_FILTER));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean actionMatch = X.actionMatch(intent, BluetoothDevice.ACTION_ACL_CONNECTED);
        boolean extraMatch = X.extraMatch(intent, BluetoothAdapter.EXTRA_STATE
                , BluetoothAdapter.ERROR, BluetoothAdapter.STATE_ON);

//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (actionMatch) {
            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Log.i("", "");
        }

    }
}
