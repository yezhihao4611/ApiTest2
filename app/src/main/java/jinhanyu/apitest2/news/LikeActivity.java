package jinhanyu.apitest2.news;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jinhanyu.apitest2.R;

/**
 * Created by Administrator on 2016/9/30 0030.
 */

public class LikeActivity extends AppCompatActivity {
    NewsInfo newsInfo;
    ListView lv_like_or_history;
    List<NewsInfo> list;
    NewsAdapter newsAdapter;
    LikeDataHelper likeDataHelper;
    SQLiteDatabase likeSQLiteReadableDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_or_history);

        lv_like_or_history = (ListView) findViewById(R.id.lv_like_or_history);

        list = new ArrayList<>();

        likeDataHelper=new LikeDataHelper(LikeActivity.this);
        likeSQLiteReadableDatabase=likeDataHelper.getReadableDatabase();
        Cursor cursor = likeSQLiteReadableDatabase.query(LikeDataHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newsInfo=new NewsInfo();
                newsInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newsInfo.setImageUrl(cursor.getString(cursor.getColumnIndex("imgsrc")));
                newsInfo.setNewsUrl(cursor.getString(cursor.getColumnIndex("url")));
                newsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(0,newsInfo);

            }

            newsAdapter = new NewsAdapter(LikeActivity.this, list);
            lv_like_or_history.setAdapter(newsAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        likeSQLiteReadableDatabase.close();
        likeDataHelper.close();
    }

}
