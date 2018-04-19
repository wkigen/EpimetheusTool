package com.github.wkigen.epimetheus.EpimetheusToolLib.utils;

import com.github.wkigen.epimetheus.EpimetheusToolLib.comparator.DexComparator;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.iface.MethodParameter;

import javax.rmi.CORBA.Util;

public class Log {

    public static void print(final String format, final Object... params){
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        System.out.printf(log+"\n");
    }

    public static void print(DexComparator.FixClassInfo fixClassInfo){
        print("class: %s",Utils.type2ClassName(fixClassInfo.fixClassDef.getType()));
        for (DexBackedMethod dexBackedMethod : fixClassInfo.fixMethod){
            StringBuffer methodStr = new StringBuffer(dexBackedMethod.getName() + "(");
            for (MethodParameter parameter : dexBackedMethod.getParameters()){
                methodStr.append(Utils.type2ClassName(parameter.getType())+" ");
            }
            methodStr.append(")");
            print("       method: %s",methodStr);
        }
    }
}
