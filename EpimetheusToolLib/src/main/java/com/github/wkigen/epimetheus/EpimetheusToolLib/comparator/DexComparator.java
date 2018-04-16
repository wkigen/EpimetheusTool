package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.wkigen.epimetheus.dex.Annotation;
import com.github.wkigen.epimetheus.dex.ClassDef;
import com.github.wkigen.epimetheus.dex.Dex;
import com.github.wkigen.epimetheus.dex.TableOfContents;
import org.jf.dexlib2.dexbacked.util.AnnotationsDirectory;

public class DexComparator {

    private final Set<DexClassInfo> changeClassList = new HashSet<>();

	public DexComparator() {
		
	}

	private boolean compareClassDef(Dex oldDex,Dex newDex,ClassDef oldClassDef,ClassDef newClassDef){

	    if (oldClassDef.getAccessFlags() != newClassDef.getAccessFlags())
	        return false;

        int oldSourceFileIndex = oldClassDef.getSourceFileIndex();
        int newSourceFileIndex = newClassDef.getSourceFileIndex();
        if(!oldDex.strings().get(oldSourceFileIndex).equals(newDex.strings().get(newSourceFileIndex)))
            return false;

        int oldAnnotationOffset = oldClassDef.getAnnotationsOffset();
        int newAnnotationOffset = newClassDef.getAnnotationsOffset();
        Annotation oldAnnotations = oldDex.open(oldAnnotationOffset).readAnnotation();
        Annotation newAnnotations = newDex.open(newAnnotationOffset).readAnnotation();


	    return true;
    }
	
	public boolean compare(List<Dex> oldDexList,List<Dex> newDexList) {

	    for (Dex newDex : newDexList){
	        for (ClassDef newClassDef : newDex.classDefs()){
	            boolean isFind = false;
                for (Dex oldDex : oldDexList){
                    for (ClassDef oldClassDef : newDex.classDefs()) {
                        String oldDesc = oldDex.typeNames().get(oldClassDef.getTypeIndex());
                        String newDesc = newDex.typeNames().get(newClassDef.getTypeIndex());
                        if (oldDesc.equals(newDesc)){
                            if (!compareClassDef(oldDex,newDex,oldClassDef,newClassDef)){
                                changeClassList.add(new DexClassInfo(newDex,newClassDef));
                            }
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind)
                        changeClassList.add(new DexClassInfo(newDex,newClassDef));
                }
            }
        }

		return false;
	}


    public static final class DexClassInfo {
        public ClassDef classDef = null;
        public Dex owner = null;

        public DexClassInfo(Dex owner,ClassDef classDef ){
            this.owner = owner;
            this.classDef = classDef;
        }
    }
}
