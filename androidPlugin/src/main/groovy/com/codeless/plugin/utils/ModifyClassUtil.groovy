package com.codeless.plugin.utils

import com.codeless.plugin.MethodCell
import com.codeless.plugin.ReWriterConfig
import org.objectweb.asm.*

/**
 * Created by bryansharp(bsp0911932@163.com) on 2016/5/10.
 * Modified by nailperry on 2017/3/2.
 *
 */
public class ModifyClassUtil {

    public
    static byte[] modifyClasses(String className, byte[] srcByteCode) {
        byte[] classBytesCode = srcByteCode;
        //不修改PluginAgent
        if (className.startsWith(ReWriterConfig.sAgentClassName)) {
            return srcByteCode;
        }
        try {
            Log.info("====start modifying ${className}====" + ReWriterConfig.sAgentClassName);

            //注解
            classBytesCode = ModifyClassUtil2.modifyClasses(className, classBytesCode)

            //自动采集功能。
            classBytesCode = modifyClass(classBytesCode);

            Log.info("====finish modifying ${className}====");
            return classBytesCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classBytesCode == null) {
            classBytesCode = srcByteCode;
        }
        return classBytesCode;
    }


    private
    static byte[] modifyClass(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor methodFilterCV = new MethodFilterClassVisitor(classWriter);
        ClassReader cr = new ClassReader(srcClass);
        cr.accept(methodFilterCV, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    private
    static void onlyVisitClassMethod(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        MethodFilterClassVisitor methodFilterCV = new MethodFilterClassVisitor(classWriter);
        methodFilterCV.onlyVisit = true;
        ClassReader cr = new ClassReader(srcClass);
        cr.accept(methodFilterCV, ClassReader.EXPAND_FRAMES);
    }

    private static boolean instanceOfFragment(String superName) {
        return superName.equals('android/app/Fragment') || superName.equals('android/support/v4/app/Fragment')
    }

    /**
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
     *            INVOKEINTERFACE.
     * @param owner
     *            the internal name of the method's owner class (see
     * {@link org.objectweb.asm.Type#getInternalName() getInternalName}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link org.objectweb.asm.Type Type}).
     * @param start 方法参数起始索引（ 0：this，1+：普通参数 ）
     *
     * @param count 方法参数个数
     *
     * @param paramOpcodes 参数类型对应的ASM指令
     *
     */
    private
    static void visitMethodWithLoadedParams(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes) {
        for (int i = start; i < start + count; i++) {
            methodVisitor.visitVarInsn(paramOpcodes[i - start], i);
        }
        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false);

        Log.logEach("==== BuriedPoint aop inject system view ====", methodName, methodDesc)

    }

    private
    static void visitMethodWithLoadedParams2(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, boolean isStatic, String originDesc) {
        Log.logEach("* visitMethodWithLoadedParams2", originDesc)
        MethodParam[] methodList = Utils.getMethodParams(originDesc)
        Log.logEach("                                 methodList.length", methodList.length);


        //第一个变量
        if (!isStatic) {
            methodVisitor.visitVarInsn(jdk.internal.org.objectweb.asm.Opcodes.ALOAD, 0);
        } else {
            methodVisitor.visitInsn(Opcodes.ACONST_NULL)
        }

        //组建第二个变量

        //传递数组。参考：https://www.iteye.com/blog/alvinqq-940965
        //参考：InstantRunForansForm: 525行
        //TODO Object[] 内置类型转为为Object 参考：https://www.programcreek.com/java-api-examples/?code=Meituan-Dianping%2FRobust%2FRobust-master%2Fgradle-plugin%2Fsrc%2Fmain%2Fgroovy%2Frobust%2Fgradle%2Fplugin%2Fasm%2FRobustAsmUtils.java#  createPrimateTypeObj()

        if (methodList.length >= 6) {
            methodVisitor.visitIntInsn(Opcodes.BIPUSH, methodList.length);
        } else {
            methodVisitor.visitInsn(Opcodes.ICONST_0 + methodList.length);
        }
        methodVisitor.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        int bigPrimaiteTypeCount = 0;
        for (int index = 0; index < methodList.length; index++) {
            methodVisitor.visitInsn(Opcodes.DUP);
            if (index <= 5) {
                methodVisitor.visitInsn(Opcodes.ICONST_0 + index);
            } else {
                methodVisitor.visitIntInsn(Opcodes.BIPUSH, index);
            }


            int argIndex = isStatic ? index : index + 1;
            argIndex = argIndex + bigPrimaiteTypeCount;

            if (!Utils.createPrimateTypeObj(methodVisitor, argIndex, methodList[index].mTypes)) {
                Log.logEach("                                 createPrimateTypeObj fasle");
                methodVisitor.visitVarInsn(methodList[index].mOpcode, argIndex);
                methodVisitor.visitInsn(Opcodes.AASTORE);
            }

            //这里又要做特殊处理，在实践过程中发现个问题：public void xxx(long a, boolean b, double c,int d)
            //当一个参数的前面一个参数是long或者是double类型的话，后面参数在使用LOAD指令，加载数据索引值要+1
            //个人猜想是和long，double是8个字节的问题有关系。这里做了处理
            //比如这里的参数：[a=LLOAD 1] [b=ILOAD 3] [c=DLOAD 4] [d=ILOAD 6];
            //这里需要判断当前参数的前面一个参数的类型是什么
            if ("J".equals(methodList[index].mTypes) || "D".equals(methodList[index].mTypes)) {
                //如果前面一个参数是long，double类型，load指令索引就要增加1
                bigPrimaiteTypeCount++;
            }
        }


        println "        visitMethodInsn";

        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false);
    }

