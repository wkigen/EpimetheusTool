package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import com.github.wkigen.epimetheus.EpimetheusToolLib.utils.Log;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.dexbacked.*;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;


public class DexComparator {

    private final Set<FixClassInfo> changeClassList = new HashSet<>();
    private Set<DexBackedClassDef> oldClassDefList = new HashSet<>();
    private Set<DexBackedClassDef> newClassDefList = new HashSet<>();

	public DexComparator(List<File> oldDexFileList, List<File> newDexFileList) {

        Set<DexBackedDexFile> oldDexList = loadDexFile(oldDexFileList);
        Set<DexBackedDexFile> newDexList = loadDexFile(newDexFileList);

        for (DexBackedDexFile dexBackedDexFile : oldDexList){
            oldClassDefList.addAll(dexBackedDexFile.getClasses());
        }

        for (DexBackedDexFile dexBackedDexFile : newDexList){
            newClassDefList.addAll(dexBackedDexFile.getClasses());
        }
    }

	public Set<FixClassInfo> getChangeClassList(){
	    return changeClassList;
    }

	private Set<DexBackedDexFile> loadDexFile(List<File> dexFileList){
        Set<DexBackedDexFile> dexBackedDexFiles = new HashSet<>();
        try {
            for (File file : dexFileList){
                dexBackedDexFiles.add(DexFileFactory.loadDexFile(file,26));
            }
        }catch (Exception e){

        }
        return dexBackedDexFiles;
    }


    public boolean isSameClassDef(DexBackedClassDef oldClassDef,DexBackedClassDef newClassDef,FixClassInfo fixClassInfo){

        if (!oldClassDef.getType().equals(newClassDef.getType()))
            return false;

        if (!oldClassDef.getSuperclass().equals(newClassDef.getSuperclass()))
            return false;

        if(oldClassDef.getAccessFlags() != newClassDef.getAccessFlags())
            return false;

        if (!isSameInterfaces(oldClassDef.getInterfaces(),newClassDef.getInterfaces()))
            return false;

        if (!isSameFields(oldClassDef.getFields(),newClassDef.getFields(),fixClassInfo))
            return false;

        if (!isSameMethods(oldClassDef.getMethods(),newClassDef.getMethods(),fixClassInfo))
            return false;

        return true;
    }

    public boolean isSameInterfaces(List<String> oldInterface,List<String> newInterface) {
        if(oldInterface.size() != newInterface.size())
            return false;

        for (int i = 0 ;i < oldInterface.size();i++){
            if (!oldInterface.get(i).equals(newInterface.get(i)))
                return false;
        }
	    return true;
    }

    public boolean isSameFields(Iterable<? extends DexBackedField> oldFieldsIter,Iterable<? extends DexBackedField> newFieldsIter,FixClassInfo fixClassInfo) {

        List<DexBackedField> oldFields = new ArrayList<>();
        List<DexBackedField> newFields = new ArrayList<>();

        for (DexBackedField newField : oldFieldsIter){
            oldFields.add(newField);
        }
        for (DexBackedField newField : newFieldsIter){
            newFields.add(newField);
        }

        if (oldFields.size() != newFields.size()) {
            fixClassInfo.canHot = false;
            return false;
        }

        for (int i = 0;i < oldFields.size();i++){
            if (!isSameField(oldFields.get(i),newFields.get(i)))
                return false;
        }

	    return true;
    }

    public boolean isSameField(DexBackedField oldField,DexBackedField newField) {

	    if (!oldField.getType().equals(newField.getType()))
	        return false;

        if (oldField.getAccessFlags() != newField.getAccessFlags())
            return false;

        if (!oldField.getName().equals(newField.getName()))
            return false;

        if (!isSameAnnotations(oldField.getAnnotations(),newField.getAnnotations()))
            return false;

	    return true;
    }


    public boolean isSameAnnotations(Set<? extends Annotation> oldAnnotationSet, Set<? extends Annotation> newAnnotationSet) {

        List<DexBackedAnnotation> oldAnnotations = new ArrayList<>();
        List<DexBackedAnnotation> newAnnotations = new ArrayList<>();

        for (Annotation annotation : oldAnnotationSet){
            oldAnnotations.add((DexBackedAnnotation)annotation);
        }
        for (Annotation annotation : newAnnotationSet){
            newAnnotations.add((DexBackedAnnotation)annotation);
        }

        if (oldAnnotations.size() != newAnnotations.size())
            return false;

        for (int i = 0; i < oldAnnotations.size();i++){
           if (!isSameAnnotation(oldAnnotations.get(i),newAnnotations.get(i)))
               return false;
        }

	    return true;
    }

    public boolean isSameAnnotation(DexBackedAnnotation oldAnnotation, DexBackedAnnotation newAnnotation) {

	    if (!oldAnnotation.getType().equals(newAnnotation.getType()))
	        return false;

        if (oldAnnotation.getVisibility() != newAnnotation.getVisibility())
            return false;

        if (!isSameAnnotationElements(oldAnnotation.getElements(),newAnnotation.getElements()))
            return false;

	    return true;
    }

