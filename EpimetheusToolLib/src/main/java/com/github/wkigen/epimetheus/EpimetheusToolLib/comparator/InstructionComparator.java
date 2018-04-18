package com.github.wkigen.epimetheus.EpimetheusToolLib.comparator;

import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.dexbacked.DexBackedMethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.*;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.instruction.*;
import org.jf.dexlib2.immutable.reference.*;

import java.util.ArrayList;
import java.util.List;

public class InstructionComparator {

    private DexBackedMethod oldMethod;
    private DexBackedMethod newMethod;

    public InstructionComparator(DexBackedMethod oldMethod,DexBackedMethod newMethod){
        this.oldMethod = oldMethod;
        this.newMethod = newMethod;
    }

    public boolean isSameInstructions(DexBackedMethodImplementation oldMethodImplementation , DexBackedMethodImplementation newMethodImplementation) throws Exception{

        if (oldMethod.getImplementation() == null && newMethod.getImplementation() == null)
            return true;

        if (oldMethod.getImplementation() == null || newMethod.getImplementation() == null)
            return false;

        if (oldMethod.getImplementation() .getRegisterCount() != newMethod.getImplementation().getRegisterCount())
            return false;

        List<Instruction> oldInstructionList = new ArrayList<>();
        List<Instruction> newInstructionList = new ArrayList<>();

        for (Instruction instruction:oldMethodImplementation.getInstructions()){
            oldInstructionList.add(instruction);
        }

        for (Instruction instruction:newMethodImplementation.getInstructions()){
            newInstructionList.add(instruction);
        }

        if (oldInstructionList.size() != newInstructionList.size())
            return false;

        for (int i = 0; i < oldInstructionList.size();i++ ){
            if (!isSameInstruction(oldInstructionList.get(i),newInstructionList.get(i)))
                return false;
        }

        return true;
    }

