package com.example.taekwondop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FightActivity extends AppCompatActivity {

    private Button btn_fight_begin;
    private Button btn_fight_resume;
    private Button btn_fight_setting;
    private Button btn_fight_record;
    private Button btn_count_down_rest;
    private Button btn_count_down_fight;
    private Button btn_fight_redd1;
    private Button btn_fight_redd2;
    private Button btn_fight_redd3;
    private Button btn_fight_blued1;
    private Button btn_fight_blued2;
    private Button btn_fight_blued3;
    private Button btn_red_warning;
    private Button btn_blue_warning;
    private Button btn_red_deduct;
    private Button btn_blue_deduct;
    private Button btn_time_stop;

    private TextView tv_fight_red_name;
    private TextView tv_fight_blue_name;
    private TextView tv_red_fine;
    private TextView tv_blue_fine;
    private TextView tv_count_down;
    private TextView tv_red_score;
    private TextView tv_blue_score;

    CountDownTimer timer;

//    private EditText et_fight_setting_red_name,et_fight_setting_blue_name,et_fight_setting_fight,et_fight_setting_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        btn_blue_deduct = findViewById(R.id.btn_blue_deduct);
        btn_blue_warning = findViewById(R.id.btn_blue_warning);
        btn_red_warning = findViewById(R.id.btn_red_warning);
        btn_red_deduct = findViewById(R.id.btn_red_deduct);
        btn_count_down_fight = findViewById(R.id.btn_count_down_fight);
        btn_count_down_rest = findViewById(R.id.btn_count_down_rest);
        btn_fight_begin = findViewById(R.id.btn_fight_begin);
        btn_fight_blued1 = findViewById(R.id.btn_fight_blued1);
        btn_fight_blued2 = findViewById(R.id.btn_fight_blued2);
        btn_fight_blued3 = findViewById(R.id.btn_fight_blued3);
        btn_fight_record = findViewById(R.id.btn_fight_record);
        btn_fight_redd1 = findViewById(R.id.btn_fight_redd1);
        btn_fight_redd2 = findViewById(R.id.btn_fight_redd2);
        btn_fight_redd3 = findViewById(R.id.btn_fight_redd3);
        btn_fight_resume = findViewById(R.id.btn_fight_resume);
        btn_fight_setting = findViewById(R.id.btn_fight_setting);
        btn_time_stop = findViewById(R.id.btn_time_stop);


        tv_fight_red_name = findViewById(R.id.tv_fight_red_name);
        tv_blue_fine = findViewById(R.id.tv_blue_fine);
        tv_count_down = findViewById(R.id.tv_count_down);
        tv_fight_blue_name = findViewById(R.id.tv_fight_blue_name);
        tv_red_fine = findViewById(R.id.tv_red_fine);
        tv_red_score = findViewById(R.id.tv_red_score);
        tv_blue_score = findViewById(R.id.tv_blue_score);

//        setListener();


//        et_fight_setting_red_name = findViewById(R.id.et_fight_setting_red_name);
//        et_fight_setting_blue_name = findViewById(R.id.et_fight_setting_blue_name);
//        et_fight_setting_fight = findViewById(R.id.et_fight_setting_fight);
//        et_fight_setting_rest = findViewById(R.id.et_fight_setting_rest);

        btn_fight_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(FightActivity.this,FightSettingActivity.class);
                startActivity(intent);
            }
        });

        btn_fight_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("进入函数：","成功进入 btn_fight_begin函数");
                Intent intent = new Intent(FightActivity.this,FightSettingActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name","杜宗伦");
//                bundle.putInt("number",88);
//                intent.putExtras(bundle);
//                startActivity(intent);
                startActivityForResult(intent,0);
            }
        });

        btn_count_down_fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this,FightSettingActivity.class);
                startActivityForResult(intent,1);
            }
        });

        btn_count_down_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this,FightSettingActivity.class);
                startActivityForResult(intent,2);
            }
        });

        btn_fight_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_red_score.setText("0");
                tv_blue_score.setText("0");

            }
        });

        /**
         * 红方加分
         */

        btn_fight_redd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                Log.d("进入函数 加分点击 获取String:",tvredscore);
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
                Log.d("进入函数 加分后",String.valueOf(tvredscore1));
                tv_red_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_fight_redd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+3;
                tv_red_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_fight_redd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1-1;
                tv_red_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_red_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_blue_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+0.5;
                tv_blue_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_red_deduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_blue_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
                tv_blue_score.setText(String.valueOf(tvredscore1));
            }
        });

        /**
         * 蓝方加分
         */

        btn_fight_blued1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_blue_score.getText().toString();
                Log.d("进入函数 加分点击 获取String:",tvredscore);
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
                Log.d("进入函数 加分后",String.valueOf(tvredscore1));
                tv_blue_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_fight_blued2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_blue_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+3;
                tv_blue_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_fight_blued3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_blue_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1-1;
                tv_blue_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_blue_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+0.5;
                tv_red_score.setText(String.valueOf(tvredscore1));
            }
        });

        btn_blue_deduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
                tv_red_score.setText(String.valueOf(tvredscore1));
            }
        });



