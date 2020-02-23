package com.xx220xx.arduinomqttbluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xx220xx.arduinomqttbluetooth.lua.Args;
import com.xx220xx.arduinomqttbluetooth.lua.Comando;
import com.xx220xx.arduinomqttbluetooth.sources.Comunicacao;
import com.xx220xx.arduinomqttbluetooth.sources.ImersiveAcitvity;

public class TelaControle extends ImersiveAcitvity {
    public static Comando c;
    public static Comunicacao comunicacao;
    RelativeLayout layout;
    Comando comando;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_controle);
        layout = findViewById(R.id.tela_controle_layout);
        info = findViewById(R.id.tela_controle_info);
        comando = c;
        c = null;

        if (comunicacao == null|| !comunicacao.isConected()) {
            Toast.makeText(getApplicationContext(),"Desconectado",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, TelaGeral.class);
            startActivity(intent);
            finish();
        }

        info.setText(comando.getName() + "\n" + comando.getDescricao());
        generateLayout();

      /*  comunicacao.setComunicacao(this,new Comunicacao.Comunicavel() {
            @Override
            public void onRequestFinish(int request, Object result) {

            }

            @Override
            public void dadosReceived(String dados) {
                Toast.makeText(getApplicationContext(), dados, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnect(String bluetoothName, boolean Isconected) {

            }

            @Override
            public void onDisconect() {
                Toast.makeText(getApplicationContext(), "Falha na comunicacao", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),TelaGeral.class));
                finish();
            }

            @Override
            public void Log(String msg) {

            }
        });*/
    }

    public void enviar(View v) {
        String msg = comando.getCode() + ": ";
        boolean error = false;
//        for (Args a : comando.getArgs()) {
//            if (!a.valido()) {
//                error = true;
//                Toast.makeText(getApplicationContext(), a.getError(), Toast.LENGTH_SHORT).show();
//                continue;
//            }
//            msg += a.getArgsCode();
//        }
//        if (error) return;

//        Toast.makeText(getApplicationContext(), "enviado " + msg, Toast.LENGTH_SHORT).show();
    }

    private void generateLayout() {
        LinearLayout l = new LinearLayout(getApplicationContext());
        l.setOrientation(LinearLayout.VERTICAL);
        for (Args a : comando.getArgs()) {
           a.addView(l,getApplicationContext());
        }
        layout.addView(l, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comunicacao = null;
        c = null;
    }

    @Override
    public void onBackPressed() {
        TelaHome.comunicacao = comunicacao;
        startActivity(new Intent(getApplicationContext(),TelaHome.class));
        finish();


    }
}
