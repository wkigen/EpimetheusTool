package com.github.wkigen.epimetheus.EpimetheusToolLib.builder;

import com.github.wkigen.epimetheus.EpimetheusToolLib.comparator.DexComparator;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Utils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jf.dexlib2.builder.BuilderMutableMethodImplementation;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.writer.builder.BuilderField;
import org.jf.dexlib2.writer.builder.BuilderMethod;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;
import org.dom4j.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PatchBuilder {

    private final static String PatchPreFix = "patch";
    private final static String PatchEndFix = ".patch";
    private final static String PatchConfig = "patch.xml";
    private final static String DexEndFix = ".dex";
    private final static String OutPutPath = "../cache/patch/";

    private String version = "1.0.0";
    private Set<DexComparator.DexClassInfo> classList;
    private String outPutPatch = "";
    private boolean canHot = true;

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

    private void buildDex(final String ver) throws IOException {

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

    private void buildConfig(final String ver){

        XMLWriter xmlWriter = null;
        try{
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");

            outputFormat.setIndent(true);
            outputFormat.setIndent("    ");
            outputFormat.setNewlines(true);
            xmlWriter = new XMLWriter(new FileWriter(new File(OutPutPath+PatchConfig)),outputFormat);

            Document document = DocumentHelper.createDocument();

            Element rootElement = document.addElement("patch");

            Element versionElement = rootElement.addElement("version");
            versionElement.addText(version);

            Element patchNameElement = rootElement.addElement("patch_name");
            patchNameElement.addText(PatchPreFix+ver);

            Element canHotElement = rootElement.addElement("can_hot");
            canHotElement.addText(canHot?"true":"false");

            for (DexComparator.DexClassInfo dexClassInfo : classList){
                String className = dexClassInfo.dexBackedClassDef.getType().replace("/",".");
                className = className.substring(1,className.length()-1);
                Element classElement = rootElement.addElement("class");
                classElement.addText(className);
            }

            xmlWriter.write(document);
        }catch (Exception e){

        }finally {
            try {
                if (xmlWriter != null)
                    xmlWriter.close();
            }catch (Exception e){

            }
        }
    }

    public void Build(){

        try{
            File outPutPathFile = new File(OutPutPath);
            if (outPutPathFile.exists())
                Utils.deleteFilesInDirectory(outPutPathFile);
            else
                outPutPathFile.mkdir();

            final String ver = version.replace(".","");

            buildDex(ver);
            buildConfig(ver);

            Utils.zipPatch(OutPutPath,outPutPatch+PatchPreFix+ver+PatchEndFix);
        }catch (Exception e){
            Log.print(e.getMessage());
        }
    }

}
