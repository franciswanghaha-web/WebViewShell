package com.example.webviewshell;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WebViewShell";
    private WebView webView;
    private Button btnBack;
    private Button btnForward;
    private Button btnHome;
    private String homeUrl;
    private static final String CONFIG_FILE = "config.txt";
    private static final String DEFAULT_URL = "https://www.example.com";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 全屏设置
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        // 隐藏导航栏和状态栏
        hideSystemUI();
        
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        
        // 配置WebView设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        
        // 设置WebViewClient和WebChromeClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "加载失败: " + description, Toast.LENGTH_LONG).show();
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 注入JavaScript隐藏网页中的地址栏/导航栏元素
                hideWebAddressBar(view);
            }
        });
        
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 可以在这里添加进度条处理
            }
        });

        // 读取配置文件并加载网址
        homeUrl = readConfigUrl();
        Log.d(TAG, "Loading URL: " + homeUrl);
        webView.loadUrl(homeUrl);
        
        // 注册上下文菜单（长按/右键菜单）
        registerForContextMenu(webView);
        
        // 初始化底部导航按钮
        initNavigationButtons();
    }
    
    private void initNavigationButtons() {
        btnBack = findViewById(R.id.btn_back);
        btnForward = findViewById(R.id.btn_forward);
        btnHome = findViewById(R.id.btn_home);
        
        // 后退按钮
        btnBack.setOnClickListener(v -> {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
            } else {
                Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 前进按钮
        btnForward.setOnClickListener(v -> {
            if (webView != null && webView.canGoForward()) {
                webView.goForward();
            } else {
                Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 首页按钮
        btnHome.setOnClickListener(v -> {
            if (webView != null && homeUrl != null) {
                webView.loadUrl(homeUrl);
                Toast.makeText(this, "返回首页", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("菜单");
        menu.add(0, 1, 0, "退出程序");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // 显示确认对话框
            new AlertDialog.Builder(this)
                .setTitle("确认退出")
                .setMessage("确定要退出程序吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    finish();
                    System.exit(0);
                })
                .setNegativeButton("取消", null)
                .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private String readConfigUrl() {
        AssetManager assetManager = getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        
        try {
            InputStream inputStream = assetManager.open(CONFIG_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            
            String url = stringBuilder.toString().trim();
            if (!url.isEmpty()) {
                return url;
            }
        } catch (IOException e) {
            Log.e(TAG, "读取配置文件失败: " + e.getMessage());
            Toast.makeText(this, "配置文件读取失败，使用默认网址", Toast.LENGTH_SHORT).show();
        }
        
        return DEFAULT_URL;
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
    
    private void hideWebAddressBar(WebView view) {
        // 注入JavaScript隐藏常见的地址栏、导航栏元素
        String js = "javascript:(function() {"
            + "var styles = document.createElement('style');"
            + "styles.innerHTML = '"
            // 隐藏常见的地址栏、导航栏、标题栏类名
            + "header, .header, #header, .navbar, #navbar, .nav-bar, #nav-bar, "
            + ".address-bar, #address-bar, .url-bar, #url-bar, .top-bar, #top-bar, "
            + ".site-header, #site-header, .page-header, #page-header, "
            + ".navigation, #navigation, .nav, #nav, .menu-bar, #menu-bar, "
            + ".toolbar, #toolbar, .tool-bar, #tool-bar { display: none !important; }"
            + "body { padding-top: 0 !important; margin-top: 0 !important; }"
            + "';"
            + "document.head.appendChild(styles);"
            // 尝试隐藏一些特定的元素ID
            + "var ids = ['address-bar', 'url-bar', 'nav-bar', 'top-nav', 'header-bar'];"
            + "for (var i = 0; i < ids.length; i++) {"
            + "  var el = document.getElementById(ids[i]);"
            + "  if (el) el.style.display = 'none';"
            + "}"
            // 隐藏固定定位的顶部元素（可能是地址栏）
            + "var allElements = document.querySelectorAll('*');"
            + "for (var i = 0; i < allElements.length; i++) {"
            + "  var el = allElements[i];"
            + "  var style = window.getComputedStyle(el);"
            + "  if (style.position === 'fixed' && style.top === '0px' && el.offsetHeight < 100) {"
            + "    el.style.display = 'none';"
            + "  }"
            + "}"
            + "})();";
        view.loadUrl(js);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onBackPressed() {
        // 如果WebView可以返回上一页，则返回
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            // 否则不退出应用（或者可以调用super.onBackPressed()退出）
            // super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
