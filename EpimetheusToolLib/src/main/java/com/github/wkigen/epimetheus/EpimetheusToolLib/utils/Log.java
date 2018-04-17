package com.github.wkigen.epimetheus.EpimetheusToolLib.utils;

public class Log {

    public static void print(final String format, final Object... params){
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        System.out.printf(log);
    }
}
