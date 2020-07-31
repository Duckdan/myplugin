package com.buried.point;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/*************************************
 * @Author : raodongming
 * @Date : 14:20  2020/3/6
 * @Email : 11101037@bbktel.com
 * @title : 
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public class CommonActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        PluginAgent.onActivityCreated(activity,savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        PluginAgent.onActivityStarted(activity);

    }

    @Override
    public void onActivityResumed(Activity activity) {
        PluginAgent.onActivityResumed(activity);

    }

    @Override
    public void onActivityPaused(Activity activity) {
        PluginAgent.onActivityPaused(activity);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        PluginAgent.onActivityStopped(activity);

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        PluginAgent.onActivitySaveInstanceState(activity,outState);

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        PluginAgent.onActivityDestroyed(activity);
    }
}
