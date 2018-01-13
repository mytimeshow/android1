package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2018/1/9
 */

public class BaseWebView extends FrameLayout {

    private ProgressBar webProgressBar;
    public WebView webView;

    public BaseWebView(Context context) {
        super(context);
        initView(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_base_web,this,true);

        webProgressBar=findViewById(R.id.web_progressbar);
        webView=findViewById(R.id.web_webview);

        setWebView();
    }

    private void loadUrl(String url){
        webView.loadUrl(url);
    }

    private void showProgressBar(boolean show){
        webProgressBar.setVisibility(show?VISIBLE:GONE);
    }

    private void setWebView() {

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.toString();
                if (Build.VERSION.SDK_INT >= 21) {
                    url = request.getUrl().toString();
                }
                view.loadUrl(url);
                return true;
            }

/*            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                LogUtil.i("shouldInterceptRequest####",url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }*/
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webProgressBar.setVisibility(View.GONE);
                } else {
                    webProgressBar.setVisibility(View.VISIBLE);
                    webProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //if (titleStr == null) titleT.setText(title);
            }
        });

        //暴露接口
        //webView.addJavascriptInterface(this, "android");

        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
        //webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作

        //webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        //webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //缓存
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //webview中缓存

        //加速加载
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }
}