    public boolean isSameInstruction(Instruction oldInstruction , Instruction newInstruction) throws Exception{

        if (!oldInstruction.getOpcode().name.equals(newInstruction.getOpcode().name))
            return false;

        switch (oldInstruction.getOpcode().format) {
            case Format10t:
                ImmutableInstruction10t old10t = ImmutableInstruction10t.of((Instruction10t)oldInstruction);
                ImmutableInstruction10t new10t = ImmutableInstruction10t.of((Instruction10t)newInstruction);
                if (!compareCode(old10t.getCodeOffset(),new10t.getCodeOffset()))
                    return false;
                break;
            case Format10x:
                break;
            case Format11n:
                ImmutableInstruction11n old11n = ImmutableInstruction11n.of((Instruction11n)oldInstruction);
                ImmutableInstruction11n new11n = ImmutableInstruction11n.of((Instruction11n)newInstruction);
                if (old11n.getRegisterA() != new11n.getRegisterA() || old11n.getNarrowLiteral() != new11n.getNarrowLiteral())
                    return false;
                break;
            case Format11x:
                ImmutableInstruction11x old11x = ImmutableInstruction11x.of((Instruction11x)oldInstruction);
                ImmutableInstruction11x new11x = ImmutableInstruction11x.of((Instruction11x)newInstruction);
                if (old11x.getRegisterA() != new11x.getRegisterA())
                    return false;
                break;
            case Format12x:
                ImmutableInstruction12x old12x = ImmutableInstruction12x.of((Instruction12x)oldInstruction);
                ImmutableInstruction12x new12x = ImmutableInstruction12x.of((Instruction12x)newInstruction);
                if (old12x.getRegisterA() != new12x.getRegisterA() || old12x.getRegisterB() != new12x.getRegisterB())
                    return false;
                break;
            case Format20bc:
                ImmutableInstruction20bc old20bc = ImmutableInstruction20bc.of((Instruction20bc)oldInstruction);
                ImmutableInstruction20bc new20bc = ImmutableInstruction20bc.of((Instruction20bc)newInstruction);
                if (!compareIndex(old20bc.getReferenceType(),old20bc.getReference(),new20bc.getReference()))
                    return false;
                break;
            case Format20t:
                ImmutableInstruction20t old20t = ImmutableInstruction20t.of((Instruction20t)oldInstruction);
                ImmutableInstruction20t new20t = ImmutableInstruction20t.of((Instruction20t)newInstruction);
                if (!compareCode(old20t.getCodeOffset(),new20t.getCodeOffset()))
                    return false;
                break;
            case Format21c:
                ImmutableInstruction21c old21c = ImmutableInstruction21c.of((Instruction21c)oldInstruction);
                ImmutableInstruction21c new21c = ImmutableInstruction21c.of((Instruction21c)newInstruction);
                if (!compareIndex(old21c.getReferenceType(),old21c.getReference(),new21c.getReference()))
                    return false;
                break;
            case Format21ih:
                ImmutableInstruction21ih old21ih = ImmutableInstruction21ih.of((Instruction21ih)oldInstruction);
                ImmutableInstruction21ih new21ih = ImmutableInstruction21ih.of((Instruction21ih)newInstruction);
                if (old21ih.getRegisterA() != new21ih.getRegisterA())
                    return false;
                if (old21ih.getNarrowLiteral() != new21ih.getNarrowLiteral())
                    return false;
                break;
            case Format21lh:
                ImmutableInstruction21lh old21lh = ImmutableInstruction21lh.of((Instruction21lh)oldInstruction);
                ImmutableInstruction21lh new21lh = ImmutableInstruction21lh.of((Instruction21lh)newInstruction);
                if (old21lh.getRegisterA() != new21lh.getRegisterA())
                    return false;
                if (old21lh.getHatLiteral() != new21lh.getHatLiteral())
                    return false;
                break;
            case Format21s:
                ImmutableInstruction21s old21s = ImmutableInstruction21s.of((Instruction21s)oldInstruction);
                ImmutableInstruction21s new21s = ImmutableInstruction21s.of((Instruction21s)newInstruction);
                if (old21s.getRegisterA() != new21s.getRegisterA())
                    return false;
                if (old21s.getNarrowLiteral() != new21s.getNarrowLiteral())
                    return false;
                break;
            case Format21t:
                ImmutableInstruction21t old21t = ImmutableInstruction21t.of((Instruction21t)oldInstruction);
                ImmutableInstruction21t new21t = ImmutableInstruction21t.of((Instruction21t)newInstruction);
                if (old21t.getRegisterA() != new21t.getRegisterA())
                    return false;
                if (!compareCode(old21t.getCodeOffset(),new21t.getCodeOffset()))
                    return false;
                break;
            case Format22b:
                ImmutableInstruction22b old22b = ImmutableInstruction22b.of((Instruction22b)oldInstruction);
                ImmutableInstruction22b new22b = ImmutableInstruction22b.of((Instruction22b)newInstruction);
                if (old22b.getRegisterA() != new22b.getRegisterA())
                    return false;
                if (old22b.getRegisterB() != new22b.getRegisterB())
                    return false;
                if (old22b.getNarrowLiteral() != new22b.getNarrowLiteral())
                    return false;
                break;
            case Format22c:
                ImmutableInstruction22c old22c = ImmutableInstruction22c.of((Instruction22c)oldInstruction);
                ImmutableInstruction22c new22c = ImmutableInstruction22c.of((Instruction22c)newInstruction);
                if (!compareIndex(old22c.getReferenceType(),old22c.getReference(),new22c.getReference()))
                    return false;
                break;
            case Format22cs:
                ImmutableInstruction22cs old22cs = ImmutableInstruction22cs.of((Instruction22cs)oldInstruction);
                ImmutableInstruction22cs new22cs = ImmutableInstruction22cs.of((Instruction22cs)newInstruction);
                if (old22cs.getRegisterA() != new22cs.getRegisterA())
                    return false;
                if (old22cs.getRegisterB() != new22cs.getRegisterB())
                    return false;
                if (old22cs.getFieldOffset() != new22cs.getFieldOffset())
                    return false;
                break;
            case Format22s:
                ImmutableInstruction22s old22s = ImmutableInstruction22s.of((Instruction22s)oldInstruction);
                ImmutableInstruction22s new22s = ImmutableInstruction22s.of((Instruction22s)newInstruction);
                if (old22s.getRegisterA() != new22s.getRegisterA())
                    return false;
                if (old22s.getRegisterB() != new22s.getRegisterB())
                    return false;
                if (old22s.getNarrowLiteral() != new22s.getNarrowLiteral())
                    return false;
                break;
            case Format22t:
                ImmutableInstruction22t old22t = ImmutableInstruction22t.of((Instruction22t)oldInstruction);
                ImmutableInstruction22t new22t = ImmutableInstruction22t.of((Instruction22t)newInstruction);
                if (old22t.getRegisterA() != new22t.getRegisterA())
                    return false;
                if (old22t.getRegisterB() != new22t.getRegisterB())
                    return false;
                if (!compareCode(old22t.getCodeOffset(),new22t.getCodeOffset()))
                    return false;
                break;
            case Format22x:
                ImmutableInstruction22x old22x = ImmutableInstruction22x.of((Instruction22x)oldInstruction);
                ImmutableInstruction22x new22x = ImmutableInstruction22x.of((Instruction22x)newInstruction);
                if (old22x.getRegisterA() != new22x.getRegisterA())
                    return false;
                if (old22x.getRegisterB() != new22x.getRegisterB())
                    return false;
                break;
            case Format23x:
                ImmutableInstruction23x old23x = ImmutableInstruction23x.of((Instruction23x)oldInstruction);
                ImmutableInstruction23x new23x = ImmutableInstruction23x.of((Instruction23x)newInstruction);
                if (old23x.getRegisterA() != new23x.getRegisterA())
                    return false;
                if (old23x.getRegisterB() != new23x.getRegisterB())
                    return false;
                if (old23x.getRegisterB() != new23x.getRegisterB())
                    return false;
                break;
            case Format25x:
                ImmutableInstruction25x old25x = ImmutableInstruction25x.of((Instruction25x)oldInstruction);
                ImmutableInstruction25x new25x = ImmutableInstruction25x.of((Instruction25x)newInstruction);
                if (old25x.getRegisterParameterD() != new25x.getRegisterParameterD())
                    return false;
                if (old25x.getRegisterParameterE() != new25x.getRegisterParameterE())
                    return false;
                if (old25x.getRegisterParameterF() != new25x.getRegisterParameterF())
                    return false;
                if (old25x.getRegisterParameterG() != new25x.getRegisterParameterG())
                    return false;
                if (old25x.getParameterRegisterCount() != new25x.getParameterRegisterCount())
                    return false;
                if (old25x.getRegisterFixedC() != new25x.getRegisterFixedC())
                    return false;
                break;
            case Format30t:
                ImmutableInstruction30t old30t = ImmutableInstruction30t.of((Instruction30t)oldInstruction);
                ImmutableInstruction30t new30t = ImmutableInstruction30t.of((Instruction30t)newInstruction);
                if (!compareCode(old30t.getCodeOffset(),new30t.getCodeOffset()))
                    return false;
                break;
            case Format31c:
                ImmutableInstruction31c  old31c = ImmutableInstruction31c.of((Instruction31c)oldInstruction);
                ImmutableInstruction31c  new31c  = ImmutableInstruction31c.of((Instruction31c)newInstruction);
                if (old31c.getRegisterA() != new31c.getRegisterA())
                    return false;
                if (!compareIndex(old31c.getReferenceType(),old31c.getReference(),new31c.getReference()))
                    return false;
                break;
            case Format31i:
                ImmutableInstruction31i  old31i = ImmutableInstruction31i.of((Instruction31i)oldInstruction);
                ImmutableInstruction31i  new31i = ImmutableInstruction31i.of((Instruction31i)newInstruction);
                if (old31i.getRegisterA() != new31i.getRegisterA())
                    return false;
                if (old31i.getNarrowLiteral() != new31i.getNarrowLiteral())
                    return false;
                break;
            case Format31t:
                ImmutableInstruction31t  old31t = ImmutableInstruction31t.of((Instruction31t)oldInstruction);
                ImmutableInstruction31t  new31t = ImmutableInstruction31t.of((Instruction31t)newInstruction);
                if (old31t.getRegisterA() != new31t.getRegisterA())
                    return false;
                if (!compareCode(old31t.getCodeOffset(),new31t.getCodeOffset()))
                    return false;
                break;
            case Format32x:
                ImmutableInstruction32x  old32x = ImmutableInstruction32x.of((Instruction32x)oldInstruction);
                ImmutableInstruction32x  new32x = ImmutableInstruction32x.of((Instruction32x)newInstruction);
                if (old32x.getRegisterA() != new32x.getRegisterA())
                    return false;
                if (old32x.getRegisterB() != new32x.getRegisterB())
                    return false;
                break;
            case Format35c:
                ImmutableInstruction35c  old35c = ImmutableInstruction35c.of((Instruction35c)oldInstruction);
                ImmutableInstruction35c  new35c = ImmutableInstruction35c.of((Instruction35c)newInstruction);
                if (old35c.getRegisterC() != new35c.getRegisterC())
                    return false;
                if (old35c.getRegisterD() != new35c.getRegisterD())
                    return false;
                if (old35c.getRegisterE() != new35c.getRegisterE())
                    return false;
                if (old35c.getRegisterF() != new35c.getRegisterF())
                    return false;
                if (old35c.getRegisterG() != new35c.getRegisterG())
                    return false;
                if (old35c.getRegisterCount() != new35c.getRegisterCount())
                    return false;
                if (!compareIndex(old35c.getReferenceType(),old35c.getReference(),new35c.getReference()))
                    return false;
                break;
            case Format35mi:
                ImmutableInstruction35mi  old35mi = ImmutableInstruction35mi.of((Instruction35mi)oldInstruction);
                ImmutableInstruction35mi  new35mi = ImmutableInstruction35mi.of((Instruction35mi)newInstruction);
                if (old35mi.getRegisterC() != new35mi.getRegisterC())
                    return false;
                if (old35mi.getRegisterD() != new35mi.getRegisterD())
                    return false;
                if (old35mi.getRegisterE() != new35mi.getRegisterE())
                    return false;
                if (old35mi.getRegisterF() != new35mi.getRegisterF())
                    return false;
                if (old35mi.getRegisterG() != new35mi.getRegisterG())
                    return false;
                if (old35mi.getRegisterCount() != new35mi.getRegisterCount())
                    return false;
                if (old35mi.getInlineIndex() != new35mi.getInlineIndex())
                    return false;
                break;
            case Format35ms:
                ImmutableInstruction35ms  old35ms = ImmutableInstruction35ms.of((Instruction35ms)oldInstruction);
                ImmutableInstruction35ms  new35ms = ImmutableInstruction35ms.of((Instruction35ms)newInstruction);
                if (old35ms.getRegisterC() != new35ms.getRegisterC())
                    return false;
                if (old35ms.getRegisterD() != new35ms.getRegisterD())
                    return false;
                if (old35ms.getRegisterE() != new35ms.getRegisterE())
                    return false;
                if (old35ms.getRegisterF() != new35ms.getRegisterF())
                    return false;
                if (old35ms.getRegisterG() != new35ms.getRegisterG())
                    return false;
                if (old35ms.getRegisterCount() != new35ms.getRegisterCount())
                    return false;
                if (old35ms.getVtableIndex() != new35ms.getVtableIndex())
                    return false;
                break;
            case Format3rc:
                ImmutableInstruction3rc  old3rc = ImmutableInstruction3rc.of((Instruction3rc)oldInstruction);
                ImmutableInstruction3rc  new3rc = ImmutableInstruction3rc.of((Instruction3rc)newInstruction);
                if (old3rc.getStartRegister() != new3rc.getStartRegister())
                    return false;
                if (old3rc.getRegisterCount() != new3rc.getRegisterCount())
                    return false;
                if (!compareIndex(old3rc.getReferenceType(),old3rc.getReference(),new3rc.getReference()))
                    return false;
                break;
            case Format3rmi:
                ImmutableInstruction3rmi  old3rmi = ImmutableInstruction3rmi.of((Instruction3rmi)oldInstruction);
                ImmutableInstruction3rmi  new3rmi = ImmutableInstruction3rmi.of((Instruction3rmi)newInstruction);
                if (old3rmi.getStartRegister() != new3rmi.getStartRegister())
                    return false;
                if (old3rmi.getRegisterCount() != new3rmi.getRegisterCount())
                    return false;
                if (old3rmi.getInlineIndex() != new3rmi.getInlineIndex())
                    return false;
                break;
            case Format3rms:
                ImmutableInstruction3rms  old3rms = ImmutableInstruction3rms.of((Instruction3rms)oldInstruction);
                ImmutableInstruction3rms  new3rms = ImmutableInstruction3rms.of((Instruction3rms)newInstruction);
                if (old3rms.getStartRegister() != new3rms.getStartRegister())
                    return false;
                if (old3rms.getRegisterCount() != new3rms.getRegisterCount())
                    return false;
                if (old3rms.getVtableIndex() != new3rms.getVtableIndex())
                    return false;
                break;
            case Format51l:
                ImmutableInstruction51l  old51l = ImmutableInstruction51l.of((Instruction51l)oldInstruction);
                ImmutableInstruction51l  new51l = ImmutableInstruction51l.of((Instruction51l)newInstruction);
                if (old51l.getRegisterA() != new51l.getRegisterA())
                    return false;
                if (old51l.getWideLiteral() != new51l.getWideLiteral())
                    return false;
                break;
            case PackedSwitchPayload:
                ImmutablePackedSwitchPayload  oldPackedSwitchPayload = ImmutablePackedSwitchPayload.of((PackedSwitchPayload)oldInstruction);
                ImmutablePackedSwitchPayload  newPackedSwitchPayload = ImmutablePackedSwitchPayload.of((PackedSwitchPayload)newInstruction);
                if(oldPackedSwitchPayload.getSwitchElements().size() != newPackedSwitchPayload.getSwitchElements().size())
                    return false;
                int packedSwitchPayloadCount = oldPackedSwitchPayload.getSwitchElements().size();
                for (int i = 0;i < packedSwitchPayloadCount;i++){
                    SwitchElement oldSwitchElement = oldPackedSwitchPayload.getSwitchElements().get(i);
                    SwitchElement newSwitchElement = newPackedSwitchPayload.getSwitchElements().get(i);
                    if (oldSwitchElement.getKey() != newSwitchElement.getKey())
                        return false;
                    if (oldSwitchElement.getOffset() != newSwitchElement.getOffset())
                        return false;
                }
                break;
            case SparseSwitchPayload:
                ImmutableSparseSwitchPayload  oldSparseSwitchPayload  = ImmutableSparseSwitchPayload.of((SparseSwitchPayload)oldInstruction);
                ImmutableSparseSwitchPayload  newSparseSwitchPayload = ImmutableSparseSwitchPayload.of((SparseSwitchPayload)newInstruction);
                if(oldSparseSwitchPayload.getSwitchElements().size() != newSparseSwitchPayload.getSwitchElements().size())
                    return false;
                int sparseSwitchPayloadCount = oldSparseSwitchPayload.getSwitchElements().size();
                for (int i = 0;i < sparseSwitchPayloadCount;i++){
                    SwitchElement oldSwitchElement = oldSparseSwitchPayload.getSwitchElements().get(i);
                    SwitchElement newSwitchElement = newSparseSwitchPayload.getSwitchElements().get(i);
                    if (oldSwitchElement.getKey() != newSwitchElement.getKey())
                        return false;
                    if (oldSwitchElement.getOffset() != newSwitchElement.getOffset())
                        return false;
                }
                break;
            case ArrayPayload:
                ImmutableArrayPayload  oldArrayPayload  = ImmutableArrayPayload.of((ArrayPayload)oldInstruction);
                ImmutableArrayPayload  newArrayPayload = ImmutableArrayPayload.of((ArrayPayload)newInstruction);
                if (oldArrayPayload.getElementWidth() != newArrayPayload.getElementWidth())
                    return false;

                int elementWidth = oldArrayPayload.getElementWidth();
                int arrayPayloadCount = oldArrayPayload.getArrayElements().size();
                for (int i =0;i < arrayPayloadCount;i++){
                    switch (elementWidth) {
                        case 1:
                            byte oldByte = (byte)oldArrayPayload.getArrayElements().get(i);
                            byte newByte = (byte)newArrayPayload.getArrayElements().get(i);
                            if (oldByte != newByte)
                                return false;
                            break;
                        case 2:
                            short oldShort = (short)oldArrayPayload.getArrayElements().get(i);
                            short newShort = (short)newArrayPayload.getArrayElements().get(i);
                            if(oldShort != newShort)
                                return false;
                            break;
                        case 4:
                            int oldInt = (int)oldArrayPayload.getArrayElements().get(i);
                            int newInt = (int)newArrayPayload.getArrayElements().get(i);
                            if(oldInt != newInt)
                                return false;
                            break;
                        case 8:
                            long oldLong = (long)oldArrayPayload.getArrayElements().get(i);
                            long newLong = (long)newArrayPayload.getArrayElements().get(i);
                            if(oldLong != newLong)
                                return false;
                            break;
                         default:
                             throw new Exception("unkone element_width: " + elementWidth);
                    }
                }
                break;
            default:
                throw new Exception("unkone getOpcode().format: " + oldInstruction.getOpcode().format);
        }
        return true;
    }

