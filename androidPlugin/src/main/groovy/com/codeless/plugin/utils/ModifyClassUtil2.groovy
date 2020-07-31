package com.codeless.plugin.utils

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.MethodInfo
import javassist.bytecode.annotation.Annotation

/**

 * 符号	含义
 * $0, $1, $2, ...	this and 方法的参数
 * $args	方法参数数组.它的类型为 Object[]
 * $$	所有实参。例如, m($$) 等价于 m($1,$2,...)
 * $cflow(...)	cflow 变量
 * $r	返回结果的类型，用于强制类型转换
 * $w	包装器类型，用于强制类型转换
 * $_	返回值
 * $sig	类型为 java.lang.Class 的参数类型数组
 * $type	一个 java.lang.Class 对象，表示返回值类型
 * $class	一个 java.lang.Class 对象，表示当前正在修改的类
 *
 * 作者：二胡
 * 链接：https://www.jianshu.com/p/b9b3ff0e1bf8
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class ModifyClassUtil2 {

    public static final String ANNONATION_NAME = "com.buried.point.BuriedPoint";

    public static ClassPool gClassPool;

    public
    static byte[] modifyClasses(String className, byte[] srcByteCode) {
        modifyClasses(gClassPool, className, srcByteCode);
    }
    /***
     * 修改代码，分析BuriedPoint的信息，并注入代码。
     * @param pool
     * @param className
     * @param srcByteCode
     * @return
     */
    public
    static byte[] modifyClasses(ClassPool pool, String className, byte[] srcByteCode) {
        byte[] classBytesCode = null;
        try {
            Log.info("====start modifying ${className}====");
            CtClass ctClass = pool.getCtClass(className)
            classBytesCode = modifyClass(pool, ctClass, srcByteCode);
            //Log.info("====revisit modified ${className}====");
            //onlyVisitClassMethod(classBytesCode);
            return classBytesCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classBytesCode == null) {
            classBytesCode = srcByteCode;
        }
        return classBytesCode;
    }

    /**
     * 插入埋点处理事件的代码
     * @param pool
     * @param ctClass
     * @param srcClass
     * @return
     * @throws IOException
     */
    private
    static byte[] modifyClass(ClassPool pool, CtClass ctClass, byte[] srcClass) throws IOException {
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }
        CtMethod[] ctMethods = ctClass.getDeclaredMethods()

        boolean hasModified = false;
        if (ctMethods != null) {
            for (CtMethod method : ctMethods) {
                Annotation[] annotations = findVAnnotations(method);

                method.getMethodInfo().getAccessFlags()
                for (Annotation anno : annotations) {

                    String moduleId = anno.getMemberValue("moduleId");
                    String eventId = anno.getMemberValue("eventId");
                    String source = anno.getMemberValue("source");
                    boolean isStatisc = javassist.Modifier.isStatic(method.getModifiers());
                    //开始注入代码
                    StringBuilder code = new StringBuilder()
                    //注意:javassist的编译器不支持泛型
                    if (isStatisc) {
                        code.append('{com.buried.point.PluginAgent.aop('
                                + moduleId + ','
                                + eventId + ','
                                + source + ','
                                + 'null,'
                                + '$args);}')
                    } else {
                        code.append('{com.buried.point.PluginAgent.aop('
                                + moduleId + ','
                                + eventId + ','
                                + source + ','
                                + '$0,'
                                + '$args);}')
                    }
                    Log.info("==== BuriedPoint aop inject ====");
                    Log.info("              method : " + method.getMethodInfo().getName() + ",class:" + ctClass.getName());
                    Log.info("              code   : " + code.toString());
                    method.insertBefore(code.toString())
                    hasModified = true;

                }
            }
        }
        if (hasModified) {
            ctClass.detach()
        }
        return ctClass.toBytecode();
    }


    public static Annotation[] findVAnnotations(CtMethod method) {
        MethodInfo methodInfo = method.getMethodInfo();

        AnnotationsAttribute attribute = (AnnotationsAttribute) methodInfo
                .getAttribute(AnnotationsAttribute.visibleTag);
        ArrayList<Annotation> list = new ArrayList<>();
        if (attribute != null) {
            Annotation[] anns2 = attribute.getAnnotations();
            int length = anns2 != null ? anns2.length : 0;
            for (int i = 0; i < length; i++) {
                Annotation annotation = anns2[i];
                String typeName = annotation.getTypeName();

                Log.info("      find  annotation: " + typeName);
                if (ANNONATION_NAME.equals(typeName)) {
                    list.add(annotation)
                }
            }
        }
        return list.toArray(new Annotation[0]);
    }


}