package jinhanyu.apitest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class WebActivity extends AppCompatActivity {
    WebView wv_newsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
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
    }

    private void initView() {
        wv_newsDetail= (WebView) findViewById(R.id.wv_newsDetail);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_newsDetail.canGoBack()) {
            wv_newsDetail.goBack();

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
