package com.buried.point;

import android.app.Application;
import android.content.DialogInterface;
import android.preference.Preference;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;

/*************************************
 * 处理Activity生命周期以及Android自己自带的方法，
 *
 * @Author : raodongming
 * @Date : 15:01  2020/3/5
 * @Email : 11101037@bbktel.com
 * @title : 
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public interface BuriedPointViewHandler extends Application.ActivityLifecycleCallbacks {

    // ----------------以下都是一些View 事件，自动采集的，无需用@BuriedPoint注解。

    void onClick(View view);

    void onClick(Object object, DialogInterface dialogInterface, int which) ;

    void onItemClick(Object object, AdapterView parent, View view, int position, long id) ;

    void onItemSelected(Object object, AdapterView parent, View view, int position, long id);

    void onGroupClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, long id) ;

    void onChildClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, int childPosition, long id) ;

    void onStopTrackingTouch(Object thisObj, SeekBar seekBar);

    void onRatingChanged(Object thisObj, RatingBar ratingBar, float rating, boolean fromUser);

    void onCheckedChanged(Object object, RadioGroup radioGroup, int checkedId);

     void onCheckedChanged(Object object, CompoundButton button, boolean isChecked);

     void onPreferenceChange(Object object, Preference preference, Object newValue);

     void onPreferenceClick(Object object, Preference preference);
}