    public boolean isSameAnnotationElements(Set<? extends DexBackedAnnotationElement> oldAnnotationEmentSet, Set<? extends DexBackedAnnotationElement> newAnnotationEmentSet) {
        List<DexBackedAnnotationElement> oldAnnotationElements = new ArrayList<>();
        List<DexBackedAnnotationElement> newAnnotationElements = new ArrayList<>();

        for (DexBackedAnnotationElement element : oldAnnotationEmentSet){
            oldAnnotationElements.add(element);
        }
        for (DexBackedAnnotationElement element : newAnnotationEmentSet){
            newAnnotationElements.add(element);
        }

        if (oldAnnotationElements.size() != newAnnotationElements.size())
            return false;

        for (int i = 0;i < oldAnnotationElements.size() ;i++){
            if (!isSameAnnotationElement(oldAnnotationElements.get(i),newAnnotationElements.get(i)))
                return false;
        }

	    return true;
    }

    public boolean isSameAnnotationElement(DexBackedAnnotationElement oldAnnotationEment,DexBackedAnnotationElement newAnnotationEment) {

	    if (!oldAnnotationEment.getName().equals(newAnnotationEment.getName()))
	        return false;
        if (oldAnnotationEment.getValue().getValueType() != newAnnotationEment.getValue().getValueType())
            return false;

	    return true;
    }

    public boolean isSameMethods(Iterable<? extends DexBackedMethod> oldMethodSet,Iterable<? extends DexBackedMethod> newMethodSet,FixClassInfo fixClassInfo) {
        List<DexBackedMethod> oldMethods = new ArrayList<>();
        List<DexBackedMethod> newMethods = new ArrayList<>();

        for (DexBackedMethod method : oldMethodSet){
            oldMethods.add(method);
        }

        for (DexBackedMethod method : newMethodSet){
            newMethods.add(method);
        }

        if (oldMethods.size() != newMethods.size()) {
            fixClassInfo.canHot = false;
            return false;
        }

        for (int i = 0;i <oldMethods.size();i++){
            if (!isSameMethod(oldMethods.get(i),newMethods.get(i))) {
                fixClassInfo.fixMethod.add(oldMethods.get(i));
            }
        }

        if (fixClassInfo.fixMethod.size() > 0)
            return false;

	    return true;
    }

    public boolean isSameMethod(DexBackedMethod oldMethod,DexBackedMethod newMethod) {

        if (!oldMethod.getName().equals(newMethod.getName()))
            return false;

        if (oldMethod.getAccessFlags() != newMethod.getAccessFlags())
            return false;

        if (!oldMethod.getReturnType().equals(newMethod.getReturnType()))
            return false;

        if (!isSameMethodParameter(oldMethod.getParameters(),newMethod.getParameters()))
            return false;

        if (!isSameAnnotations(oldMethod.getAnnotations(),newMethod.getAnnotations()))
            return false;

        try{
            InstructionComparator instructionComparator = new InstructionComparator(oldMethod,newMethod);
            if(!instructionComparator.compare())
                return false;
        }catch (Exception e){
            Log.print(e.getMessage());
        }

	    return true;
    }

    public boolean isSameMethodParameter(List<? extends MethodParameter> oldParameterList,List<? extends MethodParameter> newParameterList) {

	    if (oldParameterList.size() != newParameterList.size())
	        return false;

	    for (int i=0;i<oldParameterList.size();i++){

            MethodParameter oldMethodParameter = oldParameterList.get(i);
            MethodParameter newMethodParameter = oldParameterList.get(i);

	        if (!oldMethodParameter.getType().equals(newMethodParameter.getType()))
	            return false;

	        if (!isSameAnnotations(oldMethodParameter.getAnnotations(),newMethodParameter.getAnnotations()))
	            return false;
        }

	    return true;
    }

	public void compare() {
        for (DexBackedClassDef newDexBackedClassDef : newClassDefList){
            boolean isSame = false;
            FixClassInfo fixClassInfo =  new FixClassInfo(newDexBackedClassDef);
            for (DexBackedClassDef oldDexBackedClassDef : oldClassDefList){
                if(newDexBackedClassDef.getType().equals(oldDexBackedClassDef.getType())){
                    if (!isSameClassDef(oldDexBackedClassDef,newDexBackedClassDef,fixClassInfo))
                        isSame = false;
                    else
                        isSame = true;
                    break;
                }
            }
            if (!isSame) {
                Log.print(fixClassInfo);
                changeClassList.add(fixClassInfo);
            }
        }
	}

    public static final class FixClassInfo {
        public DexBackedClassDef fixClassDef;
        public List<DexBackedMethod> fixMethod = new ArrayList<>();
        public boolean canHot = true;

        public FixClassInfo(DexBackedClassDef fixClassDef){
            this.fixClassDef = fixClassDef;
        }
    }

}
