package com.xx220xx.arduinomqttbluetooth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xx220xx.arduinomqttbluetooth.lua.LuaBuild;
import com.xx220xx.arduinomqttbluetooth.lua.URL_files;
import com.xx220xx.arduinomqttbluetooth.sources.ImersiveAcitvity;

import org.luaj.vm2.LuaValue;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends ImersiveAcitvity {
    ProgressBar pb;
    ProgressBar porcentagem;
    TextView status;
    final int OK = 1, FAILED = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "install files from web server", Toast.LENGTH_LONG).show();
        status = findViewById(R.id.main_status);
        pb = findViewById(R.id.main_downloading);
        porcentagem = findViewById(R.id.main_progress);
//        porcentagem.setVisibility(View.INVISIBLE);
        load();


    }

    private void load() {
        new AsyncTask<Void, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                status.setText("verificando versão");
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    LuaBuild l = new LuaBuild();
                    LuaValue lv = l.loadfile(getObbDir() + "/version.smt");
                    if (lv == null)
                        return -1;
                    return lv.get("__version").toint();
                } catch (Exception e) {
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {

                download(integer);
            }
        }.execute();


    }

    private void download(Integer integer) {
        Download d = new Download() {
            @Override
            protected void onPostExecute(Integer s) {
                Toast.makeText(getApplicationContext(), "sucesso no download da versao", Toast.LENGTH_SHORT).show();
                new AsyncTask<Integer, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Integer... integers) {

                        try {
                            LuaBuild l = new LuaBuild();
                            LuaValue lv = l.loadfile(getObbDir() + "/version.smt");
                            return integers[0] != -1 && integers[0].equals(lv.get("__version").toint());
                        } catch (Exception e) {
                            return false;
                        }
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(getApplicationContext(), "Possui a mesma versao", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), TelaGeral.class));
                            finish();
                        } else {
                            downloadALL();
                        }
                    }
                }.execute(integer);
            }
        };
        status.setText("baixando arquivos");
        List<String[]> e = new ArrayList<>();
        e.add(new String[]{"version", ".smt", URL_files.urlBasicSources});
        d.execute(e);
    }

    private void downloadALL() {
        porcentagem.setVisibility(View.VISIBLE);
        LuaValue lv = new LuaBuild().loadfile(getObbDir() + "/version.smt");
        URL_files ur = new URL_files();
        List<String[]> files = ur.getUrl(lv);
        Download d = new Download() {
            @Override
            protected void onPostExecute(Integer integer) {
                startActivity(new Intent(getApplicationContext(), TelaGeral.class));
            }
        };
        d.execute(files);
    }

    class Download extends AsyncTask<List<String[]>, Integer, Integer> {
        final static int SET_MAX = 0, UPDATE = 1;

        @Override
        protected void onProgressUpdate(Integer... values) {
            porcentagem.post(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    porcentagem.setProgress(values[0].intValue(), true);
                } else {
                    porcentagem.setProgress(values[0].intValue());
                }
            });

        }

        /**
         * @param argsL tem duas posicoes, a primeira o nome do arquivo e a segunda o link
         * @return
         */
        @Override
        protected Integer doInBackground(List<String[]>... argsL) {
            for (String[] args : argsL[0]) {
                if (args.length != 3) return FAILED;
                String name = args[0] + args[1];
                runOnUiThread(() -> status.setText("baixando: " + args[0]));
                String s_url = args[2];
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), name + "  " + s_url, Toast.LENGTH_LONG).show());
                publishProgress(0);
                try {
                    URL url = new URL(s_url);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    long lengthOfFile = connection.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream outputStream = new FileOutputStream(getObbDir().getAbsolutePath() + "/" + name);
                    byte data[] = new byte[1024];
                    long total = 0;
                    int cout;
                    while ((cout = input.read(data)) != -1) {
                        total += cout;
                        publishProgress((int) (total * 100D / (lengthOfFile+1)));
                        outputStream.write(data, 0, cout);
                    }
                    outputStream.flush();
                    outputStream.close();
                    input.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return OK;
        }

    }
}
