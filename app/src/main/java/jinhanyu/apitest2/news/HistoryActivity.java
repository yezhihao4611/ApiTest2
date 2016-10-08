package jinhanyu.apitest2.news;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jinhanyu.apitest2.R;
import jinhanyu.apitest2.VpSimpleFragment;

/**
 * Created by Administrator on 2016/9/30 0030.
 */

public class HistoryActivity extends AppCompatActivity {
    NewsInfo newsInfo;
    ListView lv_like_or_history;
    List<NewsInfo> list;
    NewsAdapter newsAdapter;
    LikeDataHelper likeDataHelper;
    HistoryDataHelper historyDataHelper;
    ContentValues historyValues;
    ContentValues likeValues;
    SQLiteDatabase historySQLiteReadableDatabase;
    SQLiteDatabase historySQLiteWritableDatabase;
    SQLiteDatabase likeSqLiteWritableDatabase;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_or_history);

        lv_like_or_history = (ListView) findViewById(R.id.lv_like_or_history);

        list = new ArrayList<>();

        historyDataHelper=new HistoryDataHelper(HistoryActivity.this);
        historySQLiteReadableDatabase=historyDataHelper.getReadableDatabase();
        Cursor cursor = historySQLiteReadableDatabase.query(HistoryDataHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
             newsInfo=new NewsInfo();
                newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                newsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(0,newsInfo);

            }

            newsAdapter = new NewsAdapter(HistoryActivity.this, list);
            lv_like_or_history.setAdapter(newsAdapter);
        }

        lv_like_or_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                historyDataHelper = new HistoryDataHelper(HistoryActivity.this);
                historyValues = new ContentValues();
                historySQLiteWritableDatabase = historyDataHelper.getWritableDatabase();
                historyValues.put("title", list.get(position).getTitle());
                historyValues.put("imgsrc", list.get(position).getImageUrl());
                historyValues.put("url", list.get(position).getNewsUrl());
                historyValues.put("time", list.get(position).getTime());
                String url[]={list.get(position).getNewsUrl()};
                historySQLiteWritableDatabase.delete(HistoryDataHelper.TABLE_NAME, "url=?", url);
                historySQLiteWritableDatabase.insert(HistoryDataHelper.TABLE_NAME, null, historyValues);

                Intent intent = new Intent(HistoryActivity.this, WebActivity.class);
                intent.putExtra("url", ((NewsInfo) lv_like_or_history.getItemAtPosition(position)).getNewsUrl());
                startActivityForResult(intent, VpSimpleFragment.RQ);
            }
        });

        lv_like_or_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("收藏这条新闻吗？");
                builder.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        likeDataHelper = new LikeDataHelper(HistoryActivity.this);
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeValues.put("time", list.get(position).getTime());
                        String url[]={list.get(position).getNewsUrl()};


                        likeSqLiteWritableDatabase.delete(LikeDataHelper.TABLE_NAME, "url=?", url);
                        likeSqLiteWritableDatabase.insert(LikeDataHelper.TABLE_NAME, null, likeValues);
                        Toast.makeText(HistoryActivity.this, "已收藏", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("取消收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        likeDataHelper = new LikeDataHelper(HistoryActivity.this);
                        likeValues = new ContentValues();
                        likeSqLiteWritableDatabase = likeDataHelper.getWritableDatabase();
                        likeValues.put("title", list.get(position).getTitle());
                        likeValues.put("imgsrc", list.get(position).getImageUrl());
                        likeValues.put("url", list.get(position).getNewsUrl());
                        likeValues.put("time", list.get(position).getTime());
                        String url[]={list.get(position).getNewsUrl()};
                        likeSqLiteWritableDatabase.delete(LikeDataHelper.TABLE_NAME, "url=?", url);
                        Toast.makeText(HistoryActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        newsAdapter.notifyDataSetChanged();
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
        historySQLiteReadableDatabase.close();
        historyDataHelper.close();
    }
}
