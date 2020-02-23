package com.xx220xx.arduinomqttbluetooth.sources;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * see https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-paho-android-service/
 */
public class MQTT extends Comunicacao {
    String server = "ssl://iot.eclipse.org:1883";
    String clientId;
    MqttAndroidClient client;
    IMqttToken token;
    String topicPub = "esp32/icaugAnd";
    String topicSub = "esp32/icaugEsp";
    int qos = 1;

    public MQTT(Activity activityAtual) {
        clientId = MqttClient.generateClientId();
        this.activityAtual = activityAtual;
        client = new MqttAndroidClient(activityAtual.getApplicationContext(), server, clientId);


    }


    @Override
    public void start(Object... args) {
        activityAtual = (Activity) args[0];
        try {
            client.subscribe(topicSub, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                onDisconect();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                dadosReceived(new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }

    @Override
    public void end() {
        try {
            client.unsubscribe(topicSub);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void send(String msg) {
        byte[] encode;
        try {
            encode = msg.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            encode = msg.getBytes();
            Log.e("mqtt", "falha em codificar bytes");
            e.printStackTrace();
        }
        MqttMessage message = new MqttMessage(encode);
        message.setRetained(true);
        try {
            client.publish(topicPub, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void conect(Object... args) throws MqttException {
        /* // Outra maneira de conectar
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        IMqttToken token = client.connect(options);
        */
        IMqttActionListener actionListener;
        if (args.length > 0 && args[0] instanceof IMqttActionListener)
            actionListener = (IMqttActionListener) args[0];
        else actionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {

            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

            }
        };
        token = client.connect(activityAtual.getApplicationContext(), actionListener);


    }

    @Override
    public boolean isConected() {
        return client.isConnected();
    }

    @Override
    public void disconect(String[] args) {

    }
}
