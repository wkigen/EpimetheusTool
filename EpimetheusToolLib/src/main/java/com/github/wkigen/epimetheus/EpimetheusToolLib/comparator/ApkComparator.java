package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Utils;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkComparator {

    public final static String DEX_SUFFIX = ".dex";
    public final static String DEX_PREFIX = "classes";
    

	public boolean compare(String oldApkPath,String newApkPatch) {
		
		File oldApkFile = new File(oldApkPath);
		File newApkFile = new File(newApkPatch);
		
		if(!oldApkFile.exists()) {
			System.out.printf("can not find the old apk");
			return false;
		}
		
		if(!newApkFile.exists()) {
			System.out.printf("can not find the new apk");
			return false;
		}

		try {
			final String outPath = "../cache/";
			Utils.unZipPatch(oldApkFile.getAbsolutePath(),outPath+"old");
			Utils.unZipPatch(oldApkFile.getAbsolutePath(),outPath+"new");

			List<File> oldDexFile = Utils.findFils(new File(outPath+"old"),"classes",".dex");
			List<File> newDexFile = Utils.findFils(new File(outPath+"new"),"classes",".dex");

			DexComparator dexComparator = new DexComparator();
			dexComparator.compare(oldDexFile,newDexFile);
		} catch (Exception e) {
			Log.print(e.getMessage());
		} finally{
		}
		
		return false;
	}
}
