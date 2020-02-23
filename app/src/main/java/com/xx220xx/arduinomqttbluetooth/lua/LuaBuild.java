package com.xx220xx.arduinomqttbluetooth.lua;


import android.util.Log;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;

public class LuaBuild {
    Globals g;

    public LuaBuild() {
        g = JsePlatform.standardGlobals();
    }


    public LuaValue loadfile(File file) {
        return loadfile(file.getAbsolutePath());
    }

    public LuaValue loadfile(String fileAbsulePath) {
        File f = new File(fileAbsulePath);
        LuaValue v = g.loadfile(fileAbsulePath);
        return v.call();
    }

    public LuaValue load(String source) {
        LuaValue chunck = g.load(source);
        return chunck.call();
    }


}
