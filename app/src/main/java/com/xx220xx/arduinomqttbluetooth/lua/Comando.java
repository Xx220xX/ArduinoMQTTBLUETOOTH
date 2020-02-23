package com.xx220xx.arduinomqttbluetooth.lua;

import com.xx220xx.arduinomqttbluetooth.sources.Nomeavel;

import org.luaj.vm2.LuaValue;

public class Comando extends JLuaClass implements Nomeavel {

    public static final String LuaClass = "" +
            "_SMT_CODES = { plug = 4 }\n" +
            "Comando = {}\n" +
            "Comando.__index = Comando\n" +
            "function Comando.getParam(code)\n" +
            "    return _SMT_CODES[code]\n" +
            "end\n" +
            "function Comando.new(n_args)\n" +
            "    return setmetatable({ name = '', code = '', descricao = '', n_args = n_args, args = {} }, Comando)\n" +
            "end\n";


    /**
     * @param name : é  o nome do lclass.
     * @param code : é o codigo  a ser enviado.
     * @param n_args: numero de argumentos que ele recebe, cada um desses pode ser checado quanto a sua validade
     * @param args:
     */

    private Args[] args;

    public Comando(LuaValue lval) {
        super(lval);
        int n_args = getN_args();
        this.args = new Args[n_args];
        for (int i = 1; i <= n_args; i++) {
            args[i - 1] = new Args(lval.get("args").get(i));
        }
    }

    @Override
    public String getName() {
        return lclass.get("name").tojstring();
    }

    public String getDescricao() {
        return lclass.get("descricao").tojstring();
    }

    public String getCode() {
        return lclass.get("code").tojstring();
    }

    public int getN_args() {
        return lclass.get("n_args").toint();
    }

    public Args[] getArgs() {
        return args;
    }

    public boolean valido() {
        if (lclass.get("valido").isnil()) return true;
        return lclass.get("valido").call().toboolean();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Comando{" +
                "name='" + getName() + '\'' +
                ", descricao='" + getDescricao() + '\'' +
                ", code='" + getCode() + '\'' +
                ", n_args=" + getN_args() +
                ", args={ ");

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) s.append("null ,");
            else
                s.append(args[i].toString()).append(" ,");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1) + " }");

        return s.toString();
    }


}
