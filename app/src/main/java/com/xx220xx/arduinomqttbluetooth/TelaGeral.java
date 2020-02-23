package com.xx220xx.arduinomqttbluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xx220xx.arduinomqttbluetooth.sources.ImersiveAcitvity;
import com.xx220xx.arduinomqttbluetooth.sources.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TelaGeral extends ImersiveAcitvity {
    ProgressBar pb;
    LinearLayout lb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_geral);
        pb = findViewById(R.id.telageral_progress);
        lb = findViewById(R.id.telageral_layout_botoes);

    }

    public void startBluetooth(View v) {
        Intent screenBluetooth = new Intent(getApplicationContext(), TelaBluetooth.class);
//        Intent screenBluetooth = new Intent(getApplicationContext(), TelaHome.class);
        startActivity(screenBluetooth);
        finish();
    }

    public void startMqtt(View v) {
//        Intent screenMqtt = new Intent(getApplicationContext(),TelaMqtt.class);
        MQTT mqtt = new MQTT(this);
        pb.setVisibility(View.VISIBLE);
        lb.setVisibility(View.INVISIBLE);
        try {
            mqtt.conect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Toast.makeText(getApplicationContext(), "conectado", Toast.LENGTH_SHORT).show();
                    TelaHome.comunicacao = mqtt;
                    Intent screenBluetooth = new Intent(getApplicationContext(), TelaHome.class);
                    startActivity(screenBluetooth);
                    finish();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "falha: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.INVISIBLE);
                    lb.setVisibility(View.VISIBLE);

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
