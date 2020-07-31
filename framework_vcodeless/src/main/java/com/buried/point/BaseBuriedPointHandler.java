package com.buried.point;

import java.util.HashMap;

/**
 * 自定义事件的处理类，结合@BuriedPoint注解的埋点
 *
 * @Author : raodongming
 * @Date : 16:51  2020/3/2
 * @Email : 11101037@bbktel.com
 * @title :
 * @Company : www.vivo.com
 */
public abstract class BaseBuriedPointHandler {


    /**
     * 埋点参数其它的方法。
     * 返回是否已经处理。
     *
     * @param moduleId,eventId,source @BuriedPoint注解的参数值
     * @param args                    返回调用改方法的参数列表
     * @return 返回true的时候代表处理埋点数据，false表示不处理埋点数据逻辑
     * @parma instance 返回该类调用方法的实例对象，如果是静态方法，返回null
     */
    protected boolean handleBuriedPoint(String moduleId, String eventId, String source, Object instance, Object[] args) {
        HashMap<String, Object> dynamicParams = null;
        //BuriedPoint注解用在非静态方法时
        if (instance != null) {
            DynamicBuriedPoint annotation = instance.getClass().getAnnotation(DynamicBuriedPoint.class);
            if (annotation != null && annotation.value()) {
                IDynamicBuriedPointHandler dynamicBuriedPointHandler = (IDynamicBuriedPointHandler) instance;
                dynamicParams = dynamicBuriedPointHandler.onDynamicParams();
            }
        }
        return handleBuriedPoint(moduleId, eventId, source, dynamicParams, instance, args);
    }

    /**
     * @param moduleId      功能模块
     * @param eventId       事件id
     * @param source        事件上传所需参数 json串形式{"key":value}
     * @param dynamicParams 事件上传所需动态参数，静态方法不支持获取动态参数
     * @param instance      调用该方法的对象，如果是静态方法，则instance为null
     * @param args          使用BuriedPoint注解的方法参数
     * @return
     */
    protected abstract boolean handleBuriedPoint(String moduleId, String eventId, String source,
                                                 HashMap<String, Object> dynamicParams, Object instance, Object[] args);
}
