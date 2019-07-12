package com.xcoder.iotserver.mqtt;

import android.content.Context;

import com.xcoder.iotserver.R;
import com.xcoder.iotserver.switcher.ISwitcher;
import com.xcoder.iotserver.utensil.Ctx;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Alibaba MQTT client holder.
 *
 * @author Chuck Lee
 */
public class MQTTHolder extends Thread {

    private Context context;

    private MQTTClient mqttClient;

    public MQTTHolder() {
        super.setPriority(MAX_PRIORITY);
        super.setDaemon(true);
    }

    public MQTTHolder(Context context) {
        this();
        this.context = context;
        final String clientId = this.context.getString(R.string.clientId);
        final String productKey = this.context.getString(R.string.productKey);
        final String deviceName = this.context.getString(R.string.deviceName);
        final String deviceSecret = this.context.getString(R.string.deviceSecret);
        this.mqttClient = MQTTClient.tryNew(clientId, productKey, deviceName, deviceSecret, 60);
    }

    @Override
    public void run() {
        try {
            this.mqttClient.connect();
            final String topicFilter = this.mqttClient.getTopicFilter(this.context.getString(R.string.myTopic));
            mqttClient.subscribe(topicFilter, 1, (topic, message) -> {
                String payload = new String(message.getPayload(), "utf-8");
                ISwitcher iSwitcher = (ISwitcher) Ctx.CONTEXT.get("iSwitcher");
                try {
                    iSwitcher.play(payload);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void interrupt() {
        try {
            this.mqttClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
