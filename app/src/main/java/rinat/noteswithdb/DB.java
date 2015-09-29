package rinat.noteswithdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rinat on 27.09.2015.
 */

public class DB {

    private static final String DB_NAME = "myNote";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "notes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_NOTE = "note";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TOPIC + " text, " +
                    COLUMN_NOTE + " text" +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx,DB_NAME, null, 1);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String topic, String note) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TOPIC, topic);
        cv.put(COLUMN_NOTE, note);
        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // обновить запись из DB_TABLE
    public void updRec(long id,String topic, String note) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TOPIC, topic);
        cv.put(COLUMN_NOTE, note);
        mDB.update(DB_TABLE, cv, COLUMN_ID + " = " + id,null);
    }




    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем таблицу с полями
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
