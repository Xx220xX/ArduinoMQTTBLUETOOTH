package com.xx220xx.arduinomqttbluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.xx220xx.arduinomqttbluetooth.sources.Bluetooth;
import com.xx220xx.arduinomqttbluetooth.sources.Comunicacao;

import java.util.Set;


public class TelaBluetooth extends Activity {

    private static final int REQUEST_COUARSE_LOCATION = 295;
    /*########### objetos para layout  ##############*/
    TextView status, procurar, camposStatus;
    ProgressBar att;
    Switch ativarBluetooth;
    LinearLayout campoLista;
    ListView listaDispositivosPareados;
    ListView listaDispositivosNovos;

    ImageButton voltar;
    /*uteis*/
    boolean finalizar = false;

    Runnable apagarCampoStatus;

    {
        apagarCampoStatus = new Runnable() {
            @Override
            public void run() {
                finalizar = true;
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (finalizar)
                    runOnUiThread(() -> camposStatus.setText(""));
            }
        };
    }

    /*array list*/
    ArrayAdapter<String> pairedDevices;
    ArrayAdapter<String> newDevices;

    /*intanciando Bluetooth*/
    Bluetooth bluetooth;
    {
        Bluetooth.clear();
        bluetooth = new Bluetooth(this);
        bluetooth.setComunicacao(this, new Comunicacao.Comunicavel(){
            @Override
            public void onRequestFinish(int request, Object result) {
                switch (request) {
                    case Bluetooth.REQUIRE_TURN_ON:
                        bluetoothEnable();

                        break;
                    case Bluetooth.REQUIRE_TURN_OFF:
                        bluetoothDisable();
                        break;
                    case Bluetooth.REQUIRE_STOP_SEARCH:
                        att.setVisibility(View.INVISIBLE);
                        procurar.setText(getString(R.string.parando));
                        procurar.setClickable(false);
                        break;
                    case Bluetooth.REQUIRE_SEARCH_STARTED:
                        newDevices.clear();
                        att.setVisibility(View.VISIBLE);
                        procurar.setText(getString(R.string.parar));
                        procurar.setClickable(true);
                        break;
                    case Bluetooth.REQUIRE_SEARCH_FINISHED:
                        att.setVisibility(View.INVISIBLE);
                        procurar.setText(getString(R.string.procurar));
                        procurar.setClickable(true);
                        break;
                    case Bluetooth.REQUIRE_SEARCH_FOUND_DEVICE:
                        BluetoothDevice device = (BluetoothDevice) result;
                        final String adress = device.getAddress();
                        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                            // ver se dispositivo é pareado
                            new Thread(() -> {
                                for (int i = 0; i < pairedDevices.getCount(); i++) {
                                    String s = pairedDevices.getItem(i);
                                    s = s.substring(s.length() - 17);
                                    if (s.equals(adress)) {
                                        final TextView tv = ((TextView) listaDispositivosPareados.getChildAt(i));
                                        runOnUiThread(() -> {
                                            if (tv != null)
                                                tv.setTextColor(Color.BLUE);
                                        });
                                        break;
                                    }
                                }
                            }).start();
                            break;
                        }
                        newDevices.add(device.getName() + "\n" + device.getAddress());
                        break;
                    case Bluetooth.REQUIRE_CONNECT_FAIL:
                        camposStatus.setText(result.toString());
                        listaDispositivosNovos.setOnItemClickListener(itemClick);
                        listaDispositivosPareados.setOnItemClickListener(itemClick);
                        if (!finalizar)
                            new Thread(apagarCampoStatus, getString(R.string.apagar_mensagem_status)).start();
                        break;
                    case Bluetooth.REQUIRE_CONNECT_START:
                        camposStatus.setText(getString(R.string.conectando));
                        listaDispositivosNovos.setOnItemClickListener(itemNotClick);
                        listaDispositivosPareados.setOnItemClickListener(itemNotClick);
                        break;


                }

            }

            @Override
            public void dadosReceived(String dados) {

            }


            @Override
            public void onConnect(String bluetoothName) {
                Toast.makeText(getApplicationContext(),"conectado a :"+bluetoothName,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),TelaHome.class));
                finish();
            }

