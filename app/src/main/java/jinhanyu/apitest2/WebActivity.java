package jinhanyu.apitest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;


/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class WebActivity extends SwipeBackActivityWithoutPullToBack {
    WebView wv_newsDetail;
    ProgressBar pb_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //从左滑动关闭
        setDragEdge(SwipeBackLayoutWithoutPullToBack.DragEdge.LEFT);
        initView();

        Intent intent = getIntent();
        wv_newsDetail.loadUrl(intent.getExtras().get("url").toString());
        WebSettings webSettings = wv_newsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wv_newsDetail.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv_newsDetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //打开网页的进度条
                pb_progressBar.setProgress(0);
                if (newProgress == 100) {
                    pb_progressBar.setVisibility(View.INVISIBLE);
                } else {
                    pb_progressBar.setVisibility(View.VISIBLE);
                    pb_progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }

    //初始化控件
    private void initView() {
        wv_newsDetail = (WebView) findViewById(R.id.wv_newsDetail);
        pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);
    }

    //设置返回键的功能为网页返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_newsDetail.canGoBack()) {
            wv_newsDetail.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
