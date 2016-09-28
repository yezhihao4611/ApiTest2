package jinhanyu.apitest2.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import jinhanyu.apitest2.nogizaka.MainApplication;


/**
 * Created by anzhuo on 2016/9/27.
 */

public class TitleDataChange {

    private static final String[] TITLE_TURE = {"头条", "财经", "游戏", "电影", "娱乐", "军事",
            "手机", "体育", "科技", "汽车"};
    private static final String[] TITLE_FALSE = {"笑话", "游戏", "时尚", "情感", "精选", "电台",
            "数码", "NBA", "移动", "彩票", "教育", "论坛", "旅游", "博客", "社会", "家居", "暴雪",
            "亲子", "CBA", "足球", "房产"};

    ContentValues contentValues;
    TitleDbHelper titleDbHelper;
    SQLiteDatabase sqLiteDatabase;

    public void Add() {

    }

    public void Delete() {

    }

    public void initData() {
        contentValues = new ContentValues();
        titleDbHelper = new TitleDbHelper(MainApplication.getContext());
    }
}
