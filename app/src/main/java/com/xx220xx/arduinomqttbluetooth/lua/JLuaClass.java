package com.xx220xx.arduinomqttbluetooth.lua;

import org.luaj.vm2.LuaValue;

/**
 * é obrigatorio ter uma String final chamada LuaClass
 *
 * @example
 * public class Example extends JLuaClass{
 *      public static final LuaClass =
 *          "Example = {} \n"+
 *          "Example.__index = Example \n"+
 *          "function Example.new(var1) return setmetable({var1 = var1},Example} end ";
 *
 *      public getVar1(){
 *          return lclass.get("var1).tojstring();
 *      }
 * }
 */
public abstract class JLuaClass {
    protected LuaValue lclass;
    public JLuaClass(LuaValue lval){
        this.lclass = lval;

    }
}
