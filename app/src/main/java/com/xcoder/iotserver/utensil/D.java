package com.xcoder.iotserver.utensil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class D {

    /**
     * 获取蓝牙设备集合
     *
     * @return 蓝牙设备集合
     */
    public static Set<BluetoothDevice> getBluetoothDeviceSet() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return getBluetoothDeviceSet(bluetoothAdapter);
    }

    /**
     * 获取蓝牙设备集合
     *
     * @param bluetoothAdapter bluetoothAdapter
     * @return 蓝牙设备集合
     */
    public static Set<BluetoothDevice> getBluetoothDeviceSet(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        return bondedDevices;
    }


}
