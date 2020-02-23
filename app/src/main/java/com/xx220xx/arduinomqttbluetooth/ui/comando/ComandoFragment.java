package com.xx220xx.arduinomqttbluetooth.ui.comando;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xx220xx.arduinomqttbluetooth.R;
import com.xx220xx.arduinomqttbluetooth.TelaControle;
import com.xx220xx.arduinomqttbluetooth.TelaGeral;
import com.xx220xx.arduinomqttbluetooth.TelaHome;
import com.xx220xx.arduinomqttbluetooth.lua.Comando;
import com.xx220xx.arduinomqttbluetooth.sources.LoadComandsLua;
import com.xx220xx.arduinomqttbluetooth.sources.Nomeavel;
import com.xx220xx.arduinomqttbluetooth.sources.RecyclerViewAdapter;

import java.io.IOException;
import java.util.List;


public class ComandoFragment extends Fragment {

    private RecyclerView comandos;
    private RecyclerViewAdapter adapter;
    private List<Nomeavel> list;

    private ProgressBar progressBar;
    private TextView status;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comando, container, false);
        comandos = root.findViewById(R.id.frag_comando_lista_comandos);
        progressBar = root.findViewById(R.id.frag_comando_progress);
        status = root.findViewById(R.id.frag_comando_status);



        LoadComandsLua ld = new LoadComandsLua() {
            @Override
            protected void onPostExecute(List list) {loaded(list);}

            @Override
            protected void onProgressUpdate(String... values) {
                status.setText(values[0]);

            }
        };
        try {
            ld.execute(getContext().getResources().getAssets().open("Comando.lua"), getContext().getObbDir().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public void loaded(List l) {
        this.list = l;
        adapter = new RecyclerViewAdapter(getContext(), list);
        adapter.setClickListener((View v,int index)->{
            TelaControle.c = (Comando) adapter.getItem(index);
            TelaControle.comunicacao = TelaHome.comunicacao;
            TelaHome.comunicacao=null;
            startActivity(new Intent(getContext(), TelaControle.class));

            getActivity().finish();
        });
        comandos.setLayoutManager(new LinearLayoutManager(getContext()));
        comandos.setAdapter(adapter);
        status.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}