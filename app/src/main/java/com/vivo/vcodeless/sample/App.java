package com.vivo.vcodeless.sample;

import android.app.Application;

import com.buried.point.PluginAgent;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        PluginAgent.init(this)
                //添加自定义事件处理类
                .addBuriedPointHandler(new UmengBuriedPointHandler())
                //添加页面浏览事件处理类
                .addBuriedPointViewHandler(new UmengBuriedPointViewHandler());
    }
}
