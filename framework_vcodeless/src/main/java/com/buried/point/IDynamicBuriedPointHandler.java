package com.buried.point;

import android.net.Uri;

import java.util.HashMap;

/*************************************
 * 动态埋点的处理对象
 * @Author : 72088102
 * @Date : 17:52  2020/7/17
 * @Email :
 * @title :
 * @Company :
 * @Description : 埋点动态参数处理类，需要动态处理埋点的类必须实现该接口
 * 同时配合{@link DynamicBuriedPoint}
 ************************************/
public interface IDynamicBuriedPointHandler {
    HashMap<String, Object> onDynamicParams();
}
