package com.xx220xx.arduinomqttbluetooth.sources;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.xx220xx.arduinomqttbluetooth.lua.Args;
import com.xx220xx.arduinomqttbluetooth.lua.Comando;
import com.xx220xx.arduinomqttbluetooth.lua.LuaBuild;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadComandsLua extends AsyncTask<Object, String, List> {


    @SuppressLint("WrongThread")
    @Override
    protected List doInBackground(Object... objects) {
        InputStream init =(InputStream) objects[0];
        String path = (String) objects[1];
        List l = new ArrayList();
        File[] files = new File(path).listFiles((dir, name) -> name.endsWith(".lua"));
        LuaBuild lua = new LuaBuild();
        lua.load(Args.LuaClass);
        lua.load(Comando.LuaClass);
        String status;
        for (File f : files) {
            publishProgress("abrindo " + f.getName());
            try {
                LuaValue v = lua.loadfile(f);
                status = "sucess";
                l.add(new Comando((LuaTable) v));
            } catch (Exception e) {
                status = "failed: " + e.getMessage();
            }
            publishProgress( status + f.getName());
        }
        return l;
    }
}
