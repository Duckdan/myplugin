package com.buried.point;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;


import java.util.ArrayList;
import java.util.HashMap;

/*************************************
 *
 *  使用AOP方式，埋点代码。这里是处理埋点代码的入口点。
 *
 * @Author : raodongming
 * @Date : 17:50  2020/2/29
 * @Email : 11101037@bbktel.com
 * @title : 
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public class PluginAgent {
    public static HashMap<Integer, Pair<Integer, String>> sAliveFragMap = new HashMap<>();
    private static final String TAG = "PluginAgent";
    private static ArrayList<BaseBuriedPointHandler> mBuriedPointList = new ArrayList<>();
    private static ArrayList<BuriedPointViewHandler> mBuriedPointViewList = new ArrayList<>();
    private static CommonActivityLifecycleCallbacks gCommonLifecycleCallbacks = new CommonActivityLifecycleCallbacks();

    private static boolean gOpen = true;

    /**
     * 传入false时将会关闭自定义事件的处理
     *
     * @param gOpen
     */
    public static void setOpen(boolean gOpen) {
        PluginAgent.gOpen = gOpen;
    }

    /**
     * 初始化Plugin 插件
     *
     * @param application
     * @return
     */
    public static PluginAgent init(Application application) {
        application.registerActivityLifecycleCallbacks(gCommonLifecycleCallbacks);
        return new PluginAgent();
    }

    /**
     * 添加自定义事件埋点的处理对象
     *
     * @param handler
     */
    public PluginAgent addBuriedPointHandler(BaseBuriedPointHandler handler) {
        mBuriedPointList.add(handler);
        return this;
    }

    /**
     * 添加控件埋点事件以及页面的浏览事件的处理对象
     *
     * @param handler
     */
    public PluginAgent addBuriedPointViewHandler(BuriedPointViewHandler handler) {
        mBuriedPointViewList.add(handler);
        return this;
    }

    public static void aop(String modelId, String eventId, String source, Object instance, Object[] args) {
        if (!gOpen) {
            return;
        }
        TraceUtils.traceStart("aop");
        boolean hasHandle = false;
        if (mBuriedPointList != null) {
            LogUtils.d(TAG, "aop : " + modelId + "," + eventId + "," + source);
            for (BaseBuriedPointHandler handler : mBuriedPointList) {
                modelId = modelId == null ? BuriedPoint.DEFAULT_MODULE_ID : modelId;
                if (handleAop(modelId, eventId, source, instance, args, handler)) {
                    hasHandle = true;
                    break;
                }
            }
        }

        if (!hasHandle) {
            LogUtils.w(TAG, "未处理的埋点 : " + modelId + "," + eventId + "," + source);
        }

        TraceUtils.traceEnd("aop", 5);
    }

    private static boolean handleAop(String modelId, String eventId, String source, Object instance, Object[] args, BaseBuriedPointHandler handler) {
        return handler.handleBuriedPoint(modelId, eventId, source, instance, args);
    }


    public static void onClick(View view) {
        LogUtils.d(TAG, "onClick()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onClick(view);
            }
        }
    }

    public static void onClick(Object object, DialogInterface dialogInterface, int which) {
        LogUtils.d(TAG, "onClick(3)");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onClick(object, dialogInterface, which);
            }
        }
    }

    public static void onItemClick(Object object, AdapterView parent, View view, int position, long id) {
        LogUtils.d(TAG, "onClick()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onItemClick(object, parent, view, position, id);
            }
        }
    }

    public static void onItemSelected(Object object, AdapterView parent, View view, int position, long id) {
        LogUtils.d(TAG, "onClick()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onItemSelected(object, parent, view, position, id);
            }
        }
    }

    public static void onGroupClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, long id) {
        LogUtils.d(TAG, "onClick()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onGroupClick(thisObject, parent, v, groupPosition, id);
            }
        }
    }

    public static void onChildClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        LogUtils.d(TAG, "onChildClick()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onChildClick(thisObject, parent, v, groupPosition, childPosition, id);
            }
        }
    }

    public static void onStopTrackingTouch(Object thisObj, SeekBar seekBar) {
        LogUtils.d(TAG, "onStopTrackingTouch()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onStopTrackingTouch(thisObj, seekBar);
            }
        }
    }

    public static void onRatingChanged(Object thisObj, RatingBar ratingBar, float rating, boolean fromUser) {
        LogUtils.d(TAG, "onRatingChanged()");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onRatingChanged(thisObj, ratingBar, rating, fromUser);
            }
        }
    }

    public static void onCheckedChanged(Object object, RadioGroup radioGroup, int checkedId) {
        LogUtils.d(TAG, "onCheckedChanged(CompoundButton )");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onCheckedChanged(object, radioGroup, checkedId);
            }
        }
    }

    public static void onCheckedChanged(Object object, CompoundButton button, boolean isChecked) {
        LogUtils.d(TAG, "onCheckedChanged(CompoundButton)");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onCheckedChanged(object, button, isChecked);
            }
        }
    }


    public static void onPreferenceChange(Object object, Preference preference, Object newValue) {
        LogUtils.d(TAG, "onPreferenceChange( )");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onPreferenceChange(object, preference, newValue);
            }
        }
    }

    public static void onPreferenceClick(Object object, Preference preference) {
        LogUtils.d(TAG, "onPreferenceClick( )");

        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onPreferenceClick(object, preference);
            }
        }
    }

    public static void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtils.d(TAG, "onActivityCreated( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    public static void onActivityStarted(Activity activity) {
        LogUtils.d(TAG, "onActivityStarted( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityStarted(activity);
            }
        }
    }

    public static void onActivityResumed(Activity activity) {
        LogUtils.d(TAG, "onActivityResumed( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityResumed(activity);
            }
        }
    }

    public static void onActivityPaused(Activity activity) {
        LogUtils.d(TAG, "onActivityPaused( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityPaused(activity);
            }
        }
    }

    public static void onActivityStopped(Activity activity) {
        LogUtils.d(TAG, "onActivityStopped( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityStopped(activity);
            }
        }
    }

    public static void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtils.d(TAG, "onActivitySaveInstanceState( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    public static void onActivityDestroyed(Activity activity) {
        LogUtils.d(TAG, "onActivityDestroyed( )");
        if (mBuriedPointViewList != null) {
            for (BuriedPointViewHandler handler : mBuriedPointViewList) {
                handler.onActivityDestroyed(activity);
            }
        }
    }

    public static void onFragmentResume(Object obj) {
        addAliveFragment(obj);
    }

    public static void onFragmentPause(Object obj) {
        removeAliveFragment(obj);
    }

    private static boolean checkFragment(androidx.fragment.app.Fragment paramFragment) {
        return true;
    }

    private static boolean checkFragment(Fragment paramFragment) {
        return true;
    }

    public static void setFragmentUserVisibleHint(Object obj, boolean isUserVisibleHint) {
        if (isUserVisibleHint) {
            addAliveFragment(obj);
        } else {
            removeAliveFragment(obj);
        }
    }

    public static void onFragmentHiddenChanged(Object fragment, boolean hidden) {
        setFragmentUserVisibleHint(fragment, !hidden);
    }

    private static void addAliveFragment(Object obj) {
        View view = null;
        if (obj instanceof Fragment) {
            view = ((Fragment) obj).getView();
        } else if (obj instanceof androidx.fragment.app.Fragment) {
            view = ((androidx.fragment.app.Fragment) obj).getView();
        }
        if (null != view) {
            int viewCode = view.hashCode();
            sAliveFragMap.put(obj.hashCode(), new Pair<>(viewCode, obj.getClass().getSimpleName()));
        }
    }

    private static void removeAliveFragment(Object obj) {
        if (null != obj) {
            sAliveFragMap.remove(obj.hashCode());
        }
    }
}
