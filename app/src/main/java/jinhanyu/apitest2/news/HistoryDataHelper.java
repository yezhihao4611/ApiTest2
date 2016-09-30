package jinhanyu.apitest2.news;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class HistoryDataHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "historyInfo.db";
    public static String TABLE_NAME = "historyInfoTb";
    public static String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement,title varchar(100),imgsrc varchar(100),url varchar(100),time varchar(40))";
    public static int TABLE_VERSION = 1;


    public HistoryDataHelper(Context context) {
        super(context, DB_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
