package pl.kebapp.byjacob.fows2016.SQLiteV2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ByJacob on 2016-10-04.
 */

public class PrelegenciDbAdapter {
    private static final String DEBUG_TAG = "SqLitePrelegenciDb";
    private static int DB_VERSION = 1;
    private static final String DB_NAME = "fows2016prelegenci_database.db";
    private static final String DB_PRELEGENCI_TABLE = "fows2016_prelegenci";

    //Kolumny
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;

    public static final String KEY_NAME = "name";
    public static final String NAME_OPTIONS = "TEXT DEFAULT 'brak'";
    public static final int NAME_COLUMN = 1;

    public static final String KEY_INFO = "info";
    public static final String INFO_OPTIONS = "TEXT DEFAULT 'brak'";
    public static final int INFO_COLUMN = 2;

    public static final String KEY_URLPICTURE = "URLpicture";
    public static final String URLPICTURE_OPTIONS = "TEXT DEFAULT 'brak'";
    public static final int URLPICTURE_COLUMN = 3;

    public static final String KEY_COMPANY = "company";
    public static final String COMPANY_OPTIONS = "TEXT DEFAULT 'brak'";
    public static final int COMPANY_COLUMN = 4;

    private static final String DB_CREATE_PRELEGENCI_TABLE = "CREATE TABLE " + DB_PRELEGENCI_TABLE + "(" +
            KEY_ID + " " + ID_OPTIONS + ", " +
            KEY_NAME + " " + NAME_OPTIONS + ", " +
            KEY_INFO + " " + INFO_OPTIONS + ", " +
            KEY_URLPICTURE + " " + URLPICTURE_OPTIONS + "," +
            KEY_COMPANY + " " + COMPANY_OPTIONS + ");";
    private static final String DROP_PRELEGENCI_TABLE = "DROP TABLE IF EXISTS " + DB_PRELEGENCI_TABLE;
    private SQLiteDatabase db;
    private Context mCONTEXT;

    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_PRELEGENCI_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_PRELEGENCI_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_PRELEGENCI_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_PRELEGENCI_TABLE + " updated from ver." + oldVersion +
                    " " +
                    "to " +
                    "ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public PrelegenciDbAdapter(Context context)
    {
        mCONTEXT = context;
    }

    public PrelegenciDbAdapter open(int aDB_VERSION) {
        DB_VERSION = aDB_VERSION;
        dbHelper = new DatabaseHelper(mCONTEXT, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close()
    {
        db.close();
    }

    public long insertPrelegenci(PrelegenciTask task) {
        ContentValues newProgramValues = new ContentValues();
        newProgramValues.put(KEY_ID, task.getmID());
        newProgramValues.put(KEY_NAME, task.getmNAME());
        newProgramValues.put(KEY_INFO, task.getmINFO());
        newProgramValues.put(KEY_URLPICTURE, task.getmURLPICTURE());
        newProgramValues.put(KEY_COMPANY, task.getmCOMPANY());
        return db.insert(DB_PRELEGENCI_TABLE, null, newProgramValues);
    }
    public Cursor getALLPrelegenci() {
        String[] columns = {KEY_ID, KEY_NAME, KEY_INFO, KEY_URLPICTURE, KEY_COMPANY};
        return db.query(DB_PRELEGENCI_TABLE, columns, null, null, null, null, null);
    }
    public PrelegenciTask getPrelegenci(long id) {
        String[] columns = {KEY_ID, KEY_NAME, KEY_INFO, KEY_URLPICTURE, KEY_COMPANY};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_PRELEGENCI_TABLE, columns, where, null, null, null, null);
        PrelegenciTask task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new PrelegenciTask(cursor.getInt(ID_COLUMN), cursor.getString(NAME_COLUMN), cursor
                    .getString
                    (INFO_COLUMN), cursor.getString(URLPICTURE_COLUMN), cursor.getString(COMPANY_COLUMN));
        }
        return task;
    }
    public void test() {
        Cursor cursor = getALLPrelegenci();
        if (cursor != null && cursor.moveToFirst()) {
            PrelegenciTask task = null;
            for (int i = 0; !cursor.isNull(i); i++) {
                task = new PrelegenciTask(cursor.getInt(ID_COLUMN), cursor.getString(NAME_COLUMN), cursor
                        .getString(INFO_COLUMN), cursor.getString(URLPICTURE_COLUMN), cursor
                        .getString(COMPANY_COLUMN));
                Log.i(DEBUG_TAG, "Test:" + task.toString());
                if(!cursor.isLast())
                    cursor.moveToNext();
                else
                    break;
            }
        }
    }

}
