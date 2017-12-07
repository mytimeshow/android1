package cn.czyugang.tcg.client.modules.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.utils.LogRui;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends BaseActivity {

    public static void startWebActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    public static void startWebActivity(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("titleT", title);
        activity.startActivity(intent);
    }

    @BindView(R.id.web_webview)
    WebView webView;
    @BindView(R.id.web_progressbar)
    ProgressBar webProgressBar;
    @BindView(R.id.title_text)
    TextView titleT;
    String titleStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        setWebView();

        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

        titleStr = getIntent().getStringExtra("title");
        if (titleStr != null) titleT.setText(titleStr);
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
                    if (View.INVISIBLE == webProgressBar.getVisibility()) {
                        webProgressBar.setVisibility(View.VISIBLE);
                    }
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
                if (titleStr == null) titleT.setText(title);
            }
        });

        //暴露接口
        webView.addJavascriptInterface(this, "android");

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


    /*
    *   返回
    * */
    @OnClick(R.id.title_back)
    public void onBack() {
        WebActivity.this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
    *   开放接口
    * */
    @JavascriptInterface
    public void logMsg() {
        LogRui.w("logMsg####");
    }

    @JavascriptInterface
    public void logMsg(String msg) {
        LogRui.w("logMsg####", msg);
    }

    @JavascriptInterface
    public void openActivity(String className) {
        try {
            if (!className.substring(0, 19).equals("com.kaiback.canteen")) return;
            Class activityClass = Class.forName(className);
            startActivity(new Intent(this, activityClass));
        } catch (Exception e) {
            LogRui.e("openActivity####", e);
        }
    }

    @JavascriptInterface
    public void openActivity(String className, Object... data) {
        if (data == null) {
            openActivity(className);
            return;
        }
        try {
            if (!className.substring(0, 19).equals("com.kaiback.canteen")) return;
            Class activityClass = Class.forName(className);
            Intent intent = new Intent(this, activityClass);
            for (int i = 0; i < data.length; i = i + 2) {
                intent.putExtra((String) data[i], (String) data[i + 1]);
            }
            startActivity(intent);
        } catch (Exception e) {
            LogRui.e("openActivity####", e);
        }
    }

    @JavascriptInterface
    public boolean openApp(String pkg, String downUrl) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);
        if (intent == null) {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW);
            openBrowser.setData(Uri.parse(downUrl));
            startActivity(openBrowser);
            return false;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return true;
    }

    @JavascriptInterface
    public boolean isExistApp(String pkg) {
        return getPackageManager().getLaunchIntentForPackage(pkg) != null;
    }

}
