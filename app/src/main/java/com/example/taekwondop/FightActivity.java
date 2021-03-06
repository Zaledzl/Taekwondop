package com.example.taekwondop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taekwondop.util.ApplicationRecorder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FightActivity extends AppCompatActivity {

    private final static String TAG = FightActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    static long recv_cnt = 0;

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
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

    private BluetoothLeServicep mBluetoothLeServicep;
    private ApplicationRecorder app ;

    int sendIndex = 0;
    int sendDataLen=0;
    byte[] sendBuf;



    CountDownTimer timer;

//    private EditText et_fight_setting_red_name,et_fight_setting_blue_name,et_fight_setting_fight,et_fight_setting_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        //获取蓝牙的名字和地址
        // getIntent   Return the intent that started this activity.
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
//        Log.d("进入函数 得到地址",mDeviceAddress);
        if(mDeviceAddress!=null){
            Log.d("进入函数 得到地址",mDeviceAddress);

        }

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
//        connectConfirm();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        btn_fight_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(FightActivity.this,IntroductionActivity.class);
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
                tv_red_fine.setText("0");
                tv_blue_fine.setText("0");

            }
        });

        btn_fight_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FightActivity.this, "已保存比赛结果", Toast.LENGTH_SHORT).show();
                String date = getDateString();
                save(date);
//                Intent toDetail = new Intent(FightActivity.this,RecordDetailActivity.class);
//                toDetail.putExtra(date,date);
//                startActivity(toDetail);
//                Log.d("进入函数 已经发送Intent","666");
            }
        });

        /**
         * 红方加分
         */

        btn_fight_redd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
//                Log.d("进入函数 加分点击 获取String:",tvredscore);
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
//                Log.d("进入函数 加分后",String.valueOf(tvredscore1));
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
                String tvbluescore =tv_blue_score.getText().toString();
                double  tvbluescore1 = Double.parseDouble(tvbluescore);
                tvbluescore1 = tvbluescore1+0.5;
                tv_blue_score.setText(String.valueOf(tvbluescore1));

                String tvredfine =tv_red_fine.getText().toString();
                double  tvredfine1 = Double.parseDouble(tvredfine);
                tvredfine1 = tvredfine1+0.5;
                tv_red_fine.setText(String.valueOf(tvredfine1));

            }
        });

        btn_red_deduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvbluescore =tv_blue_score.getText().toString();
                double  tvbluescore1 = Double.parseDouble(tvbluescore);
                tvbluescore1 = tvbluescore1+1;
                tv_blue_score.setText(String.valueOf(tvbluescore1));

                String tvredfine =tv_red_fine.getText().toString();
                double  tvredfine1 = Double.parseDouble(tvredfine);
                tvredfine1 = tvredfine1+1;
                Log.d("进入函数 红方判罚后", String.valueOf(tvredfine1));
                tv_red_fine.setText(String.valueOf(tvredfine1));
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

                String tvbluefine =tv_blue_fine.getText().toString();
                double  tvbluefine1 = Double.parseDouble(tvbluefine);
                tvbluefine1 = tvbluefine1+0.5;
                tv_blue_fine.setText(String.valueOf(tvbluefine1));
            }
        });

        btn_blue_deduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+1;
                tv_red_score.setText(String.valueOf(tvredscore1));

                String tvbluefine =tv_blue_fine.getText().toString();
                double  tvbluefine1 = Double.parseDouble(tvbluefine);
                tvbluefine1 = tvbluefine1+1;
                tv_blue_fine.setText(String.valueOf(tvbluefine1));
            }
        });

