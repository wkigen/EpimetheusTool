package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.github.wkigen.epimetheus.dex.Dex;

public class ApkComparator {

    public final static String DEX_SUFFIX = ".dex";
    public final static String DEX_PREFIX = "classes";
    
	public void ApkComparator() {
		
	}
	
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
		
		ZipFile oldApkZipFile = null;
		ZipFile newApkZipFile = null;
		try {
			
			oldApkZipFile = new ZipFile(oldApkFile);
			newApkZipFile = new ZipFile(newApkFile);
			
			ArrayList<Dex> oldDexList = new ArrayList<Dex>();
			ArrayList<Dex> newDexList = new ArrayList<Dex>();
			
			for (Enumeration<? extends ZipEntry> oldEntries = oldApkZipFile.entries(); oldEntries.hasMoreElements();) {
				 ZipEntry oldDexEntry = (ZipEntry) oldEntries.nextElement();
	             String zipEntryName = oldDexEntry.getName();
				 if(zipEntryName.startsWith(DEX_PREFIX) && zipEntryName.endsWith(DEX_SUFFIX)){
					 InputStream inputSteam = oldApkZipFile.getInputStream(oldDexEntry);
					 Dex dex =new Dex(inputSteam);
					 oldDexList.add(dex);
				 }
			}
			
			for (Enumeration<? extends ZipEntry> newEntries = newApkZipFile.entries(); newEntries.hasMoreElements();) {
				 ZipEntry newDexEntry = (ZipEntry) newEntries.nextElement();
	             String zipEntryName = newDexEntry.getName();
				 if(zipEntryName.startsWith(DEX_PREFIX) && zipEntryName.endsWith(DEX_SUFFIX)){
					 InputStream inputSteam = oldApkZipFile.getInputStream(newDexEntry);
					 Dex dex =new Dex(inputSteam);
					 newDexList.add(dex);
				 }
			}
	
			DexComparator dexComparator = new DexComparator();
			dexComparator.compare(oldDexList, newDexList);
			
		} catch (Exception e) {
			
		} finally{
			try {
				if(oldApkZipFile != null)
					oldApkZipFile.close();
			}catch(Exception e) {
				
			}
		
		}
		
		return false;
	}
}
