package com.xx220xx.arduinomqttbluetooth.lua;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.xx220xx.arduinomqttbluetooth.R;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Arrays;

import static android.view.Gravity.CENTER;


public class Args extends JLuaClass {
    public static final String INT = "int", FLOAT = "float", STRING = "string", DROPDOWN = "dropDown";

    public  static final   String LuaClass = "" +
            "Args = {}\n" +
            "Args.__index = Args\n" +
            "function Args.new(name, type, descricao, error, validar)\n" +
            "    return setmetatable({ name = name, type = type, descricao = descricao, error = error, validar = validar,list={}}, Comando)\n" +
            "end";
    private View myView;

    public Args(LuaValue lval) {
        super(lval);
    }


    public String getName() {
        return lclass.get("name").tojstring();
    }

    public String getValue() {
        return lclass.get("value").tojstring();
    }

    public String getType() {
        return lclass.get("type").tojstring();
    }

    private String getDescricaoFromLua() {
        return lclass.get("descricao").tojstring();
    }

    private String getDescricao() {
        StringBuilder s = new StringBuilder(" ");

        String descricao = getDescricaoFromLua();
        for (int i = 0; i < descricao.length(); i += 30) {
            if (i + 30 < descricao.length()) {
                s.append(descricao.substring(i, i + 30)).append("\n");
            } else {
                s.append(descricao.substring(i)).append("\n");
            }
        }
        return s.substring(0, s.length() - 1);
    }



    private int getInputeType() {
        String type = getType();
        if (type.equals(STRING)) return InputType.TYPE_CLASS_TEXT;
        if (type.equals(INT))
            return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
        if (type.equals(FLOAT))
            return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        throw new IllegalArgumentException("é esperado um tipo para o argumento");
    }


    public void addView(LinearLayout l, Context context) {
        if (getType().equals(DROPDOWN)) {
            addDropDown(l, context);
            return;
        }
        EditText editText = new EditText(context);
        editText.setHint(getName() + ": " + getDescricao());
        float dez_dp = context.getResources().getDimension(R.dimen.dez_dp);
        editText.setGravity(CENTER);
        editText.setMinHeight((int) (dez_dp * 8));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 10;

        l.addView(editText, params);
        editText.setInputType(getInputeType());
        myView = editText;
    }

    private void addDropDown(LinearLayout l, Context context) {
        Spinner spinner = new Spinner(context);
        LuaTable list = (LuaTable) lclass.get("list");
        LuaValue[] names = list.keys();
        String[] sNames = new String[names.length];
        for (int i = 0; i < sNames.length; i++) sNames[i] = names[i].tojstring();
        Arrays.sort(sNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, sNames);
        spinner.setAdapter(adapter);
        l.addView(spinner, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myView = spinner;
    }


}
