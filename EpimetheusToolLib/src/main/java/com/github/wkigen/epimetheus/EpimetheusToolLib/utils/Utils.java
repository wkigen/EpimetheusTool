package com.github.wkigen.epimetheus.EpimetheusToolLib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


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

    public static void deleteFiles(File pathFile){
        if (pathFile.exists()){
            if (pathFile.isFile()){
                pathFile.delete();
            }else if (pathFile.isDirectory()){
                String[] children = pathFile.list();
                for (int i=0; i<children.length; i++) {
                    deleteFiles(new File(pathFile,children[i]));
                }
                pathFile.delete();
            }
        }
    }

    public static void unZipPatch(String zipPath, String unZipPath){

        ZipFile zipFile = null;
        File pathFile = null;
        try{
            zipFile =  new ZipFile(new File(zipPath));
            pathFile = new File(unZipPath);

            deleteFiles(pathFile);

            if (!pathFile.exists())
                pathFile.mkdirs();

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

}
