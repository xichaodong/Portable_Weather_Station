package com.sara.com.portable_weather_station;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sara.com.portable_weather_station.dao.DBCreate;
import com.sara.com.portable_weather_station.gson.AQI;
import com.sara.com.portable_weather_station.util.HttpUtil;
import com.sara.com.portable_weather_station.util.Utility;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView needle;

    private DBCreate dbCreate;
    private TextView aqiText;
    private TextView pm10Text;
    private TextView pm2_5Text;
    private TextView coText;
    private TextView no2Text;
    private TextView so2Text;
    private Timer timer;  //时间
    private TextView aqi_Text;
    private int Aqi_num;

    private float degree = 0.0F;  //记录指针旋转
    private float pre_degree = 0.0F;
    private float now_degree = 0.0F;

    public void caluDegree(int aqi_num){
        float degree = 0.0F;
        if(aqi_num >= 0 && aqi_num <= 200){
            degree = 120.0F / 200 * aqi_num;
        }else if(aqi_num >200 && aqi_num <= 300){
            degree = 30.0F / 100 * (aqi_num - 200) + 120;
        }else{
            degree = 30.0F / 200 * (aqi_num - 300) + 150;
        }
        this.degree = degree;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {  //设置仪表盘指针转动动画
            //仪表盘最大是172度，这个是自己测出来的
            caluDegree(Aqi_num);
            if (pre_degree >= degree) {
                pre_degree = now_degree;
                timer.cancel();
            }
            pre_degree += 2f;
            RotateAnimation animation = new RotateAnimation(pre_degree,
                    pre_degree, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            needle.startAnimation(animation);
        }
    };

    private class NeedleTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbCreate = new DBCreate(this,"PWS.db",null,1);
        dbCreate.getWritableDatabase();
        aqiText = (TextView)findViewById(R.id.aqi);
        pm10Text = (TextView)findViewById(R.id.pm10);
        pm2_5Text = (TextView)findViewById(R.id.pm2_5);
        coText = (TextView)findViewById(R.id.co);
        no2Text = (TextView)findViewById(R.id.no2);
        so2Text = (TextView)findViewById(R.id.so2);
        aqi_Text = (TextView)findViewById(R.id.aqi_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String AQIString  = prefs.getString("aqi",null);
        if(AQIString != null){
            AQI Aqi = Utility.handleAQIResponse(AQIString);
            showAQIInfo(Aqi);
        }else {
            requestAQI("哈尔滨");
        }

        needle = (ImageView) findViewById(R.id.needle);
        // 开始转动
        timer = new Timer();
        // 设置每一秒转动一下
        timer.schedule(new NeedleTask(), 0, 100);
    }

    public void requestAQI(final String AQIid){
        String weatherUrl = "http://www.pm25.in/api/querys/aqis_by_station.json?city=" + AQIid + "&token=5j1znBVAsnSf5xQyNQyq&station_code=1129A";
        HttpUtil.senOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call,Response response) throws IOException{
                final String responseText = response.body().string();
                final AQI aqi = Utility.handleAQIResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(aqi != null ){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("aqi",responseText);
                            editor.apply();
                            showAQIInfo(aqi);
                        }else{
                            Toast.makeText(MainActivity.this, "获取空气质量信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取空气质量信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showAQIInfo(AQI aqi){
        String Aqi = aqi.getAqi();
        String pm10 = aqi.getPm10();
        String pm2_5 = aqi.getPm2_5();
        String co = aqi.getCo();
        String no2= aqi.getNo2();
        String so2 = aqi.getSo2();
        Aqi_num = Integer.parseInt(Aqi);
        aqiText.setText(Aqi);
        pm10Text.setText(pm10);
        pm2_5Text.setText(pm2_5);
        coText.setText(co);
        no2Text.setText(no2);
        so2Text.setText(so2);
        int aqi_num  = Integer.parseInt(Aqi);
        if(aqi_num >= 0 && aqi_num <= 50){
            aqi_Text.setText("优");
            aqi_Text.setTextColor(Color.rgb(129,254,138));
            aqiText.setTextColor(Color.rgb(129,254,138));
        }else if(aqi_num > 50 && aqi_num <= 100){
            aqi_Text.setText("良");
            aqi_Text.setTextColor(Color.rgb(254,235,143));
            aqiText.setTextColor(Color.rgb(254,235,143));
        }else if(aqi_num > 100 && aqi_num <= 150){
            aqi_Text.setText("轻度");
            aqi_Text.setTextColor(Color.rgb(255,162,68));
            aqiText.setTextColor(Color.rgb(255,162,68));
        }else if(aqi_num > 150 && aqi_num <= 200){
            aqi_Text.setText("中度");
            aqi_Text.setTextColor(Color.rgb(238,0,119));
            aqiText.setTextColor(Color.rgb(238,0,119));
        }else if(aqi_num > 200 && aqi_num <= 300){
            aqi_Text.setText("重度");
            aqi_Text.setTextColor(Color.rgb(196,0,98));
            aqiText.setTextColor(Color.rgb(196,0,98));
        }else{
            aqi_Text.setText("严重");
            aqi_Text.setTextColor(Color.rgb(119,0,60));
            aqiText.setTextColor(Color.rgb(119,0,60));
        }
    }
}
