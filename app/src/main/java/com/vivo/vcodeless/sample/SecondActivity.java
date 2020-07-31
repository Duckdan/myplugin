package com.vivo.vcodeless.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.buried.point.BuriedPoint;
import com.buried.point.DynamicBuriedPoint;
import com.buried.point.IDynamicBuriedPointHandler;

import java.util.HashMap;

@DynamicBuriedPoint
public class SecondActivity extends AppCompatActivity implements IDynamicBuriedPointHandler {

    private String dynamicValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        dynamicValue = intent.getStringExtra("params");
    }

    @BuriedPoint(eventId = "secondActivity", source = "{'key1':'value1','key2':'value2'}")
    public void dynamicTest(View view) {
        Log.e("PluginAgent", "SecondActivity===");
    }
    @BuriedPoint(eventId = "secondActivity", source = "{'key1':'static1','key2':'static2'}")
    public static void staticTest() {
        Log.e("PluginAgent", "SecondActivity===Static");
    }

    @Override
    public HashMap<String, Object> onDynamicParams() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key3", dynamicValue);
        return map;
    }

    public void staticTest(View view) {
        staticTest();
    }
}