//        //先根据控件ID找到控件，然后调用setListener
//        setListener();
        /**
         * 补点东西
         */
        // 绑定服务
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }



    @Override
    protected void onResume() {  //重新注册recever 重新连接蓝牙服务
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeServicep != null) {
            final boolean result = mBluetoothLeServicep.connect(app.getBluetoothMac());
            Log.d("TAG", "Connect p1_head request result=" + result);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        if(mBluetoothLeServicep!=null)
            mBluetoothLeServicep.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // mConnectionState.setText(resourceId);
            }
        });
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("进入函数 收到Action","收到Action");
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d("ACTION_GATT_CONNECTED","ACTION_GATT_CONNECTED");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                mBluetoothLeService.connect(mDeviceAddress);
                Log.d("ACTION_GATT_DISCONNECTED","ACTION_GATT_DISCONNECTED");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //特征值找到才代表连接成功
                mConnected = true;
                updateConnectionState(R.string.connected);
                Log.d("ACTION_GATT_SERVICES_DISCOVERED","ACTION_GATT_SERVICES_DISCOVERED");
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)){
                mBluetoothLeService.connect(mDeviceAddress);
                Log.d("ACTION_GATT_SERVICES_NO_DISCOVERED","ACTION_GATT_SERVICES_NO_DISCOVERED");
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // 得到字节码数据
                Log.d("ACTION_DATA_AVAILABLE","ACTION_DATA_AVAILABLE");
//                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
                Log.d("进入函数 输出信息",intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA).toString());
                Log.d("进入函数 输出信息",bytesToString(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA)));

                String code = bytesToString(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
                Log.d("进入函数 code",code);
//                String codee = "F628F6280124801A000600031072C29DA5A5A5A5";
//                Log.d("进入函数 code",codee);
//                Log.d("进入函数 codee", String.valueOf(code.equals(codee)));
                code = code.substring(20,24);
                Log.d("进入函数 缩减后的code",code);

                switch (code){
                    case "0002":
                        String tvbluescore =tv_blue_score.getText().toString();
                        double  tvbluescore1 = Double.parseDouble(tvbluescore);
                        tvbluescore1 = tvbluescore1+1;
                        tv_blue_score.setText(String.valueOf(tvbluescore1));
                        break;
                    case "0003":
                        String tvbluescore2 =tv_blue_score.getText().toString();
                        double  tvbluescore3 = Double.parseDouble(tvbluescore2);
                        tvbluescore3 = tvbluescore3+3;
                        tv_blue_score.setText(String.valueOf(tvbluescore3));
                        break;
                    case "0200":
                        String tvredscore =tv_red_score.getText().toString();
                        double  tvredscore1 = Double.parseDouble(tvredscore);
                        tvredscore1 = tvredscore1+1;
                        tv_red_score.setText(String.valueOf(tvredscore1));
                        break;
                    case "0300":
                        String tvredscore2 =tv_red_score.getText().toString();
                        double  tvredscore3 = Double.parseDouble(tvredscore2);
                        tvredscore3 = tvredscore3+3;
                        tv_red_score.setText(String.valueOf(tvredscore3));
                        break;
                    case "0100":
                        String tvredscore4 =tv_red_score.getText().toString();
                        double  tvredscore5 = Double.parseDouble(tvredscore4);
                        tvredscore5 = tvredscore5-1;
                        tv_red_score.setText(String.valueOf(tvredscore5));
                        break;
                    case "0001":
                        String tvbluescore4 =tv_blue_score.getText().toString();
                        double  tvbluescore5 = Double.parseDouble(tvbluescore4);
                        tvbluescore5 = tvbluescore5-1;
                        tv_blue_score.setText(String.valueOf(tvbluescore5));
                        break;
                    default:
                        Log.v("Tag进入函数","codecodecode");
                        break;
                }
                if(code.equals("F628F6280137801A00060003105FC29DA5A5A5A5")){
                    Log.d("进入函数 if了","if");
                }



            }else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
                Log.d("ACTION_WRITE_SUCCESSFUL","ACTION_WRITE_SUCCESSFUL");
                if (sendDataLen>0)
                {
                    Log.v("log","Write OK,Send again");
                }
                else {
                    Log.v("log","Write Finish");
                }
            }

