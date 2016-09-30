package jinhanyu.apitest2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import jinhanyu.apitest2.news.ApiConstants;
import jinhanyu.apitest2.news.HistoryActivity;
import jinhanyu.apitest2.news.HistoryDataHelper;
import jinhanyu.apitest2.news.LikeActivity;
import jinhanyu.apitest2.news.LikeDataHelper;
import jinhanyu.apitest2.news.NewsAdapter;
import jinhanyu.apitest2.news.NewsDataHelper;
import jinhanyu.apitest2.news.NewsInfo;
import jinhanyu.apitest2.news.WebActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anzhuo on 2016/9/20.
 */

public class VpSimpleFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private String mTitle;
    public static final String BUNDLE_TITLE = "title";
    ApiConstants a = new ApiConstants();
    public String[][] newsContent = {{"头条", a.HEADLINE_ID, a.HEADLINE_TYPE}, {"财经", a.FINANCE_ID, a.OTHER_TYPE},
            {"游戏", a.GAME_ID, a.OTHER_TYPE}, {"电影", a.MOVIE_ID, a.OTHER_TYPE},
            {"娱乐", a.ENTERTAINMENT_ID, a.OTHER_TYPE}, {"军事", a.MILITARY_ID, a.OTHER_TYPE},
            {"手机", a.PHONE_ID, a.OTHER_TYPE}, {"体育", a.SPORTS_ID, a.OTHER_TYPE},
            {"科技", a.TECH_ID, a.OTHER_TYPE}, {"汽车", a.CAR_ID, a.OTHER_TYPE},
            {"时尚", a.FASHION_ID, a.OTHER_TYPE}, {"情感", a.EMOTION_ID, a.OTHER_TYPE},
            {"精选", a.CHOICE_ID, a.OTHER_TYPE}, {"电台", a.RADIO_ID, a.OTHER_TYPE},
            {"数码", a.DIGITAL_ID, a.OTHER_TYPE}, {"NBA", a.NBA_ID, a.OTHER_TYPE},
            {"移动", a.MOBILE_ID, a.OTHER_TYPE}, {"彩票", a.LOTTERY_ID, a.OTHER_TYPE},
            {"教育", a.EDUCATION_ID, a.OTHER_TYPE}, {"论坛", a.FORUM_ID, a.OTHER_TYPE},
            {"旅游", a.TOUR_ID, a.OTHER_TYPE}, {"博客", a.BLOG_ID, a.OTHER_TYPE},
            {"社会", a.SOCIETY_ID, a.OTHER_TYPE}, {"家居", a.FURNISHING_ID, a.OTHER_TYPE},
            {"暴雪", a.BLIZZARD_ID, a.OTHER_TYPE}, {"亲子", a.PATERNITY_ID, a.OTHER_TYPE},
            {"CBA", a.CBA_ID, a.OTHER_TYPE}, {"足球", a.FOOTBALL_ID, a.OTHER_TYPE},
            {"房产", a.HOUSE_ID, a.HOUSE_TYPE}, {"笑话", a.JOKE_ID, a.OTHER_TYPE}};


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
    NewsDataHelper newsDataHelper;
    LikeDataHelper likeDataHelper;
    HistoryDataHelper historyDataHelper;
    SQLiteDatabase likeSqLiteWritableDatabase;
    SQLiteDatabase likeSqLiteReadableDatabase;
    SQLiteDatabase historySQLiteWritableDatabase;
    SQLiteDatabase sqLiteWritableDatabase;
    SQLiteDatabase sqLiteReadableDatabase;
    SQLiteDatabase historySQLiteReadableDatabase;
    ContentValues values;
    ContentValues likeValues;
    ContentValues historyValues;
    private BGARefreshLayout mRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news, null);
        lv_news = (ListView) view.findViewById(R.id.lv_news);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(BUNDLE_TITLE);
        }
        for (int i = 0; i < newsContent.length; i++) {
            if (newsContent[i][0].equals(mTitle)) {
                channel_id = newsContent[i][1];
                channel_type = newsContent[i][2];
            }
        }
        news_number = 0;
        news_refreshnumber = 1;


        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this.getContext(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


        //初始化list
        list = new ArrayList<>();

        showNews();

        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把点击的新闻保存在历史记录中
                historyDataHelper = new HistoryDataHelper(getActivity());
                historyValues = new ContentValues();
                historySQLiteWritableDatabase = historyDataHelper.getWritableDatabase();
                historyValues.put("title", list.get(i).getTitle());
                historyValues.put("imgsrc", list.get(i).getImageUrl());
                historyValues.put("url", list.get(i).getNewsUrl());
                historyValues.put("time", list.get(i).getTime());
                String url[]={list.get(i).getNewsUrl()};
                historySQLiteWritableDatabase.delete(HistoryDataHelper.TABLE_NAME, "url=?", url);
                historySQLiteWritableDatabase.insert(HistoryDataHelper.TABLE_NAME, null, historyValues);

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", ((NewsInfo) lv_news.getItemAtPosition(i)).getNewsUrl());
                startActivityForResult(intent, RQ);
            }
        });
        lv_news.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("收藏这条新闻吗？");
                builder.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        likeDataHelper = new LikeDataHelper(getActivity());
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeValues.put("time", list.get(position).getTime());
                        String url[]={list.get(position).getNewsUrl()};


                        likeSqLiteWritableDatabase.delete(LikeDataHelper.TABLE_NAME, "url=?", url);
                        likeSqLiteWritableDatabase.insert(LikeDataHelper.TABLE_NAME, null, likeValues);
                        Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("取消收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        likeDataHelper = new LikeDataHelper(getActivity());
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeValues.put("time", list.get(position).getTime());
                        String url[]={list.get(position).getNewsUrl()};
                        likeSqLiteWritableDatabase.delete(LikeDataHelper.TABLE_NAME, "url=?", url);
                        Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("收藏列表", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getActivity(), HistoryActivity.class));
                        startActivity(new Intent(getActivity(), LikeActivity.class));
                    }
                });

                builder.show();
                return true;

            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static VpSimpleFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);

        VpSimpleFragment fragment = new VpSimpleFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOAD:
                    if (str == null) {
                        Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
                        loadNews();
                    } else {
                        try {
                            JSONObject jso = new JSONObject(str);
                            JSONArray jsaData = jso.getJSONArray(channel_id);
                            newsDataHelper = new NewsDataHelper(getActivity());
                            values = new ContentValues();
                            sqLiteWritableDatabase = newsDataHelper.getWritableDatabase();
                            if (news_number == 0) {
                                sqLiteWritableDatabase.delete(NewsDataHelper.TABLE_NAME, "1", null);
                            }
                            for (int i = 0; i < jsaData.length(); i++) {
                                newsInfo = new NewsInfo();
                                JSONObject jsoData = (JSONObject) jsaData.get(i);
                                if (jsoData.isNull("ads")) {
                                    if (!jsoData.isNull("url") && jsoData.getString("url") != "" && jsoData.getString("url").contains("html")) {
                                        newsInfo.setTitle(jsoData.getString("title"));
                                        newsInfo.setImageUrl(jsoData.getString("imgsrc"));
                                        newsInfo.setNewsUrl(jsoData.getString("url"));
                                        newsInfo.setTime(jsoData.getString("lmodify"));
                                        list.add(newsInfo);

                                        values.put("title", jsoData.getString("title"));
                                        values.put("imgsrc", jsoData.getString("imgsrc"));
                                        values.put("url", jsoData.getString("url"));
                                        values.put("time", jsoData.getString("lmodify"));
                                        sqLiteWritableDatabase.insert(NewsDataHelper.TABLE_NAME, null, values);
                                    }
                                }
                            }
                            sqLiteWritableDatabase.close();
                            newsAdapter = new NewsAdapter(getActivity(), list);
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
                    str = null;
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
        newsDataHelper = new NewsDataHelper(getActivity());
        sqLiteReadableDatabase = newsDataHelper.getReadableDatabase();
        Cursor cursor = sqLiteReadableDatabase.query(NewsDataHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newsInfo = new NewsInfo();
                newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                newsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(newsInfo);
            }
        }
        sqLiteReadableDatabase.close();
        newsAdapter = new NewsAdapter(getActivity(), list);
        lv_news.setAdapter(newsAdapter);
    }


    private void loadMore() {
        news_number = news_number + 20;
        showNews();
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
