package com.example.taekwondop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetailActivity extends Activity {

    private TextView date_view ;
    private TextView result_view;
    private ListView detail_list;
    private Button delete_button;
    private TextView p1_info_view;
    private TextView p2_info_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("进入函数 接受到了Intent :","666");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        date_view=findViewById(R.id.date);
//        result_view=findViewById(R.id.result);
//        detail_list=findViewById(R.id.detail_list);
        delete_button=findViewById(R.id.delete_button);
        p1_info_view=findViewById(R.id.p1_info_view);
        p2_info_view=findViewById(R.id.p2_info_view);

        Intent this_it = this.getIntent();//这里其实应该模块化
        Bundle para = this_it.getExtras();
        final String date = para.getString("date");
        Log.d("进入函数 date:",date);
        String name1 = date+"red";
        String name2 = date+"blue";

        String record = getRecord(date);
        String p1_info = getRecord(name1);
        String p2_info = getRecord(name2);

        Log.d("进入函数 record:",record);
        Log.d("进入函数 p1_info:",p1_info);
        Log.d("进入函数 p2_info:",p2_info);
        String[] list = helperForDelete(record);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//        detail_list.setAdapter(adapter);

        p1_info_view.setText(p1_info);
        p2_info_view.setText(p2_info);

        date_view.setText(date);
//        result_view.setText(getResult(list));

        delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(RecordDetailActivity.this)
                        .setTitle("正在删除")
                        .setMessage("确认删除本条记录?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(RecordDetailActivity.this, "已删除本条记录", Toast.LENGTH_SHORT).show();
                                deleteDetail(date);
                                Intent back = new Intent(RecordDetailActivity.this,RecordActivity.class);
                                startActivity(back);
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
    }

    private String getRecord(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        return sharedPreferences.getString(key,"获取失败");
    }
    private String[] helperForDelete(String para){
        String[] temp = para.split(",");
        for(int i=0;i<temp.length;i++){
            temp[i]=temp[i].substring(0,temp[i].length());
        }
        return temp;
    }
//    private String getResult(String[] record){
//        int p1 = 0;
//        int p2 = 0;
//        for(int i=0;i<record.length;i++){
//            String[] cur = record[i].split("\\+");
//            if(cur[0].equals("p1")){
//                p1+=Integer.valueOf(cur[1]);
//            }else{
//                p2+=Integer.valueOf(cur[1]);
//            }
//        }
//        StringBuilder sb = new StringBuilder();
//        if(p1==p2){
//            sb.append("本局平局");
//        }else if(p1>p2){
//            sb.append("本局p1胜出");
//        }else{
//            sb.append("本局p2胜出");
//        }
//        sb.append("  ");
//        sb.append("p1总分:"+String.valueOf(p1)+"  ");
//        sb.append("p2总分:"+String.valueOf(p2));
//        return sb.toString();
//    }

    private void deleteDetail(String date){
        SharedPreferences sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(date);
        editor.commit();
    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
//
//        }
//        return super.dispatchKeyEvent(event);
//    }
}