    static class MethodFilterClassVisitor extends ClassVisitor {
        public boolean onlyVisit = false;
        public HashSet<String> visitedFragMethods = new HashSet<>()// 无需判空
        private String superName
        private String[] interfaces
        private ClassVisitor classVisitor

        public MethodFilterClassVisitor(
                final ClassVisitor cv) {
            super(Opcodes.ASM5, cv);
            this.classVisitor = cv
        }

        @Override
        void visitEnd() {
            Log.logEach('* visitEnd *');

            if (instanceOfFragment(superName)) {
                MethodVisitor mv;
                // 添加剩下的方法，确保super.onHiddenChanged(hidden);等先被调用
                Iterator<Map.Entry<String, MethodCell>> iterator = ReWriterConfig.sFragmentMethods.entrySet().iterator()
                while (iterator.hasNext()) {
                    Map.Entry<String, MethodCell> entry = iterator.next()
                    String key = entry.getKey()
                    MethodCell methodCell = entry.getValue()

                    if (visitedFragMethods.contains(key))
                        continue
                    mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, methodCell.name, methodCell.desc, null, null);
                    mv.visitCode();
                    // call super
                    visitMethodWithLoadedParams(mv, Opcodes.INVOKESPECIAL, superName, methodCell.name, methodCell.desc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                    // call injected method
                    visitMethodWithLoadedParams(mv, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassNamePath, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(methodCell.paramsCount, methodCell.paramsCount);
                    mv.visitEnd();
                }
            }
            super.visitEnd()
        }

        @Override
        void visitAttribute(Attribute attribute) {
            Log.logEach('* visitAttribute *', attribute, attribute.type, attribute.metaClass, attribute.metaPropertyValues, attribute.properties);
            super.visitAttribute(attribute)
        }

        @Override
        AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            Log.logEach('* visitAnnotation *', desc, visible);
            return super.visitAnnotation(desc, visible);
        }

        @Override
        void visitInnerClass(String name, String outerName,
                             String innerName, int access) {
            Log.logEach('* visitInnerClass *', name, outerName, innerName, Log.accCode2String(access));
            super.visitInnerClass(name, outerName, innerName, access)
        }

        @Override
        void visitOuterClass(String owner, String name, String desc) {
            Log.logEach('* visitOuterClass *', owner, name, desc);
            super.visitOuterClass(owner, name, desc)
        }

        @Override
        void visitSource(String source, String debug) {
            Log.logEach('* visitSource *', source, debug);
            super.visitSource(source, debug)
        }

        @Override
        FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            Log.logEach('* visitField *', Log.accCode2String(access), name, desc, signature, value);
            return super.visitField(access, name, desc, signature, value)
        }

        @Override
        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            Log.logEach('* visit *', Log.accCode2String(access), name, signature, superName, interfaces);
            this.superName = superName
            this.interfaces = interfaces
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {
            MethodVisitor myMv = null;
            if (!onlyVisit) {
                Log.logEach("* visitMethod *", Log.accCode2String(access), name, desc, signature, exceptions);
            }
            if (interfaces != null && interfaces.length > 0) {
                MethodCell methodCell = ReWriterConfig.sInterfaceMethods.get(name + desc)
                /*if (methodCell != null) {
                    Log.logEach("* visitMethod *", name + desc, methodCell, methodCell.parent, interfaces);
                }*/

                if (methodCell != null && interfaces.contains(methodCell.parent)) {
                    if (onlyVisit) {
                        myMv = new MethodLogVisitor(cv.visitMethod(access, name, desc, signature, exceptions));
                    } else {
                        Log.logEach("   * will modify ", name + desc);

                        try {
                            MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
                            myMv = new MethodLogVisitor(methodVisitor) {
                                @Override
                                void visitCode() {
                                    super.visitCode();
                                    visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassNamePath, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            myMv = null
                        }
                    }
                }
            }

            if (instanceOfFragment(superName)) {
                MethodCell methodCell = ReWriterConfig.sFragmentMethods.get(name + desc)
                if (methodCell != null) {
                    // 记录该方法已存在
                    visitedFragMethods.add(name + desc)
                    if (onlyVisit) {
                        myMv = new MethodLogVisitor(cv.visitMethod(access, name, desc, signature, exceptions));
                    } else {
                        try {
                            MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
                            myMv = new MethodLogVisitor(methodVisitor) {

                                @Override
                                void visitInsn(int opcode) {

                                    // 确保super.onHiddenChanged(hidden);等先被调用
                                    if (opcode == Opcodes.RETURN) { //在返回之前安插代码
                                        visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassNamePath, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                                    }
                                    super.visitInsn(opcode);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            myMv = null
                        }
                    }
                }
            }
            if (myMv != null) {
                if (onlyVisit) {
                    Log.logEach("* revisitMethod *", Log.accCode2String(access), name, desc, signature);
                }
                return myMv;
            } else {
                return cv.visitMethod(access, name, desc, signature, exceptions);
            }
        }
    }
}