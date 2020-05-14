package com.example.taekwondop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.taekwondop.R;

public class TrainActivity extends AppCompatActivity {

    private WebView mWvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        mWvMain = findViewById(R.id.wv);
        //加载本地HTML
//        mWvMain.loadUrl("file:///android_asset/UESTC.html");
        mWvMain.getSettings().setJavaScriptEnabled(true);
        mWvMain.setWebViewClient(new MyWebViewClient());
        mWvMain.loadUrl("https://m.bilibili.com/");
    }

    class MyWebViewClient extends WebViewClient{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&mWvMain.canGoBack()){
            mWvMain.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
