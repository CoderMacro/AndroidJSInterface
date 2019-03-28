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
import android.widget.TextView;

public class WebViewActivityJSCallNativeIntercept extends AppCompatActivity {

    private WebView mWebView;
    private TextView showTextView;
    private static final String TAG = "JSCallNativeIntercept";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_js_call_native_intercept);

        mWebView = findViewById(R.id.webv_content);
        showTextView = findViewById(R.id.showtext);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new MYWebChromeClient());
        mWebView.setWebViewClient(new MYWebViewClient());

        mWebView.loadUrl("file:////android_asset/JSCallNativeIntercept.html");
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
        // 新方法 API21以后
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            super.shouldOverrideUrlLoading(view, request);
            // 拦截Request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());
                showTextView.setText("拦截链接 :" + request.getUrl().toString());

            }
            return true;
        }
        // 旧方法 兼容API21之前
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            // 拦截url
            Log.d(TAG, "shouldOverrideUrlLoading: " + url);
            showTextView.setText("拦截链接 :" + url);
            return true;
        }
    }


}
