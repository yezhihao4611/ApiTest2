package jinhanyu.apitest2.news;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import jinhanyu.apitest2.R;
import jinhanyu.apitest2.VpSimpleFragment;

/**
 * Created by Administrator on 2016/9/30 0030.
 */

public class LikeActivity extends AppCompatActivity {
    NewsInfo newsInfo;
    ListView lv_like_or_history;
    List<NewsInfo> list;
    NewsAdapter newsAdapter;
    LikeDataHelper likeDataHelper;
    HistoryDataHelper historyDataHelper;
    ContentValues historyValues;
    ContentValues likeValues;
    SQLiteDatabase historySQLiteWritableDatabase;
    SQLiteDatabase likeSQLiteReadableDatabase;
    SQLiteDatabase likeSqLiteWritableDatabase;
    public static final int MSG_SETCHANGED = 102;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_or_history);

        lv_like_or_history = (ListView) findViewById(R.id.lv_like_or_history);

        list = new ArrayList<>();

        likeDataHelper = new LikeDataHelper(LikeActivity.this);
        likeSQLiteReadableDatabase = likeDataHelper.getReadableDatabase();
        Cursor cursor = likeSQLiteReadableDatabase.query(LikeDataHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newsInfo = new NewsInfo();
                newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                newsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(0, newsInfo);

            }

            newsAdapter = new NewsAdapter(LikeActivity.this, list);
            lv_like_or_history.setAdapter(newsAdapter);
        }

        lv_like_or_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                historyDataHelper = new HistoryDataHelper(LikeActivity.this);
                historyValues = new ContentValues();
                historySQLiteWritableDatabase = historyDataHelper.getWritableDatabase();
                historyValues.put("title", list.get(position).getTitle());
                historyValues.put("imgsrc", list.get(position).getImageUrl());
                historyValues.put("url", list.get(position).getNewsUrl());
                historyValues.put("time", list.get(position).getTime());
                String url[] = {list.get(position).getNewsUrl()};
                historySQLiteWritableDatabase.delete(HistoryDataHelper.TABLE_NAME, "url=?", url);
                historySQLiteWritableDatabase.insert(HistoryDataHelper.TABLE_NAME, null, historyValues);

                Intent intent = new Intent(LikeActivity.this, WebActivity.class);
                intent.putExtra("url", ((NewsInfo) lv_like_or_history.getItemAtPosition(position)).getNewsUrl());
                startActivityForResult(intent, VpSimpleFragment.RQ);
            }
        });

        lv_like_or_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LikeActivity.this);
                builder.setTitle("取消收藏这条新闻");
                builder.setPositiveButton("取消收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        likeDataHelper = new LikeDataHelper(LikeActivity.this);
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeValues.put("time", list.get(position).getTime());
                        String url[] = {list.get(position).getNewsUrl()};
                        likeSqLiteWritableDatabase.delete(LikeDataHelper.TABLE_NAME, "url=?", url);
                        Toast.makeText(LikeActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(MSG_SETCHANGED);
                    }
                });
                builder.setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        likeSQLiteReadableDatabase.close();
        likeDataHelper.close();
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SETCHANGED:
                    list.clear();
                    Cursor cursor = likeSQLiteReadableDatabase.query(LikeDataHelper.TABLE_NAME, null, null, null, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            newsInfo = new NewsInfo();
                            newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                            newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                            newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                            newsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
                            list.add(0, newsInfo);
                        }
                        newsAdapter.notifyDataSetChanged();
                        break;
                    }
            }
        }
    };
}
