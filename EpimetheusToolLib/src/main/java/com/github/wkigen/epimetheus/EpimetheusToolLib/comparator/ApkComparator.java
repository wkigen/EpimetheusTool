package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import com.github.wkigen.epimetheus.EpimetheusToolLib.builder.PatchBuilder;
import com.github.wkigen.epimetheus.EpimetheusToolLib.common.Constant;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Utils;
import java.io.File;
import java.util.List;

public class ApkComparator {

	public DexComparator dexComparator ;

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
			Utils.unZipPatch(oldApkFile.getAbsolutePath(),Constant.OutPutCache+"old");
			Utils.unZipPatch(newApkFile.getAbsolutePath(),Constant.OutPutCache+"new");

			List<File> oldDexFile = Utils.findFils(new File(Constant.OutPutCache+"old"),"classes",Constant.DexEndFix);
			List<File> newDexFile = Utils.findFils(new File(Constant.OutPutCache+"new"),"classes",Constant.DexEndFix);

			dexComparator = new DexComparator(oldDexFile,newDexFile);
			dexComparator.compare();

		} catch (Exception e) {
			Log.print(e.getMessage());
		} finally{

		}
	}
}
