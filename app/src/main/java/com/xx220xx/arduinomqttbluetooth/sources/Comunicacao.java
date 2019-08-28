package com.xx220xx.arduinomqttbluetooth.sources;

public abstract class Comunicacao {
    protected static Comunicacao atual = null;

    public abstract void send(String msg);
    abstract void conect(String[] args);
    abstract void disconect(String[] args);
    public  void onReceive(int REQUESTE,String msg){

    }
}
