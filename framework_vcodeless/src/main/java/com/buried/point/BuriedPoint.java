package com.buried.point;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

/*************************************
 * @Author : raodongming
 * @Date : 17:52  2020/2/29
 * @Email : 11101037@bbktel.com
 * @title : 
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuriedPoint {
    public static String DEFAULT_MODULE_ID = "A274";

    String moduleId() default DEFAULT_MODULE_ID;


    String eventId();

    String source() default "";
}

