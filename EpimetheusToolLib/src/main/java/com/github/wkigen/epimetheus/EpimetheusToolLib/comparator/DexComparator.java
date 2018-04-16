package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.wkigen.epimetheus.dex.*;
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
        if(oldAnnotations.compareTo(newAnnotations) != 0)
            return false;

        if (oldClassDef.getClassDataOffset() != 0 && newClassDef.getClassDataOffset() != 0){
            ClassData oldClassData = oldDex.readClassData(oldClassDef);
            ClassData newClassData = newDex.readClassData(newClassDef);

            ClassData.Field[] oldFields = oldClassData.allFields();
            ClassData.Field[] newFields = newClassData.allFields();
            if (oldFields.length != newFields.length)
                return false;
            for (ClassData.Field newField : newFields){
                boolean isSame = false;
                for (ClassData.Field oldField : oldFields){
                    if (newField.getFieldIndex() == oldField.getFieldIndex()
                            && newField.getAccessFlags() == oldField.getAccessFlags()){
                        isSame = true;
                    }
                }
                if (!isSame)
                    return false;
            }

            ClassData.Method[] oldMethods = oldClassData.allMethods();
            ClassData.Method[] newMethods = newClassData.allMethods();
            if (oldMethods.length != newMethods.length)
                return false;
            for (ClassData.Method newMethod:newMethods){
                boolean isSame = false;
                for (ClassData.Method oldMethod:oldMethods){
                    if (oldMethod.getMethodIndex() == newMethod.getMethodIndex() &&
                            oldMethod.getAccessFlags() == newMethod.getAccessFlags() ){
//                        Code oldCode = oldDex.readCode(oldMethod);
//                        Code newCode = oldDex.readCode(newMethod);
                        isSame = true;
                    }
                }
                if (!isSame)
                    return false;
            }
        }

	    return true;
    }
	
	public boolean compare(List<Dex> oldDexList,List<Dex> newDexList) {
        int count = 0;
	    for (Dex newDex : newDexList){
	        for (ClassDef newClassDef : newDex.classDefs()){
                count++;
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
