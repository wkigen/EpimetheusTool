package com.github.wkigen.epimetheus.EpimetheusToolLib.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class Utils {

    public static List<File> findFils(File pathFile,String prefix,String endfix){
        List<File> files = new ArrayList<>();

        if (pathFile.isDirectory()){
            String[] children = pathFile.list();
            for (int i=0; i<children.length; i++) {
                if (children[i].startsWith(prefix) && children[i].endsWith(endfix)){
                    files.add(new File(pathFile,children[i]));
                }
            }
        }else {
            if (pathFile.getName().startsWith(prefix) &&
                    pathFile.getName().endsWith(endfix)){
                files.add(pathFile);
            }
        }

        return files;
    }

    public static void deleteFilesInDirectory(File pathFile){
        if (pathFile.exists()){
            if (pathFile.isFile()){
                pathFile.delete();
            }else if (pathFile.isDirectory()){
                String[] children = pathFile.list();
                for (int i=0; i<children.length; i++) {
                    deleteFilesInDirectory(new File(pathFile,children[i]));
                }
            }
        }
    }

    private static void compress(ZipOutputStream zipOutputStream,BufferedOutputStream bufferedOutputStream,File sourceFile,String base) throws Exception {
        if(sourceFile.isDirectory()) {
            File[] flist = sourceFile.listFiles();
            if(flist.length == 0 ){
                zipOutputStream.putNextEntry(  new ZipEntry(base+"/") );
            } else {
                for(File file : flist){
                    compress(zipOutputStream,bufferedOutputStream,file,base+"/"+file.getName());
                }
            }
        }else{
            zipOutputStream.putNextEntry( new ZipEntry(base) );
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int count = 0;
            byte[] buffer=new byte[1024];
            while((count = bis.read(buffer,0,1024))!=-1){
                zipOutputStream.write(buffer, 0, count);
            }
            bis.close();
            fos.close();
        }
    }


    public static void zipPatch(String zipSouPath,String zipDesPath){
        ZipOutputStream zipOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try{
            File souFile = new File(zipSouPath);
            if (!souFile.exists())
                return;
            File desFile = new File(zipDesPath);
            if (!desFile.exists())
                desFile.delete();

            zipOutputStream = new ZipOutputStream( new FileOutputStream(zipDesPath));
            bufferedOutputStream = new BufferedOutputStream(zipOutputStream);

            File[] flist = souFile.listFiles();
            for(File file : flist){
                compress(zipOutputStream,bufferedOutputStream,file,file.getName());
            }
        }catch (Exception e){

        }finally {
            try {
                if (zipOutputStream != null)
                    zipOutputStream.close();
                if (bufferedOutputStream != null)
                    bufferedOutputStream.close();
            }catch (Exception e){

            }
        }
    }

    public static void unZipPatch(String zipPath, String unZipPath){

        ZipFile zipFile = null;
        File pathFile = null;
        try{
            zipFile =  new ZipFile(new File(zipPath));
            pathFile = new File(unZipPath);

            if (!pathFile.exists())
                pathFile.mkdirs();
            else
                deleteFilesInDirectory(pathFile);

            InputStream in = null;
            FileOutputStream  out = null;
            for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();

                in = zipFile.getInputStream(entry);
                String outPath = (unZipPath+ "/" + zipEntryName).replaceAll("\\*", "/");

                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }

                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        }catch (Exception e){
            Log.print(e.getMessage());
        }finally {
            try{
                if (zipFile != null)
                    zipFile.close();
            }catch (Exception e){

            }
        }
    }

    public static String type2ClassName(String type){
        switch (type){
            case "Z":
                return "boolean";
            case "B":
                return "byte";
            case "C":
                return "char";
            case "S":
                return "short";
            case "I":
                return "int";
            case "J":
                return "long";
            case "F":
                return "float";
            case "D":
                return "double";
            case "V":
                return "void";
             default:
                if (type == null || type.length() < 2)
                    return "";
                 String ret = type.replace("/",".");
                 ret = ret.substring(1,ret.length()-1);
                 return ret;
        }
    }

}
