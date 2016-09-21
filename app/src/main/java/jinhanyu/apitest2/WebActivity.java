package jinhanyu.apitest2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class WebActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    WebView wv_newsDetail;
    RefreshableView refreshableView;
    private BGARefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initRefreshLayout(mRefreshLayout);

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

    private void initRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, false);

//             设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

//         为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
//         设置正在加载更多时不显示加载更多控件
        mRefreshLayout.setIsShowLoadingMoreView(true);
//         设置正在加载更多时的文本
//            refreshViewHolder.setLoadingMoreText("正在加载...");
//         设置整个加载更多控件的背景颜色资源id
//            refreshViewHolder.setLoadMoreBackgroundColorRes();
//         设置整个加载更多控件的背景drawable资源id
//            refreshViewHolder.setLoadMoreBackgroundDrawableRes();
//         设置下拉刷新控件的背景颜色资源id
//            refreshViewHolder.setRefreshViewBackgroundColorRes();
//         设置下拉刷新控件的背景drawable资源id
//            refreshViewHolder.setRefreshViewBackgroundDrawableRes();
//         设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//            mRefreshLayout.setCustomHeaderView(mBanner, false);
//         可选配置  -------------END
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                finish();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束下拉刷新
                mRefreshLayout.endRefreshing();
            }
        }.execute();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void initView() {
        wv_newsDetail = (WebView) findViewById(R.id.wv_newsDetail);
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
