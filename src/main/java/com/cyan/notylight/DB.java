package com.cyan.notylight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    /** 单例 */
    private static DB sInstance;
    private static Context _context;

    /**
     * 获取单例
     *
     * @param context
     *            Context
     * @return 单例
     */
    public static DB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DB.class) {
                if (sInstance == null) {
                    _context = context;
                    sInstance = new DB(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * just为了方便
     *
     * @param context
     */
    private DB(Context context) {
        super(context, "db_name_noty_light", null, 1);
    }

    /**
     * 真正的构造方法
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    private DB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Table.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
