package com.xx220xx.arduinomqttbluetooth.sources;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.Random;

public class MQTT {
    String broker = "tcp://iot.eclipse.org:1883";
    String client_UN_password = "cliente_name";
    String client_PN_username = "password";
    String publish_topic = "topico pub";
    String subscribe_topic = "topico sub";
    String mensagem = "none";
    String client_id = "";
    MqttAndroidClient androidClient;

    public MQTT() {
        this.client_id = "mqtt" + new Random().nextInt(5000 - 1) + 1;//1 ~ 4999
    }

    public void onReceveMsg(String msg) {

    }

    public void connect(Context context) {
        if (androidClient.isConnected())
            try {
                disconnect(androidClient);
            } catch (MqttException e) {

            }
        androidClient = getMqttClient(context, broker, client_id, client_PN_username, client_UN_password);
        callback();
    }
    public String  subscribe(String topic_sub,int qos){
        if(!androidClient.isConnected())
            return null;
        try {
            androidClient.subscribe(topic_sub,qos);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }
    private MqttAndroidClient getMqttClient(Context context, String broker, String client_id, String client_pn_username, String client_un_password) {
        final MqttAndroidClient temp = new MqttAndroidClient(context,broker,client_id);
        try{
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setAutomaticReconnect(true);
            options.setUserName(client_pn_username);
            options.setPassword(client_un_password.toCharArray());
            IMqttToken token =temp.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    temp.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.d("MQTT getClient","falha ");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private void callback(){

    }

    private void disconnect(MqttAndroidClient androidClient)throws MqttException {

    }

}
    