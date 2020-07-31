package com.vivo.vcodeless.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buried.point.BuriedPoint;
import com.buried.point.DynamicBuriedPoint;
import com.buried.point.IDynamicBuriedPointHandler;

import java.util.HashMap;

/**
 * 注解参数为false时，实现了IDynamicBuriedPointHandler接口也不不处理动态参数
 */
@DynamicBuriedPoint(false)
public class MainActivity extends AppCompatActivity implements IDynamicBuriedPointHandler {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click("visa", 123);
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickUmeng("umeng", 123);
            }
        });
    }

    @BuriedPoint(moduleId = "A34", eventId = "define_click")
    public void click(String product, int userid) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("params", "动态参数1");
        startActivity(intent);
        Toast.makeText(this, product + "我被点击了" + userid, Toast.LENGTH_SHORT).show();
    }


    @BuriedPoint(eventId = "umeng_click", source = "{'provider':'神策数据','number':100,'isLogin':true}")
    public void clickUmeng(String product, int userid) {
        Toast.makeText(this, product + "我被点击了" + userid, Toast.LENGTH_SHORT).show();
    }

    /**
     * 所有埋点需要用到的动态参数，都放在这里处理
     *
     * @return
     */
    @Override
    public HashMap<String, Object> onDynamicParams() {
        return null;
    }
}
