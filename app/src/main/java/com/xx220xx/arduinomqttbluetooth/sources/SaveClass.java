package com.xx220xx.arduinomqttbluetooth.sources;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveClass {
    public static interface OnLoad {
        void load(Object o);
    }

    public static void save(final String path, final String fileName, final Serializable o) {
        new Thread(() -> {
            try {
                FileOutputStream fout = new FileOutputStream(path + '/' + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(o);
                oos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ).start();
    }

    public static void load(final String path, final String fileName, Activity a, final OnLoad onLoad) {
        try {
            FileInputStream fin = new FileInputStream(path + '/' + fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            final Object o = ois.readObject();
            ois.close();
            a.runOnUiThread(() -> onLoad.load(o));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
