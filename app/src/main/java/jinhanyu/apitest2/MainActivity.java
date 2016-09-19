package jinhanyu.apitest2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jinhanyu.apitest2.viewpager.ViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
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
    ViewPagerAdapter viewPagerAdapter;
    CircleIndicator indicator;
    ViewPager viewpager;


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

                            list.add(newsInfo);
                        }
                        newsAdapter = new NewsAdapter(MainActivity.this, list);
                        lv_news.setAdapter(newsAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
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

}
