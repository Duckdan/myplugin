package com.vivo.vcodeless.sample;


import android.text.TextUtils;
import android.util.Log;

import com.buried.point.BaseBuriedPointHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 自定义事件的埋点示例
 */
public class UmengBuriedPointHandler extends BaseBuriedPointHandler {


    @Override
    protected boolean handleBuriedPoint(String moduleId, String eventId, String source, HashMap<String, Object> dynamicParams, Object instance, Object[] args) {
        Log.e("PluginAgent", moduleId + "," + eventId + "," + source + "," + instance);
        try {

            HashMap<String, Object> map = new HashMap<>();
            //如果source包含":"，那么传入的数据就是json
            if (!TextUtils.isEmpty(source) && source.contains(":")) {
                JSONObject jsonObject = new JSONObject(source);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    map.put(key, jsonObject.getString(key));
                }
            }
            //非空判断
            if (dynamicParams != null) {
                Set<String> keySet = dynamicParams.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    map.put(key, dynamicParams.get(key));
                }
            }

            //解析完静态和动态参数之后，便可以调用第三方sdk的api进行具体的埋点业务处理

            Log.e("PluginAgent", map.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
