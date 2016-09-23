package jinhanyu.apitest2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class NewsDataHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "newsInfo.db";
    public static String TABLE_NAME = "newsInfoTb";
    public static String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement,title varchar(200),imgsrc varchar(200),url varchar(200))";
    public static int TABLE_VERSION = 1;


    public NewsDataHelper(Context context) {
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
