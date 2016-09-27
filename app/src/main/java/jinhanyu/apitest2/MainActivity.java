package jinhanyu.apitest2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    public static int RQ = 110; //startActivityforResult所需的RequestCode
    public static final int MSG_LOAD = 99;  //handler所需的MSG
    String url; //json的网址
    String str;  //json的内容
    String channel_type;  //频道类型
    String channel_id;//频道id
    int news_number; //新闻条目序号
    int news_refreshnumber;//刷新次数
    ListView lv_news;
    NewsInfo newsInfo;
    List<NewsInfo> list;
    NewsAdapter newsAdapter;
    ViewPager viewpager;
    NewsDataHelper newsDataHelper;
    LikeDataHelper likeDataHelper;
    SQLiteDatabase likeSqLiteWritableDatabase;
    SQLiteDatabase likeSqLiteReadableDatabase;
    SQLiteDatabase sqLiteWritableDatabase;
    SQLiteDatabase sqLiteReadableDatabase;
    ContentValues values;
    ContentValues likeValues;
    private BGARefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRefreshLayout(mRefreshLayout);
        //初始化控件
        initView();

        //初始化为“热门”频道
        channel_type = ApiConstants.HEADLINE_TYPE;
        channel_id = ApiConstants.HEADLINE_ID;
        news_number = 0;
        news_refreshnumber = 1;

        //初始化list
        list = new ArrayList<>();

        showNews();

        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("url", ((NewsInfo) lv_news.getItemAtPosition(i)).getNewsUrl());
                startActivityForResult(intent, RQ);
            }
        });
        lv_news.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("收藏这条新闻吗？");
                builder.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//
                        likeDataHelper = new LikeDataHelper(MainActivity.this);
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeSqLiteWritableDatabase.insert(LikeDataHelper.TABLE_NAME, null, likeValues);

                        Toast.makeText(MainActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//                builder.setNeutralButton("不再出现", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "该新闻不会再出现", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//                builder.show();
                return true;
            }
        });
    }


    //初始化控件
    private void initView() {
        lv_news = (ListView) findViewById(R.id.lv_news);
        viewpager = (ViewPager) findViewById(R.id.vp_imageNews);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOAD:
                    if (str == null) {
                        Toast.makeText(MainActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                        loadNews();
                    } else {
                        try {
                            JSONObject jso = new JSONObject(str);
                            JSONArray jsaData = jso.getJSONArray(channel_id);
                            newsDataHelper = new NewsDataHelper(MainActivity.this);
                            values = new ContentValues();
                            sqLiteWritableDatabase = newsDataHelper.getWritableDatabase();
                            if (news_number==0){
//                                sqLiteWritableDatabase.delete(NewsDataHelper.TABLE_NAME,"1",null);
                            }
                            for (int i = 0; i < jsaData.length(); i++) {
                                newsInfo = new NewsInfo();
                                JSONObject jsoData = (JSONObject) jsaData.get(i);
                                if (jsoData.isNull("ads")) {
                                    if (!jsoData.isNull("url") && jsoData.getString("url") != "" && jsoData.getString("url").contains("html")) {
                                        newsInfo.setTitle(jsoData.getString("title"));
                                        newsInfo.setImageUrl(jsoData.getString("imgsrc"));
                                        newsInfo.setNewsUrl(jsoData.getString("url"));
                                        list.add(newsInfo);

                                        values.put("title", jsoData.getString("title"));
                                        values.put("imgsrc", jsoData.getString("imgsrc"));
                                        values.put("url", jsoData.getString("url"));
                                        sqLiteWritableDatabase.insert(NewsDataHelper.TABLE_NAME, null, values);
                                    }
                                }
                            }
                            sqLiteWritableDatabase.close();
                            newsAdapter = new NewsAdapter(MainActivity.this, list);
                            if (news_number == 0) {
                                lv_news.setAdapter(newsAdapter);
                            } else {
                                newsAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    };

    public void showNews() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                url = ApiConstants.NEWS_DETAIL + channel_type + "/" + channel_id + "/" + news_number + ApiConstants.END_URL;
                try {
                    str=null;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    str = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(MSG_LOAD);
            }
        }.start();
    }

    public void loadNews() {
        newsDataHelper = new NewsDataHelper(MainActivity.this);
        sqLiteReadableDatabase = newsDataHelper.getReadableDatabase();
        Cursor cursor = sqLiteReadableDatabase.query(NewsDataHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newsInfo = new NewsInfo();
                newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                list.add(newsInfo);
            }
        }
        sqLiteReadableDatabase.close();
        newsAdapter = new NewsAdapter(MainActivity.this, list);
        lv_news.setAdapter(newsAdapter);
    }


    private void loadMore() {
        news_number = news_number + 20;
        showNews();
    }

    private void initRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(MainActivity.this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                    news_number = 60 + 20 * news_refreshnumber;
                    news_refreshnumber++;
                    list.clear();
                    showNews();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                    loadMore();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束加载更多
                mRefreshLayout.endLoadingMore();
            }
        }.execute();

        return true;

    }

}
