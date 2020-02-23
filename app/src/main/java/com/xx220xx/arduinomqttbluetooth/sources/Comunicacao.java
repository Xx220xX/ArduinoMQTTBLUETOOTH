package com.xx220xx.arduinomqttbluetooth.sources;

import android.app.Activity;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.luaj.vm2.ast.Str;

public abstract class Comunicacao {
    protected static Comunicacao atual = null;
    protected Comunicavel comunicacao;
    protected String myName;
    protected Activity activityAtual;

    public static void clear() {
        if (atual != null) atual.end();
        atual = null;
    }

    public String getMyName() {
        return myName;
    }

    public String getClientName() {
        return clientName;
    }

    protected String clientName;

    @Deprecated
    public void setComunicacao(Comunicavel comunicacao) {
        this.comunicacao = comunicacao;
    }

    public void setComunicacao(Activity activity, Comunicavel comunicacao) {
        this.activityAtual = activity;
        this.comunicacao = comunicacao;
    }

    public static Comunicacao now() {
        return atual;
    }

    public static void setNow(Comunicacao c) {
        if (atual != null) {
            atual.disconect(null);
        }
        atual = c;
    }

    public abstract void start(Object... args);

    public abstract void end();

    public abstract void send(String msg);

    public abstract void conect(Object[] args) throws Exception;

    public abstract boolean isConected();

    public abstract void disconect(String... args);
//    abstract  void onReceive(int REQUESTE,String msg);


    protected void onDisconect() {
        if (comunicacao != null)
            activityAtual.runOnUiThread(() -> comunicacao.onDisconect());
    }

    protected void onConnect(String msg) {
        if (comunicacao != null)
            activityAtual.runOnUiThread(() -> comunicacao.onConnect(msg));
    }

    protected void dadosReceived(String msg) {
        if (comunicacao != null)
            activityAtual.runOnUiThread(() -> comunicacao.dadosReceived(msg));
    }

    protected void Log(String msg) {
        if (comunicacao != null)
            activityAtual.runOnUiThread(() -> comunicacao.Log(msg));
    }

    protected void onRequestFinish(int request, Object result) {
        if (comunicacao != null)
            activityAtual.runOnUiThread(() -> comunicacao.onRequestFinish(request, result));
    }

    public interface Comunicavel {
        void onRequestFinish(int request, Object result);

        void dadosReceived(String dados);

        void onConnect(String name);

        void onDisconect();

        void Log(String msg);
    }
}