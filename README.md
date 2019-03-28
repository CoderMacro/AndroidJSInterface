# AndroidJSInterface
此项目，主要演示Android的WebView视图中，Native Call JavaScript、以及JavaScript Call Native的几种方式。

-------------------


> **写在前面**: Hybrid不可避免使用Native和JS交互，本文主要总结一下Android和JavaScript交互的解决方方。



[TOC]



### A、Native调用JavaScript

##### **方案一**:调用```loadUrl```执行一段js
```java
mWebView.loadUrl("javascript:alert('hello world')");
```
##### **方案二**:调用```evaluateJavascript```异步执行一段js
```java
String jsString = "sum(a, b)";
mWebView.evaluateJavascript(js, new ValueCallback<String>() {
  @Override
  public void onReceiveValue(String s) {
    // 这里可以处理被调用js方法的return
  }
});
```

##### **方案三**:引用[JsBridge](https://github.com/lzyzsd/JsBridge)框架

* Web首先初始化WebViewJavascriptBridge，注册事件监听。
* Web然后初始化接收函数回调，分为*默认接收*和*指定接收*，responseCallback回调给Native信息
* Native 调用```Webview.send()``` 或 ```WebView.callHandler()```

```javaScript
//注册事件监听，初始化
function setupWebViewJavascriptBridge(callback) {
	if (window.WebViewJavascriptBridge) {
		callback(WebViewJavascriptBridge)
	} else {
		document.addEventListener(
                'WebViewJavascriptBridgeReady',
                function() {
                    callback(WebViewJavascriptBridge)
                },
                false
        );
    }
}

//回调函数，接收java发送来的数据
setupWebViewJavascriptBridge(function(bridge) {
   //默认接收
	bridge.init(function(message, responseCallback) {
		// 处理接收到消息
	});

	//指定接收，
	bridge.registerHandler("JSFUCN", function(data, responseCallback) {
		// 处理接收到消息
	});
})
```

```java
    // 默认接收
    private void callJavaScriptDefaut() {
        mWebView.send("发送数据给js默认接收, new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
               
        });
    }
    // 指定接收
    private void callJavaScriptSpec() {
   

        mWebView.callHandler("JSFUCN", "发送数据给js指定接收", new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
               
            }
        });
    }
```



-------------------

**利用 webview调用js需要注意一下几点**

* WebSettings设置支持javascript
```java
// 设置在类之前
@SuppressLint("SetJavaScriptEnabled") 
mWebView.getSettings().setJavaScriptEnabled(true);
```
* 在运行Js脚本前，要有document对象，至少得load一个空白页
```java
webView.loadData("","text/html","UTF-8");
```
* 如果还是不行的话，就应该是因为第一个还没执行完呢
        - 从界面按钮调用 
        - 延时调用
        - 在onPageFinished中调用
```java
webView.loadData(“”,"text/html","UTF-8");
webView.loadUrl("javascript:alert('hello')");
```
* 必须实现WebChromeClient、WebViewClient
```java
webView.setWebViewClient(new WebViewClient());
webView.setWebChromeClient(new WebChromeClient());
```

* 使用JSBridge*不用*实现WebViewClient

-------------------

### B、JavaScript调用Native

##### **方案一**:拦截跳转

* WebViewClient重载```shouldOverrideUrlLoading``` 拦截URL

Web代码

``` javaScript 
function openMyBlogNewTarget() {
    window.open('https://www.jianshu.com/u/263c210b047c')
}

function openMyBlog() {
    window.location.href = 'https://www.jianshu.com/u/263c210b047c'
}
```
``` html
<a href="http://www.baidu.com" title="baidu" target="_blank">新标签页打开百度</a>
<a href="http://www.baidu.com" title="baidu" >当前页打开百度</a>

<button onclick='openMyBlogNewTarget()'>新标签打开我的博客</button>
<button onclick="openMyBlog()">打开我的博客</button>
```
Android代码

```java
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
       }
		return true;
	}
        
   // 旧方法 兼容API21之前
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		super.shouldOverrideUrlLoading(view, url);
		// 拦截url
		Log.d(TAG, "shouldOverrideUrlLoading: " + url);
		return true;
	}
}
```

##### **方案二**:JSInterface

* JS中使用window.objects.function语法调用Native，其中objects与Native中一直。
* Native中定义Class，以及被调用的func用 public、@JavascriptInterface修饰。
* Native中```addJavascriptInterface(obj, name)```。


```javaScript

function callNative() {
	var amount = 123.5;
	var orderno = "FG1afsdakewqr";
	window.mobileApp.toPay(amount, orderno);
}
```
``` html
<button onclick="window.mobileApp.sendMessage('特斯拉 Model 3')">获取商品名</button>
<button onclick="callNative()">支付按钮</button>
```

```java
mWebView.addJavascriptInterface(new JsInterface(tv), "mobileApp");


private class JsInterface {

	private TextView showText;
	
	public JsInterface(TextView tv) {
		showText = tv;
	}

	@JavascriptInterface
	public void sendMessage(String msg) {
		Log.d(TAG, "sendMessage: " + msg);

	}
        
   @JavascriptInterface
	public void toPay(float amount, String oderno) {
		Log.d(TAG, "sendMessage: " + amount);
    }
}
```

##### **方案三**:引用[JsBridge](https://github.com/lzyzsd/JsBridge)框架

* JS中调用```window.WebViewJavascriptBridge.send``` 或者 ```window.WebViewJavascriptBridge.callHandler``` 向Native发消息
* Native中注册指定接收```WebView.registerHandler```或者注册默认接收```WebView.setDefaultHandler```

```javaScript
//js传递数据给java
function jsCallNativeDefault() {
	window.WebViewJavascriptBridge.send({ 'param': "str1" },	function(responseData) { //处理java回传的数据

	});
}

function jsCallNativeSpec() {
	window.WebViewJavascriptBridge.callHandler(
	'Spec' //指定接收参数 submitFromWeb与java一致
	,'指定接收'
	,function(responseData) { //处理java回传的数据
	
	 });
}
```

```java
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

```