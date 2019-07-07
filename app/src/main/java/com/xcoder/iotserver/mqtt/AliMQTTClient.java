package com.xcoder.iotserver.mqtt;

import android.util.Log;

import com.xcoder.iotserver.switcher.ISwitcher;
import com.xcoder.iotserver.utensil.AliyunIoTSignUtil;
import com.xcoder.iotserver.utensil.Ctx;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AliMQTTClient extends Thread {

    private String productKey;

    private String deviceName;

    private String deviceSecret;

    private MqttClient mqttClient;

    public AliMQTTClient() {
        super.setPriority(MAX_PRIORITY);
        super.setDaemon(true);
        this.productKey = "****";
        this.deviceName = "****";
        this.deviceSecret = "****";
    }

    public AliMQTTClient(String productKey, String deviceName, String deviceSecret) {
        this();
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
    }

    @Override
    public void run() {
        try {
            String clientId = "androidthings" + System.currentTimeMillis();

            Map<String, String> params = new HashMap<>(16);
            params.put("productKey", productKey);
            params.put("deviceName", deviceName);
            params.put("clientId", clientId);
            String timestamp = String.valueOf(System.currentTimeMillis());
            params.put("timestamp", timestamp);

            // cn-shanghai
            String targetServer = "tcp://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

            String mqttclientId = clientId + "|securemode=3,signmethod=hmacsha1,timestamp=" + timestamp + "|";
            String mqttUsername = deviceName + "&" + productKey;
            String mqttPassword = AliyunIoTSignUtil.sign(params, deviceSecret, "hmacsha1");

            connectMqtt(targetServer, mqttclientId, mqttUsername, mqttPassword);

            this.mqttClient.subscribe("****", 1, (topic, message) -> {
                ISwitcher iSwitcher = (ISwitcher) Ctx.CONTEXT.get("iSwitcher");
                try {
                    iSwitcher.play("/".concat(new String(message.getPayload(), "utf-8")));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void connectMqtt(String url, String clientId, String mqttUsername, String mqttPassword) throws Exception {
        MemoryPersistence persistence = new MemoryPersistence();
        this.mqttClient = new MqttClient(url, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // MQTT 3.1.1
        connOpts.setMqttVersion(4);
        connOpts.setAutomaticReconnect(true);
//        connOpts.setCleanSession(true);
        connOpts.setCleanSession(false);

        connOpts.setUserName(mqttUsername);
        connOpts.setPassword(mqttPassword.toCharArray());
        connOpts.setKeepAliveInterval(60);

        mqttClient.connect(connOpts);
        Log.d(TAG, "connected " + url);
    }

    public void release() {
        try {
            this.mqttClient.unsubscribe("****");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            this.mqttClient.disconnectForcibly();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        try {
            this.mqttClient.close(true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
