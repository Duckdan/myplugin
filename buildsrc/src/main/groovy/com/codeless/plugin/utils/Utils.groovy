package com.codeless.plugin.utils

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.MethodInfo
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.MethodVisitor


public class Utils {

    public static void  main(String[] args){
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(Test.class.getName());
        CtMethod[] methods = ctClass.getDeclaredMethods();

        for (CtMethod m : methods) {
            MethodInfo methodInfo = m.getMethodInfo();
            boolean isStatisc =  javassist.Modifier.isStatic(m.getModifiers());
            System.out.println("df:"+isStatisc)

        }

    }

    static MethodParam[] getMethodParams(String methodDesc) {
        ArrayList<MethodParam> rets = new ArrayList<MethodParam>();

        if(methodDesc != null &&  methodDesc.length() > 0) {
            int start = methodDesc.indexOf("(");
            if (start >= 0) {
                start = start + 1;
                int end = methodDesc.indexOf(")");
                methodDesc = methodDesc.substring(start, end);
            }
        }
        if(methodDesc != null &&  methodDesc.length() > 0) {
            char[] chars = methodDesc.toCharArray();
            for (int i = 0; i < chars.length;) {
                char ch = chars[i];
                int oldI = i;
               // Log.logEach("* v                dsfsdf", ch,i)

                if(ch == 'I' ||ch == 'B' ||ch == 'S' ||ch == 'C' ||ch == 'Z' ){
                    //Opcodes.ILOAD  boolean,byte,char ,short,int
                    MethodParam desc = new MethodParam();
                    desc.mOpcode = Opcodes.ILOAD
                    desc.mTypes = "" + ch;
                    rets.add(desc);
                    i++;
                }else if(ch == 'J'){
                    //Opcodes.LLOAD long
                    MethodParam desc = new MethodParam();
                    desc.mOpcode = Opcodes.LLOAD
                    desc.mTypes = "" + ch;
                    rets.add(desc);
                    i++;
                }else if(ch == 'F'){
                    //Opcodes.FLOAD float
                    MethodParam desc = new MethodParam();
                    desc.mOpcode = Opcodes.FLOAD
                    desc.mTypes = "" + ch;
                    rets.add(desc);
                    i++;
                }else if(ch == 'D'){
                    //Opcodes.DLOAD double
                    MethodParam desc = new MethodParam();
                    desc.mOpcode = Opcodes.DLOAD
                    desc.mTypes = "" + ch;
                    rets.add(desc);
                    i++;
                }else if(ch =='[' || ch =='L'){
                    // Opcodes.ALOAD  非内置类型
                    MethodParam desc = new MethodParam();
                    desc.mOpcode = Opcodes.ALOAD
                    desc.mTypes = "" + ch;
                    rets.add(desc);
                    i = nextPosition(chars,i);
                }else {
                    throw  new UnsupportedOperationException("unhow Opcodes !");
                }

                if(oldI >=i){
                    throw new RuntimeException();
                }

            }
        }

        return rets.toArray(new MethodParam[0]);
    }
    static int nextPosition(char[] chars,int start){
        int index = chars.length;
        for(int i= start;i < chars.length;i++){
            if(chars[i] == '['){
                continue;
            }else if(chars[i] =='L'){
                for(i++;i<chars.length;i++){
                    if(chars[i] ==';'){
                        return i+1;
                    }
                }
            }else{
                return i+1;
            }
        }

        return index;
    }

    private static void createByteObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Byte");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ILOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Byte", "<init>", "(B)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createBooleanObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Boolean");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ILOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Boolean", "<init>", "(Z)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createShortObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Short");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ILOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Short", "<init>", "(S)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createCharObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Character");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ILOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Character", "<init>", "(C)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createIntegerObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Integer");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ILOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Integer", "<init>", "(I)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createFloatObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Float");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.FLOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Float", "<init>", "(F)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createDoubleObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Double");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.DLOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Double", "<init>", "(D)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void createLongObj(MethodVisitor mv, int argsPostion) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Long");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.LLOAD, argsPostion);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Long", "<init>", "(J)V");
        mv.visitInsn(Opcodes.AASTORE);
    }

    /**
     * 创建基本类型对应的对象
     *
     * @param mv
     * @param argsPostion
     * @param typeS
     * @return
     */
     static boolean createPrimateTypeObj(MethodVisitor mv, int argsPostion, String typeS) {
         Log.logEach("                                 createPrimateTypeObj", argsPostion,typeS);

         if ("Z".equals(typeS)) {
            createBooleanObj(mv, argsPostion);
            return true;
        }
        if ("B".equals(typeS)) {
            createByteObj(mv, argsPostion);
            return true;
        }
        if ("C".equals(typeS)) {
            createCharObj(mv, argsPostion);
            return true;
        }
        if ("S".equals(typeS)) {
            createShortObj(mv, argsPostion);
            return true;
        }
        if ("I".equals(typeS)) {
            createIntegerObj(mv, argsPostion);
            return true;
        }
        if ("F".equals(typeS)) {
            createFloatObj(mv, argsPostion);
            return true;
        }
        if ("D".equals(typeS)) {
            createDoubleObj(mv, argsPostion);
            return true;
        }
        if ("J".equals(typeS)) {
            createLongObj(mv, argsPostion);
            return true;
        }
        return false;
    }

}