            @Override
            public void onDisconect() {

            }

            @Override
            public void Log(String msg) {


            }
        });
    }

    private void pegarPareados() {
        Set<BluetoothDevice> pareados = bluetooth.getDevicesPaired();
        if (pairedDevices != null) pairedDevices.clear();
        pairedDevices = new ArrayAdapter<>(getApplicationContext(), R.layout.lista_simples);
        if (pareados.size() > 0) {
            for (BluetoothDevice device : pareados) {
                pairedDevices.add(device.getName() + "\n" + device.getAddress());
            }
        }
        listaDispositivosPareados.setAdapter(pairedDevices);
        bluetooth.aceitarComoServidor();
    }

    /*intanciando ouvintes*/
    CompoundButton.OnCheckedChangeListener changeBluetooth;
    View.OnClickListener search;
    AdapterView.OnItemClickListener itemClick;
    AdapterView.OnItemClickListener itemNotClick;

    {
        itemNotClick = (parent, view, position, id) -> {
        };

        itemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pararThreadCampoStatus();
                String device = (String) parent.getItemAtPosition(position);
                device = device.substring(device.length() - 17);
                bluetooth.conect(device);
            }
        };
        search = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pararThreadCampoStatus();
                if (bluetooth.isSearching()) {
                    bluetooth.pararBusca();

                } else {
                    pegarPareados();
                    bluetooth.buscarDispositivosProximos();
                }
            }
        };
        changeBluetooth = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pararThreadCampoStatus();
                if (isChecked) {
                    status.setText(getString(R.string.ativando));
                    ativarBluetooth.setClickable(false);
                    bluetooth.turnOn();
                } else {
                    ativarBluetooth.setClickable(false);
                    status.setText(getString(R.string.desativando));
                    bluetooth.pararBusca();
                    procurar.setVisibility(View.INVISIBLE);
                    campoLista.setVisibility(View.INVISIBLE);
                    bluetooth.turnOff();
                }
            }
        };
    }

    private void pararThreadCampoStatus() {
        camposStatus.setText("");
        finalizar = false;
    }


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bluetoothg);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        /*Pegando Views do layout*/
        status = findViewById(R.id.statusativado);
        procurar = findViewById(R.id.procurar);
        att = findViewById(R.id.atualizando);
        ativarBluetooth = findViewById(R.id.ativacao);
        campoLista = findViewById(R.id.campo);
        listaDispositivosPareados = findViewById(R.id.listadisppareados);
        listaDispositivosNovos = findViewById(R.id.listadispnovos);
        camposStatus = findViewById(R.id.statusConection);

        ativarBluetooth.setChecked(bluetooth.isEnable());
        ativarBluetooth.setOnCheckedChangeListener(changeBluetooth);
        procurar.setOnClickListener(search);

        newDevices = new ArrayAdapter<>(getApplicationContext(), R.layout.lista_simples);
        listaDispositivosNovos.setAdapter(newDevices);
        listaDispositivosPareados.setAdapter(pairedDevices);

        listaDispositivosPareados.setOnItemClickListener(itemClick);
        listaDispositivosNovos.setOnItemClickListener(itemClick);

        if (bluetooth.isEnable()) {
            bluetoothEnable();
        } else {
            bluetoothDisable();
        }

        if (bluetooth.isConected()) {
            startActivity(new Intent(getApplicationContext(), TelaHome.class));
            finish();
        }

        voltar = findViewById(R.id.voltarbluetooth);
        voltar.setOnClickListener(v -> onBackPressed());

//        startActivity(new Intent(getApplicationContext(), TelaComandos.class));
//        finish();

    }

    private void bluetoothEnable() {
        status.setText(R.string.ativado);
        procurar.setVisibility(View.VISIBLE);
        campoLista.setVisibility(View.VISIBLE);
        ativarBluetooth.setClickable(true);
        pegarPareados();
        procurar.callOnClick();

    }

    private void bluetoothDisable() {
        status.setText(R.string.desativado);
        ativarBluetooth.setClickable(true);
        campoLista.setVisibility(View.INVISIBLE);
        procurar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        Bluetooth.clear();
        finish();
    }
}