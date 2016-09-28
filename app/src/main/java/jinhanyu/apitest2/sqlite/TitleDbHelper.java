package jinhanyu.apitest2.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anzhuo on 2016/9/27.
 */

public class TitleDbHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "titlesData.db";//文件名
    public static String TABLE_NAME = "titlesDataTb";//表名
    public static String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement, title varchar(32), content varchar(128), point varchar(64), time varchar(64), newTime varchar(64))";
    public static int TABLE_VERSION = 1;

    public TitleDbHelper(Context context) {
        super(context, DB_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