//            HashMap<String,String> map = InfoCenter.messageBuffer(intent.getByteArrayExtra(BluetoothLeServicep.EXTRA_DATA));
//            Log.d("进入函数 接收信息",intent.getByteArrayExtra(BluetoothLeServicep.EXTRA_DATA).toString());
//            dealMessage(map);
        }
    };

    final int SPEED = 1;



    private void dealMessage(HashMap<String,String> map){
        String name = map.get("name");
        tv_red_score = findViewById(R.id.tv_red_score);
        tv_blue_score = findViewById(R.id.tv_blue_score);
        String power = map.get("power");
        if(power.equals("power_low")){
            Toast.makeText(FightActivity.this, "电池电量低", Toast.LENGTH_SHORT).show();
        }
        switch (name){
            case "red_head":
                String tvbluescore =tv_blue_score.getText().toString();
                double  tvbluescore1 = Double.parseDouble(tvbluescore);
                tvbluescore1 = tvbluescore1+3;
                tv_blue_score.setText(String.valueOf(tvbluescore1));
            case "red_body":
                String tvbluescore2 =tv_blue_score.getText().toString();
                double  tvbluescore3 = Double.parseDouble(tvbluescore2);
                tvbluescore3 = tvbluescore3+1;
                tv_blue_score.setText(String.valueOf(tvbluescore3));
                break;
            case "blue_head":
                String tvredscore =tv_red_score.getText().toString();
                double  tvredscore1 = Double.parseDouble(tvredscore);
                tvredscore1 = tvredscore1+3;
                tv_red_score.setText(String.valueOf(tvredscore1));
                break;
            case "blue_body":
                String tvredscore2 =tv_red_score.getText().toString();
                double  tvredscore3 = Double.parseDouble(tvredscore2);
                tvredscore3 = tvredscore3+1;
                tv_red_score.setText(String.valueOf(tvredscore3));
                break;
            default:
                Log.v("Tag进入函数","检测到无法确定来源的心跳码");
                break;
        }
    }

    private String getDateString(){//获取当前系统时间(用做记录保存的键值)
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
        String str = formatter.format(curDate);
//        Log.d("进入函数 time",curDate.toString());
        return str;
    }

    //保存当前比赛信息
    private void save(String date){
        SharedPreferences sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件

        String redName = tv_fight_red_name.getText().toString();
        String blueName = tv_fight_blue_name.getText().toString();
        String redScore = tv_red_score.getText().toString();
        String blueScore = tv_blue_score.getText().toString();

        String name1 = date+"red";
        String name2 = date+"blue";

        String redInfo = redName+" 得分 :"+redScore;
        String blueInfo = blueName+" 得分 :"+blueScore;

        editor.putString(date,date);
        editor.putString(name1,redInfo);
        editor.putString(name2,blueInfo);

        //步骤4：提交
        editor.apply();
    }




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
//                    Log.d("进入函数 onTick", "进入函数+ onTick");
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


    /**
     * 绑定服务是客户端--服务器接口中的服务器。组件（如activity）和服务进行绑定后，可以发送请求、接收响应、执行进程间通信（IPC）。不会无限期在后台运行。
     *
     * 要提供服务绑定，必须实现onBind()回调方法，该方法返回的IBinder对象定义了客户端用来与服务进行交互的编程接口。
     *
     * 客户端可以通过调用bindService()绑定到服务。调用时，必须提供ServiceConnection的实现，后者会监控与服务的连接，当Android系统创建客户端与服务之间的连接时，
     * 会对ServiceConnection回调onServiceConnected()，向客户端传递用来与服务通信的IBinder。当实现绑定服务的时候，最重要的环节是定义onBind()回调方法返回的接口。
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            // Activity与Service的通信方式有三种:其中第一种也是最简单的一种, 即LocalBinder方式
            //特点:Activity和Service位于同一个进程内, 简单,方便,可以实现activity和service之间的函数互相调用.
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            // 如果初始化蓝牙服务失败，那就说明失败
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // 根据设备地址，用蓝牙服务连接到设备
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        // 在蓝牙服务断开连接的时候，要把初始化的实例赋值为NULL
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private void connectConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FightActivity.this);
        builder.setTitle("提示");
        builder.setMessage("已检测到蓝牙设备地址"+"\n"+"是否连接？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(app.getBluetoothMac()!=null) {
                    Intent gattServiceIntent1 = new Intent(FightActivity.this, BluetoothLeServicep.class);
                    bindService(gattServiceIntent1, mServiceConnection, BIND_AUTO_CREATE);
                    Log.d("进入函数 bindService","bindService");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public static String bytesToString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
//            sb.append(' ');  这个空格是为了有时候观察方便
        }
        return sb.toString();
    }


}