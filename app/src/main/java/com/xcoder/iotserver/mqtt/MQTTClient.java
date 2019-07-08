package com.xcoder.iotserver.mqtt;

import com.xcoder.iotserver.utensil.AliyunIoTSignUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.Map;

public class MqCli extends MqttClient {

    private static final String CLIENT_ID_CONNECTOR = "|securemode=3,signmethod=hmacsha1,timestamp=";

    private static final String DEFAULT_SIGN_METHOD = "hmacsha1";

    private static final String CLIENT_ID_SUFFIX = "|";

    private static final String SERVER_URI_PROTOCOL = "tcp://";

    private static final String SERVER_URI_SUFFIX = ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

    private MqttConnectOptions mqttConnectOptions;

    private String productKey;

    private String deviceName;

    private String deviceSecret;

    private int keepAliveInterval;

    MqCli(final String clientId, final String productKey) throws MqttException {
        super(getServerURI(productKey), getSuperClientId(clientId));
        this.productKey = productKey;
        this.mqttConnectOptions = new MqttConnectOptions();
        this.mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        this.mqttConnectOptions.setAutomaticReconnect(true);
        this.mqttConnectOptions.setCleanSession(false);
    }

    public MqCli(String clientId, String productKey, String deviceName, String deviceSecret, int keepAliveInterval) throws MqttException {
        this(clientId, productKey);
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
        this.keepAliveInterval = keepAliveInterval;
        this.mqttConnectOptions.setKeepAliveInterval(this.keepAliveInterval);
    }

    @Override
    public void connect() throws MqttException {
        final String username = this.getUsername();
        final String password = this.getPassword();
        final char[] passwords = password.toCharArray();
        this.mqttConnectOptions.setUserName(username);
        this.mqttConnectOptions.setPassword(passwords);
        super.connect(mqttConnectOptions);
    }

    private String getUsername() {
        String username = this.deviceName.concat("&").concat(this.productKey);
        return username;
    }

    private String getPassword() {
        final String superClientId = super.getClientId();
        String timestamp = getTimestamp(superClientId);
        String clientId = getClientId(superClientId);

        Map<String, String> map = new HashMap<>(16);
        map.put("productKey", this.productKey);
        map.put("deviceName", this.deviceName);
        map.put("timestamp", timestamp);
        map.put("clientId", clientId);
        String password = AliyunIoTSignUtil.sign(map, this.deviceSecret, DEFAULT_SIGN_METHOD);
        return password;
    }

    static String getClientId(String superClientId) {
        String clientId = superClientId.substring(superClientId.indexOf(CLIENT_ID_SUFFIX));
        return clientId;
    }

    static String getTimestamp(String superClientId) {
        String timestamp = superClientId.substring(superClientId.lastIndexOf("="), superClientId.lastIndexOf(CLIENT_ID_SUFFIX));
        return timestamp;
    }


    static String getServerURI(String productKey) {
        String serverURI = SERVER_URI_PROTOCOL.concat(productKey).concat(SERVER_URI_SUFFIX);
        return serverURI;
    }

    static String getSuperClientId(String clientId) {
        String timestamp = System.currentTimeMillis() + "";
        String superClientId = clientId.concat(CLIENT_ID_CONNECTOR).concat(timestamp).concat(CLIENT_ID_SUFFIX);
        return superClientId;
    }
}
