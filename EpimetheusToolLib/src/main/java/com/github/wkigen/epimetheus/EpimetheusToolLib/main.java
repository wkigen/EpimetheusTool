package com.github.wkigen.epimetheus.EpimetheusToolLib;

import com.github.wkigen.epimetheus.EpimetheusToolLib.builder.PatchBuilder;
import com.github.wkigen.epimetheus.EpimetheusToolLib.comparator.ApkComparator;
import org.jf.dexlib2.writer.builder.DexBuilder;

public class main {

	private static void printHelp(){
		System.out.printf("params:\n");
		System.out.printf("three params:\n");
		System.out.printf("1.the path of old apk \n");
		System.out.printf("2.the path of new apk \n");
		System.out.printf("3.the path of patch apk \n");
	}
	
	public static void main(String[] args) {
//
//		if(args == null || args.length != 3 ) {
//			printHelp();
//			return;
//		}
//
//		ApkComparator apkComparator = new ApkComparator();
//		apkComparator.compare(args[0], args[1]);

		ApkComparator apkComparator = new ApkComparator();
		apkComparator.compare( "..\\testapk\\old.apk","..\\testapk\\new.apk");

		PatchBuilder patchBuilder = new PatchBuilder();
		patchBuilder.OutPutPatch("..\\").Version("1.0.1").DexBuilder(apkComparator.dexComparator.getChangeClassList()).Build();

	}
	
	
}
