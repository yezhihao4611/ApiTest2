package jinhanyu.apitest2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import jinhanyu.apitest2.viewpager.ViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    public static int RQ = 110;
    String url;
    String str;
    public static final int MSG = 99;
    String channel_type;
    String channel_id;
    int news_number;
    ListView lv_news;
    NewsInfo newsInfo;
    PicNewsInfo picNewsInfo;
    List<NewsInfo> list;
    NewsAdapter newsAdapter;
    NewsAdapter2 newsAdapter2;
    ViewPagerAdapter viewPagerAdapter;
    CircleIndicator indicator;
    ViewPager viewpager;
    LoadingHeadView loadingHeadView;
    PtrFrameLayout store_house_ptr_frame;
    RefreshableView refreshableView;
    private BGARefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        channel_type = ApiConstants.HEADLINE_TYPE;
        channel_id = ApiConstants.HEADLINE_ID;
        news_number = 0;
        list = new ArrayList<>();
        refresh();
        initRefreshLayout(mRefreshLayout);


//        store_house_ptr_frame = (PtrFrameLayout) findViewById(R.id.store_house_ptr_frame);
//        loadingHeadView = new LoadingHeadView(this);
//
//        store_house_ptr_frame.setHeaderView(loadingHeadView);
//        store_house_ptr_frame.addPtrUIHandler(loadingHeadView);
//        store_house_ptr_frame.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                frame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                        store_house_ptr_frame.refreshComplete();
//                    }
//                }, 500);
//            }
//        });

//        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
//        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                list = new ArrayList<>();
//                refresh();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                refreshableView.finishRefreshing();
//            }
//        }, 0);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("url", ((NewsInfo) lv_news.getItemAtPosition(i)).getNewsUrl());
                startActivityForResult(intent, RQ);
            }
        });
    }


    private void initView() {
        lv_news = (ListView) findViewById(R.id.lv_news);
//        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewpager = (ViewPager) findViewById(R.id.vp_imageNews);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG:
                    if (str == null) {
                        Toast.makeText(MainActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jso = new JSONObject(str);
                            JSONArray jsaData = jso.getJSONArray(channel_id);

                            for (int i = 0; i < jsaData.length(); i++) {
                                newsInfo = new NewsInfo();
                                JSONObject jsoData = (JSONObject) jsaData.get(i);
                                if (jsoData.isNull("ads")) {
                                    if (jsoData.isNull("url")) {
                                        continue;
                                    } else {
                                        newsInfo.setTitle(jsoData.getString("title"));
                                        newsInfo.setImageUrl(jsoData.getString("imgsrc"));
                                        newsInfo.setNewsUrl(jsoData.getString("url"));
                                    }
                                    list.add(newsInfo);
                                } else {
                                    JSONArray jsaAds = jsoData.getJSONArray("ads");
                                    picNewsInfo = new PicNewsInfo();
                                    for (int j = 0; j < jsaAds.length(); j++) {
                                        JSONObject jsoAds = (JSONObject) jsaAds.get(j);
                                        picNewsInfo.setTitle(jsoAds.getString("title"));
                                        picNewsInfo.setImageUrl(jsoAds.getString("imgsrc"));
                                    }
//                                viewpager.setAdapter(viewPagerAdapter);
//                                indicator.setViewPager(viewpager);
                                }
                            }
                            newsAdapter = new NewsAdapter(MainActivity.this, list);
                            if (news_number == 0) {
                                lv_news.setAdapter(newsAdapter);
                            } else {
                                newsAdapter.notifyDataSetInvalidated();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    public void refresh() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                url = ApiConstants.NEWS_DETAIL + channel_type + "/" + channel_id + "/" + news_number + ApiConstants.END_URL;
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    str = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(MSG);
            }
        }.start();
    }


    private void initRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGARefreshViewHolder(this, true) {
            @Override
            public View getRefreshHeaderView() {
                return null;
            }

            @Override
            public void handleScale(float scale, int moveYDistance) {

            }

            @Override
            public void changeToIdle() {

            }

            @Override
            public void changeToPullDown() {

            }

            @Override
            public void changeToReleaseRefresh() {

            }

            @Override
            public void changeToRefreshing() {

            }

            @Override
            public void onEndRefreshing() {

            }
        };
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
                news_number = 0;
                list = new ArrayList<>();
                refresh();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束下拉刷新
                mRefreshLayout.endRefreshing();
//                        mDatas.addAll(0, DataEngine.loadNewData());
//                        mAdapter.setDatas(mDatas);
            }
        }.execute();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                news_number += 20;
                refresh();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束加载更多
                mRefreshLayout.endLoadingMore();
//                        mAdapter.addDatas(DataEngine.loadMoreData());
            }
        }.execute();
        return true;
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    // 通过代码方式控制进入加载更多状态
    public void beginLoadingMore() {
        mRefreshLayout.beginLoadingMore();
    }

}
