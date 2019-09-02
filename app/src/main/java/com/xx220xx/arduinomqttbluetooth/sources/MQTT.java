package com.xx220xx.arduinomqttbluetooth.sources;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MQTT {
    String broker = "tcp://iot.eclipse.org:1883";
    String  client_UN="cliente_name";
    String  client_PN="password";
    String publish_topic ="topico pub";
    String subscribe_topic ="topico sub";
    String mensagem="none";
MqttAndroidClient client;
PahoMq
    public MQTT(){

    }
    public void onReceveMsg(String msg){

    }


}
    