//        //先根据控件ID找到控件，然后调用setListener
//        setListener();

    }

//    //设置监听器 给每一个事件设置点击事件
//    private void setListener(){
//        //onclick实现了OnClick类
//        OnClick onclick = new OnClick();
//        btn_fight_blued1.setOnClickListener(onclick);
//        btn_fight_blued2.setOnClickListener(onclick);
//        btn_fight_blued3.setOnClickListener(onclick);
//        btn_blue_warning.setOnClickListener(onclick);
//        btn_blue_deduct.setOnClickListener(onclick);
//        btn_fight_redd1.setOnClickListener(onclick);
//        btn_fight_redd2.setOnClickListener(onclick);
//        btn_fight_redd3.setOnClickListener(onclick);
//        btn_red_warning.setOnClickListener(onclick);
//        btn_red_deduct.setOnClickListener(onclick);
//
//
//    }

//    //OnClick 实现了 ONClickListener接口
//    private class OnClick implements View.OnClickListener{
//        @Override
//        public void onClick(View v){
//            Log.d("进入函数 加分点击:","ok");
//            Intent intent = null;
//            switch (v.getId()){
//                case R.id.btn_fight_redd1:
//                    String tvredscore =tv_red_score.getText().toString();
//                    Log.d("进入函数 加分点击 获取String:",tvredscore);
//                    int  tvredscore1 = Integer.parseInt(tvredscore);
//                    tvredscore1 = tvredscore1+1;
//                    Log.d("进入函数 加分后",String.valueOf(tvredscore1));
//                    tv_red_score.setText("100");
//                    break;
//
//            }
//            startActivity(intent);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
//            Log.d("进入函数 requestcode=5", String.valueOf(requestCode));
            tv_fight_red_name.setText(data.getExtras().getString("redName"));
            tv_fight_blue_name.setText(data.getExtras().getString("blueName"));
        }
        if(resultCode == 2){
            Log.d("进入函数 requestcode=2", String.valueOf(requestCode));
            String fightTime = data.getExtras().getString("fightTime");
            int ft = Integer.parseInt(fightTime);
            Log.d("进入函数 收到设置的时间", String.valueOf(ft));
            timer = new CountDownTimer(ft*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    Log.d("进入函数 onTick", "进入函数 onTick");
                    tv_count_down.setText(String.valueOf(millisUntilFinished / 1000)) ;
                }

                @Override
                public void onFinish() {
                    Log.d("进入函数 onFinish", "进入函数 onFinish");
                    tv_count_down.setText("结束");
                }
        };
            timer.start();
            btn_time_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                }
            });

    }
        if(resultCode == 3){
            Log.d("进入函数 requestcode=3", String.valueOf(requestCode));
            String restTime = data.getExtras().getString("restTime");
            int rt = Integer.parseInt(restTime);
            Log.d("进入函数 收到设置的时间", String.valueOf(rt));
            timer = new CountDownTimer(rt*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    Log.d("进入函数 onTick", "进入函数 onTick");
                    tv_count_down.setText(String.valueOf(millisUntilFinished / 1000)) ;
                }

                @Override
                public void onFinish() {
                    Log.d("进入函数 onFinish", "进入函数 onFinish");
                    tv_count_down.setText("结束");
                }
            };
            timer.start();
            btn_time_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                }
            });
        }

//        Toast.makeText(FightActivity.this,data.getExtras().getString("title"), Toast.LENGTH_LONG).show();
    }

}
