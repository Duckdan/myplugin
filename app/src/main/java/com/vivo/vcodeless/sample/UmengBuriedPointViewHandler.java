package com.vivo.vcodeless.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.buried.point.BuriedPointViewHandler;

/**
 * //TOTO 友盟/神策....等第三方SDK，用户可以使用方法
 */
public class UmengBuriedPointViewHandler implements BuriedPointViewHandler {
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(Object object, DialogInterface dialogInterface, int which) {

    }

    @Override
    public void onItemClick(Object object, AdapterView parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(Object object, AdapterView parent, View view, int position, long id) {

    }

    @Override
    public void onGroupClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, long id) {

    }

    @Override
    public void onChildClick(Object thisObject, ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

    }

    @Override
    public void onStopTrackingTouch(Object thisObj, SeekBar seekBar) {

    }

    @Override
    public void onRatingChanged(Object thisObj, RatingBar ratingBar, float rating, boolean fromUser) {

    }

    @Override
    public void onCheckedChanged(Object object, RadioGroup radioGroup, int checkedId) {

    }

    @Override
    public void onCheckedChanged(Object object, CompoundButton button, boolean isChecked) {

    }

    @Override
    public void onPreferenceChange(Object object, Preference preference, Object newValue) {

    }

    @Override
    public void onPreferenceClick(Object object, Preference preference) {

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.e("PluginAgent","onActivityStarted 第三方处理的位置");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.e("PluginAgent","onActivityResumed 第三方处理的位置");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
