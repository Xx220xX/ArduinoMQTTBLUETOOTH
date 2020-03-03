package com.xx220xx.arduinomqttbluetooth.ui.terminal;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.xx220xx.arduinomqttbluetooth.R;
import com.xx220xx.arduinomqttbluetooth.TelaGeral;
import com.xx220xx.arduinomqttbluetooth.sources.Comunicacao;

public class TerminalFragment extends Fragment {

    private Button sendMsg;
    private EditText input;
    private ListView receive;
    private ArrayAdapter<String> receiveAdpter;
    private Comunicacao comunicacao;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terminal, container, false);


        comunicacao = Comunicacao.now();
        if (comunicacao == null || !comunicacao.isConected()) {
            Toast.makeText(getContext(), "Desconectado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), TelaGeral.class);
            startActivity(intent);
            getActivity().finish();
        }
        sendMsg = root.findViewById(R.id.terminal_enviar);
        input = root.findViewById(R.id.terminal_input);
        receive = root.findViewById(R.id.terminal_mensagens);
        receiveAdpter = new ArrayAdapter<>(getContext(), R.layout.lista_simples);

        receive.setAdapter(receiveAdpter);
        receive.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String s = (String) parent.getItemAtPosition(position);
            if (s.contains(comunicacao.getClientName() + ": ")) {
                s = s.replace(comunicacao.getClientName() + ": ", "");
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("last msg", s);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(getContext(), R.string.copiado_para_area_de_tranferencia, Toast.LENGTH_SHORT).show();
            } else if (s.contains("> ")) {
                s = s.replace("> ", "");
                input.setText(s);
            }
        });

        sendMsg.setOnClickListener((View v) -> {
            String msg = input.getText().toString();
            comunicacao.send(msg);
            receiveAdpter.add("> " + msg);
            input.setText("");
        });


        comunicacao.setComunicacao(getActivity(), new Comunicacao.Comunicavel() {
            @Override
            public void onRequestFinish(int request, Object result) {

            }

            @Override
            public void dadosReceived(String dados) {
                receiveAdpter.add(comunicacao.getClientName() + ": " + dados);

            }

            @Override
            public void onConnect(String name) {

            }

            @Override
            public void onDisconect() {
                comunicacao.end();
                Comunicacao.clear();
                Toast.makeText(getContext(), "Desconectado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), TelaGeral.class));
                getActivity().finish();
            }

            @Override
            public void Log(String msg) {

            }
        });
        comunicacao.start(getActivity());
        return root;
    }
}