    private boolean compareCode(int oldCodeOffset,int newCodeOffset) throws Exception {
//        if (oldCodeOffset > 0 && newCodeOffset > 0)
//            return isSameInstructions(new DexBackedMethodImplementation(oldMethod.dexFile, oldMethod, oldCodeOffset),new DexBackedMethodImplementation(newMethod.dexFile, newMethod, newCodeOffset));
        return oldCodeOffset == newCodeOffset;
    }

    private boolean compareIndex(int opcode, ImmutableReference oldReference, ImmutableReference newReference) throws Exception{
        switch (opcode){
            case ReferenceType.STRING:
                ImmutableStringReference oldStrReference = ImmutableStringReference.of((StringReference)oldReference);
                ImmutableStringReference newStrReference = ImmutableStringReference.of((StringReference)newReference);
                if (!oldStrReference.getString().equals(newStrReference.getString()))
                    return false;
                break;
            case ReferenceType.TYPE:
                ImmutableTypeReference oldTypeReference = ImmutableTypeReference.of((TypeReference)oldReference);
                ImmutableTypeReference newTypeReference = ImmutableTypeReference.of((TypeReference)newReference);
                if (!oldTypeReference.getType().equals(newTypeReference.getType()))
                    return false;
                break;
            case ReferenceType.FIELD:
                ImmutableFieldReference oldFieldReference = ImmutableFieldReference.of((FieldReference)oldReference);
                ImmutableFieldReference newFieldReference = ImmutableFieldReference.of((FieldReference)newReference);
                if (!oldFieldReference.getType().equals(newFieldReference.getType()))
                    return false;
                if (!oldFieldReference.getDefiningClass().equals(newFieldReference.getDefiningClass()))
                    return false;
                break;
            case ReferenceType.METHOD:
                ImmutableMethodReference oldMethodReference = ImmutableMethodReference.of((MethodReference)oldReference);
                ImmutableMethodReference newMethodReference = ImmutableMethodReference.of((MethodReference)newReference);
                if (!oldMethodReference.getReturnType().equals(newMethodReference.getReturnType()))
                    return false;
                if (!oldMethodReference.getDefiningClass().equals(newMethodReference.getDefiningClass()))
                    return false;
                if (oldMethodReference.getParameterTypes().size() != newMethodReference.getParameterTypes().size())
                    return false;
                for (int i = 0; i< oldMethodReference.getParameterTypes().size();i++){
                    if (!oldMethodReference.getParameterTypes().get(i).equals(newMethodReference.getParameterTypes().get(i)))
                        return false;
                }
                break;
            default:
                throw new Exception("unkone ReferenceType : " + opcode);
        }
        return true;
    }

    public boolean compare() throws Exception{
        if (!isSameInstructions(oldMethod.getImplementation() ,newMethod.getImplementation()))
            return false;

        return true;
    }

}
