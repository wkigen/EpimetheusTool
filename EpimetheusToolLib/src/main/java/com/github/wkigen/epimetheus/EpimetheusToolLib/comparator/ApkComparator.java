package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import com.github.wkigen.epimetheus.EpimetheusToolLib.builder.PatchBuilder;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Utils;
import java.io.File;
import java.util.List;

public class ApkComparator {

	public DexComparator dexComparator = new DexComparator();

	public void compare(String oldApkPath,String newApkPatch) {
		
		File oldApkFile = new File(oldApkPath);
		File newApkFile = new File(newApkPatch);
		
		if(!oldApkFile.exists()) {
			System.out.printf("can not find the old apk");
			return;
		}
		
		if(!newApkFile.exists()) {
			System.out.printf("can not find the new apk");
			return;
		}

		try {
			final String outPath = "../cache/";
			Utils.unZipPatch(oldApkFile.getAbsolutePath(),outPath+"old");
			Utils.unZipPatch(newApkFile.getAbsolutePath(),outPath+"new");

			List<File> oldDexFile = Utils.findFils(new File(outPath+"old"),"classes",".dex");
			List<File> newDexFile = Utils.findFils(new File(outPath+"new"),"classes",".dex");

			dexComparator.compare(oldDexFile,newDexFile);

		} catch (Exception e) {
			Log.print(e.getMessage());
		} finally{

		}
	}
}
