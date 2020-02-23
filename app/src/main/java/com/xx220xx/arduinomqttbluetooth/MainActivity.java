package com.xx220xx.arduinomqttbluetooth;

import android.content.Intent;
import android.os.Bundle;

import com.xx220xx.arduinomqttbluetooth.sources.ImersiveAcitvity;

public class MainActivity extends ImersiveAcitvity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(getApplicationContext(),TelaGeral.class));
        finish();
    }
}
