package com.example.taekwondop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FightSettingActivity extends AppCompatActivity {

    private Button et_fight_setting_finish,et_fight_setting_finish2,et_fight_setting_finish3;

    private EditText et_fight_setting_red_name,et_fight_setting_blue_name,et_fight_setting_fight,et_fight_setting_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_setting);

        et_fight_setting_red_name = findViewById(R.id.et_fight_setting_red_name);
        et_fight_setting_blue_name = findViewById(R.id.et_fight_setting_blue_name);
        et_fight_setting_fight = findViewById(R.id.et_fight_setting_fight);
        et_fight_setting_rest = findViewById(R.id.et_fight_setting_rest);

        et_fight_setting_finish = findViewById(R.id.et_fight_setting_finish);
        et_fight_setting_finish2 = findViewById(R.id.et_fight_setting_finish2);
        et_fight_setting_finish3 = findViewById(R.id.et_fight_setting_finish3);

        // 提交双方姓名
        et_fight_setting_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("redName",et_fight_setting_red_name.getText().toString());
                bundle1.putString("blueName",et_fight_setting_blue_name.getText().toString());
                bundle1.putString("fightTime",et_fight_setting_fight.getText().toString());
                bundle1.putString("restTIme",et_fight_setting_rest.getText().toString());
                intent.putExtras(bundle1);
//                setResult(Activity.RESULT_OK,intent);
                setResult(1,intent);
                finish();
            }
        });

        // 提交每场比赛时间
        et_fight_setting_finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("redName",et_fight_setting_red_name.getText().toString());
                bundle1.putString("blueName",et_fight_setting_blue_name.getText().toString());
                bundle1.putString("fightTime",et_fight_setting_fight.getText().toString());
                bundle1.putString("restTIme",et_fight_setting_rest.getText().toString());
                intent.putExtras(bundle1);
//                setResult(Activity.RESULT_OK,intent);
                setResult(2,intent);
                finish();
            }
        });

        // 提交场间休息时间
        et_fight_setting_finish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("redName",et_fight_setting_red_name.getText().toString());
                bundle1.putString("blueName",et_fight_setting_blue_name.getText().toString());
                bundle1.putString("fightTime",et_fight_setting_fight.getText().toString());
                bundle1.putString("restTime",et_fight_setting_rest.getText().toString());
                intent.putExtras(bundle1);
//                setResult(Activity.RESULT_OK,intent);
                setResult(3,intent);
                finish();
            }
        });



    }
}
