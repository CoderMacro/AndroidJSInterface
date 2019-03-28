package com.macro.nativeInterfaceJS;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class WebViewActivityNativeCallJS extends AppCompatActivity {

    private BridgeWebView mWebView;
    private EditText addOneEditText;
    private TextView addTV;
    private EditText addTwoEditText;
    private Button callBtn;

    private static final String TAG = "NativeCallJS";

    private int number = 0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_native_call_js);

        number = getIntent().getIntExtra("offer", 1);





        mWebView = findViewById(R.id.webv_content);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new MYWebChromeClient());
//        mWebView.setWebViewClient(new MYWebViewClient());


//        callJavaScript_One();

        addOneEditText = findViewById(R.id.edt_add_1);
        addTV = findViewById(R.id.add_tv);
        addTwoEditText = findViewById(R.id.edt_add_2);
        callBtn = findViewById(R.id.btn);

        if (number == 1) {

            addOneEditText.setVisibility(View.GONE);
            addTwoEditText.setVisibility(View.GONE);
            addTV.setVisibility(View.GONE);
            callBtn.setText("触发JS Alter");
            mWebView.loadUrl("");
        } else {
            mWebView.loadUrl("file:////android_asset/nativeCallJs.html");
        }


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");

                if (WebViewActivityNativeCallJS.this.number == 1) {
                    WebViewActivityNativeCallJS.this.callJavaScript_One();
                } else if (WebViewActivityNativeCallJS.this.number == 2) {
                    WebViewActivityNativeCallJS.this.callJavaScript_Two();
                } else if (WebViewActivityNativeCallJS.this.number == 31) {
                    WebViewActivityNativeCallJS.this.callJavaScript_Three_1();
                } else if (WebViewActivityNativeCallJS.this.number == 32) {
                    WebViewActivityNativeCallJS.this.callJavaScript_Three_2();
                }

            }
        });
    }

    // 方案一
    private void callJavaScript_One() {
        mWebView.loadUrl("javascript:alert('hello world')");
    }

    // 方案二
    private void callJavaScript_Two() {

        String jsString = String.format("sum(%s, %s);", addOneEditText.getText().toString(), addTwoEditText.getText().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(jsString, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d(TAG, "onReceiveValue" + s);
                }
            });
        }
    }

    // 方案三-1
    private void callJavaScript_Three_1() {
        String jsString = String.format("sum(%s, %s);", addOneEditText.getText().toString(), addTwoEditText.getText().toString());

        mWebView.send("发送数据给js默认接收" + jsString, new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
                Toast.makeText(WebViewActivityNativeCallJS.this, data, Toast.LENGTH_LONG).show();
            }
        });
    }
    // 方案三-2
    private void callJavaScript_Three_2() {
        String jsString = String.format("sum(%s, %s);", addOneEditText.getText().toString(), addTwoEditText.getText().toString());

        mWebView.callHandler("JSFUCN", "发送数据给js指定接收" + jsString, new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
                Toast.makeText(WebViewActivityNativeCallJS.this, data, Toast.LENGTH_LONG).show();
            }
        });
    }


    private class MYWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }




}
