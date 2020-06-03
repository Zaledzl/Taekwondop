package com.example.taekwondop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taekwondop.BLE.DeviceScanActivity;
//import com.example.taekwondop.BLE.DeviceScanActivity;

public class MainActivity extends AppCompatActivity {

    private Button main_fight,main_help,main_setting,main_bluetooth,main_record,main_train;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////onCreate 是启动
        super.onCreate(savedInstanceState);
        //设置界面的形式
        setContentView(R.layout.activity_main);
        main_bluetooth = findViewById(R.id.main_bluetooth);
        main_setting = findViewById(R.id.main_setting);
        main_help= findViewById(R.id.main_help);
        main_fight = findViewById(R.id.main_fight);
        main_record = findViewById(R.id.main_record);
        main_train = findViewById(R.id.main_train);
        //先根据控件ID找到控件，然后调用setListener
        setListener();
    }

    //设置监听器 给每一个事件设置点击事件
    private void setListener(){
        //onclick实现了OnClick类
        OnClick onclick = new OnClick();
        main_fight.setOnClickListener(onclick);
        main_help.setOnClickListener(onclick);
        main_setting.setOnClickListener(onclick);
        main_bluetooth.setOnClickListener(onclick);
        main_record.setOnClickListener(onclick);
        main_train.setOnClickListener(onclick);

    }

    //OnClick 实现了 ONClickListener接口
    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.main_bluetooth:
                    intent = new Intent(MainActivity.this, DeviceScanActivity.class);
                    break;
                case R.id.main_fight:
                    intent = new Intent(MainActivity.this,FightActivity.class);
                    break;
                case R.id.main_help:
                    intent = new Intent(MainActivity.this,HelpActivity.class);
                    break;
                case R.id.main_setting:
                    intent = new Intent(MainActivity.this, FightSettingActivity.class);
                    break;
                case R.id.main_record:
                    intent = new Intent(MainActivity.this, RecordActivity.class);
                    break;
                case R.id.main_train:
                    intent = new Intent(MainActivity.this, TrainActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}
