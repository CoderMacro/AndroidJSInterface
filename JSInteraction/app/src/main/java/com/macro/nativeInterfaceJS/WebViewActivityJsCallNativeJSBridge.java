package com.macro.nativeInterfaceJS;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;


public class WebViewActivityJsCallNativeJSBridge extends AppCompatActivity {

    private BridgeWebView mWebView;
    private TextView showTextView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_js_call_native_jsbridge);

        mWebView = findViewById(R.id.webv_content);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl("file:////android_asset/JSCallNativeJSBridge.html");

//
        mWebView.setWebChromeClient(new MYWebChromeClient());
//        mWebView.setWebViewClient(new WebViewClient());



        showTextView = findViewById(R.id.showtext);

        mWebView.registerHandler("Spec", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("", "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
                showTextView.setText("指定接收:" + data);
            }

        });

        mWebView.setDefaultHandler(new DefaultHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("", "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
                showTextView.setText("默认接收:" + data);
            }
        });

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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }


}
