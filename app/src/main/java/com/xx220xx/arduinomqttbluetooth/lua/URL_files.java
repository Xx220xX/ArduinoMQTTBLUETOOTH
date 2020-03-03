package com.xx220xx.arduinomqttbluetooth.lua;

import android.app.Activity;
import android.util.Log;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class URL_files {
    public static String  urlBasicSources = "https://raw.githubusercontent.com/Xx220xX/ArduinoMQTTBLUETOOTH/master/lua/files2download.lua";
    public List<String[]> getUrl(LuaValue source){
        List<String[]> links = new ArrayList<>();
        int length = source.length();
        String padrao = source.get("linkFormat").tojstring();
        for(int i=1;i<length;i++){
            String name = source.get(i).tojstring();
            links.add(new String[]{name,".lua",String.format(padrao,name)});
            Log.e("url",name+" "+String.format(padrao,name));
        }
        return links;
    }
}
