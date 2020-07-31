package com.buried.point;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*************************************
 *
 * 动态参数注解
 * @Author : 72088102
 * @Date : 17:52  2020/7/17
 * @Email :
 * @title :
 * @Company :
 * @Description :
 ************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicBuriedPoint {

    /**
     * 默认使用这个注解就是要处理动态参数，false的时候将不会再处理动态参数
     *
     * @return
     */
    boolean value() default true;
}
