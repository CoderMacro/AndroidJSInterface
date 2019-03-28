package com.macro.nativeInterfaceJS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Map;

public class WebViewActivityJSCallNativeIJSInterface extends AppCompatActivity {

    private WebView mWebView;
    private static final String TAG = "JSCallNativeIJSInterfac";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_js_call_native_jsinterface);

        mWebView = findViewById(R.id.webv_content);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new MYWebChromeClient());
        mWebView.setWebViewClient(new MYWebViewClient());

        mWebView.loadUrl("file:////android_asset/JSCallNativeJSInterface.html");

        mWebView.addJavascriptInterface(new JsInterface((TextView) findViewById(R.id.showtext)), "mobileApp");
    }



    private class MYWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MYWebViewClient extends  WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }


    private class JsInterface {

        private TextView showText;

        public JsInterface(TextView tv) {
            showText = tv;
        }

        @JavascriptInterface
        public void sendMessage(String msg) {
            Log.d(TAG, "sendMessage: " + msg);
            showText.setText("需要支付的商品:" + msg);

        }
        @JavascriptInterface
        public void toPay(float amount, String oderno) {
            Log.d(TAG, "sendMessage: " + amount);
            WebViewActivityJSCallNativeIJSInterface.this.showNormalDialog(amount, oderno);
        }

    }

    private void showNormalDialog(float amount, String oderno){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(WebViewActivityJSCallNativeIJSInterface.this);
        normalDialog.setTitle("确定支付订单:" + oderno);
        normalDialog.setMessage("总价" + amount);
        normalDialog.setPositiveButton("支付",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.create().show();
    }



}
