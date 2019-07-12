package com.xcoder.iotserver.mqtt;

import com.xcoder.iotserver.utensil.AliyunIoTSignUtil;
import com.xcoder.iotserver.utensil.X;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Alibaba MQTT Client.
 *
 * @author Chuck Lee.
 */
public class MQTTClient extends MqttClient {

    private static final String CLIENT_ID_CONNECTOR = "|securemode=3,signmethod=hmacsha1,timestamp=";

    private static final String DEFAULT_SIGN_METHOD = "hmacsha1";

    private static final String CLIENT_ID_SUFFIX = "|";

    private static final String SERVER_URI_PROTOCOL = "tcp://";

    private static final String SERVER_URI_SUFFIX = ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

    private String clientId;

    private String productKey;

    private String deviceName;

    private String deviceSecret;

    private int keepAliveInterval;

    private String timestamp;

    private String topicFilterPrefix;

    public MQTTClient(final String clientId, final String productKey, final String deviceName
            , final String deviceSecret, final int keepAliveInterval, final String timestamp) throws MqttException {
        super(getServerURI(productKey), getSuperClientId(clientId, timestamp), new MemoryPersistence());
        this.clientId = clientId;
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
        this.keepAliveInterval = keepAliveInterval;
        this.timestamp = timestamp;
        this.topicFilterPrefix = "/".concat(this.productKey).concat("/").concat(this.deviceName).concat("/user");
    }

    public MQTTClient(final String clientId, final String productKey, final String deviceName
            , final String deviceSecret, final int keepAliveInterval) throws MqttException {
        this(clientId, productKey, deviceName, deviceSecret, keepAliveInterval
                , String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void connect() throws MqttException {
        this.connect(this.timestamp);
    }

    public final void connect(final String timestamp) throws MqttException {
        final String password = this.getPassword(timestamp);
        final char[] passwords = password.toCharArray();
        final String username = this.getUsername();

        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        mqttConnectOptions.setKeepAliveInterval(this.keepAliveInterval);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setPassword(passwords);
        mqttConnectOptions.setUserName(username);
        super.connect(mqttConnectOptions);
    }

    public String getTopicFilterPrefix() {
        return topicFilterPrefix;
    }

    public String getTopicFilter(final String topicFilter) {
        return this.topicFilterPrefix.concat(topicFilter);
    }

    private String getUsername() {
        String username = this.deviceName.concat("&").concat(this.productKey);
        return username;
    }

    private String getPassword(final String timestamp) {
        X.objectNotNull(timestamp);
        checkTimestamp(timestamp);
        Map<String, String> map = new HashMap<>(16);
        map.put("productKey", this.productKey);
        map.put("deviceName", this.deviceName);
        map.put("clientId", this.clientId);
        map.put("timestamp", timestamp);
        String password = AliyunIoTSignUtil.sign(map, this.deviceSecret, DEFAULT_SIGN_METHOD);
        return password;
    }

    private static String getServerURI(final String productKey) {
        String serverURI = SERVER_URI_PROTOCOL.concat(productKey).concat(SERVER_URI_SUFFIX);
        return serverURI;
    }

    private static String getSuperClientId(final String clientId, final String timestamp) {
        String superClientId = clientId.concat(CLIENT_ID_CONNECTOR)
                .concat(timestamp).concat(CLIENT_ID_SUFFIX);
        return superClientId;
    }

    private void checkTimestamp(final String timestamp) {
        if (null == this.timestamp) {
            return;
        }
        if (this.timestamp.equals(timestamp)) {
            return;
        }
        throw new RuntimeException("阿里巴巴MQTT客户端配置：客户端ID（clientId）的时间戳（timestamp）与密码（password）的时间戳（timestamp）不匹配，请检查！");
    }

    public static MQTTClient tryNew(final String clientId, final String productKey, final String deviceName
            , final String deviceSecret, final int keepAliveInterval) {
        try {
            final MQTTClient mqttClient = new MQTTClient(clientId, productKey
                    , deviceName, deviceSecret, keepAliveInterval);
//            mqttClient.connect();
            return mqttClient;
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
