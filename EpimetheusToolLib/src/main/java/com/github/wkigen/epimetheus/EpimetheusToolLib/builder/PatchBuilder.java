package com.github.wkigen.epimetheus.EpimetheusToolLib.builder;

import com.github.wkigen.epimetheus.EpimetheusToolLib.comparator.DexComparator;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Utils;
import org.jf.dexlib2.builder.BuilderMutableMethodImplementation;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.writer.builder.BuilderField;
import org.jf.dexlib2.writer.builder.BuilderMethod;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PatchBuilder {

    private final static String PatchPreFix = "patch";
    private final static String PatchEndFix = ".patch";
    private final static String DexEndFix = ".dex";
    private final static String OutPutPath = "../cache/patch/";

    private String version = "1.0.0";
    private Set<DexComparator.DexClassInfo> classList;
    private String outPutPatch = "";

    public PatchBuilder(){

    }

    public PatchBuilder Version(String version){
        this.version = version;
        return this;
    }

    public PatchBuilder OutPutPatch(String outPutPatch){
        this.outPutPatch = outPutPatch;
        return this;
    }

    public PatchBuilder DexBuilder(Set<DexComparator.DexClassInfo> classList){
        this.classList = classList;
        return this;
    }

    private void buildDex() throws IOException {
        final String ver = version.replace(".","");

        DexBuilder builder = DexBuilder.makeDexBuilder();
        for (DexComparator.DexClassInfo dexClassInfo : classList){
            DexBackedClassDef classDef = dexClassInfo.dexBackedClassDef;

            List<BuilderField> builderFields = new ArrayList<>();
            for (Field field : classDef.getFields()) {
                final BuilderField builderField = builder.internField(
                        field.getDefiningClass(),
                        field.getName(),
                        field.getType(),
                        field.getAccessFlags(),
                        field.getInitialValue(),
                        field.getAnnotations()
                );
                builderFields.add(builderField);
            }

            List<BuilderMethod> builderMethods = new ArrayList<>();
            for (Method method : classDef.getMethods()) {
                MethodImplementation methodImpl = method.getImplementation();
                if (methodImpl != null) {
                    methodImpl = new BuilderMutableMethodImplementation(builder, methodImpl);
                }
                BuilderMethod builderMethod = builder.internMethod(
                        method.getDefiningClass(),
                        method.getName(),
                        method.getParameters(),
                        method.getReturnType(),
                        method.getAccessFlags(),
                        method.getAnnotations(),
                        methodImpl
                );
                builderMethods.add(builderMethod);
            }

            builder.internClassDef(classDef.getType(),classDef.getAccessFlags(),classDef.getSuperclass(),
                    classDef.getInterfaces(),classDef.getSourceFile(),classDef.getAnnotations(),builderFields,builderMethods);
        }

        File dest = new File(OutPutPath+PatchPreFix+ver+DexEndFix);
        FileDataStore fileDataStore = new FileDataStore(dest);
        builder.writeTo(fileDataStore);
    }

    public void Build(){

        try{
            File outPutPathFile = new File(OutPutPath);
            if (outPutPathFile.exists())
                Utils.deleteFiles(outPutPathFile);
            outPutPathFile.mkdir();

            final String ver = version.replace(".","");

            buildDex();

            Utils.zipPatch(OutPutPath,outPutPatch+PatchPreFix+ver+PatchEndFix);
        }catch (Exception e){
            Log.print(e.getMessage());
        }
    }

}
