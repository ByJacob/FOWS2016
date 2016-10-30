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

public class ProgramDbAdapter {
    private static final String DEBUG_TAG = "SqLiteProgramDb";
    private static int DB_VERSION = 1;
    private static final String DB_NAME = "fows2016program_database.db";
    private static final String DB_PROGRAM_TABLE = "fows2016_program";

    //Kolumny
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;

    public static final String KEY_DATE = "date";
    public static final String DATE_OPTIONS = "INTEGER DEFAULT 0";
    public static final int DATE_COLUMN = 1;

    public static final String KEY_STARTH = "startH";
    public static final String STARTH_OPTIONS = "INTEGER DEFAULT 0";
    public static final int STARTH_COLUMN = 2;

    public static final String KEY_STARTM = "startM";
    public static final String STARTM_OPTIONS = "INTEGER DEFAULT 0";
    public static final int STARTM_COLUMN = 3;

    public static final String KEY_ENDH = "endH";
    public static final String ENDH_OPTIONS = "INTEGER DEFAULT 0";
    public static final int ENDH_COLUMN = 4;

    public static final String KEY_ENDM = "endM";
    public static final String ENDM_OPTIONS = "INTEGER DEFAULT 0";
    public static final int ENDM_COLUMN = 5;

    public static final String KEY_THEME = "theme";
    public static final String THEME_OPTIONS = "TEXT DEFAULT 'brak'";
    public static final int THEME_COLUMN = 6;

    public static final String KEY_SPEAKERID = "speakerID";
    public static final String SPEAKERID_OPTIONS = "INTEGER DEFAULT 0";
    public static final int SPEAKERID_COLUMN = 7;

    public static final String KEY_LANG = "lang";
    public static final String LANG_OPTIONS = "TEXT DEFAULT 'pl'";
    public static final int LANG_COLUMN = 8;

    private static final String DB_CREATE_PROGRAM_TABLE = "CREATE TABLE " + DB_PROGRAM_TABLE + "(" +
            KEY_ID + " " + ID_OPTIONS + ", " +
            KEY_DATE + " " + DATE_OPTIONS + ", " +
            KEY_STARTH + " " + STARTH_OPTIONS + ", " +
            KEY_STARTM + " " + STARTM_OPTIONS + ", " +
            KEY_ENDH + " " + ENDH_OPTIONS + ", " +
            KEY_ENDM + " " + ENDM_OPTIONS + ", " +
            KEY_THEME + " " + THEME_OPTIONS + ", " +
            KEY_SPEAKERID + " " + SPEAKERID_OPTIONS + ", " +
            KEY_LANG + " " + LANG_OPTIONS + ");";
    private static final String DROP_PROGRAM_TABLE = "DROP TABLE IF EXISTS " + DB_PROGRAM_TABLE;
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
            db.execSQL(DB_CREATE_PROGRAM_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_PROGRAM_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_PROGRAM_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_PROGRAM_TABLE + " updated from ver." + oldVersion + " " +
                    "to " +
                    "ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public ProgramDbAdapter(Context aContext) {
        mCONTEXT = aContext;
    }

    public ProgramDbAdapter open(int aDB_VERSION) {
        DB_VERSION = aDB_VERSION;
        dbHelper = new DatabaseHelper(mCONTEXT, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertProgram(ProgramTask task) {
        ContentValues newProgramValues = new ContentValues();
        newProgramValues.put(KEY_DATE, task.getmDate());
        newProgramValues.put(KEY_STARTH, task.getmStartH());
        newProgramValues.put(KEY_STARTM, task.getmStartM());
        newProgramValues.put(KEY_ENDH, task.getmEndH());
        newProgramValues.put(KEY_ENDM, task.getmEndM());
        newProgramValues.put(KEY_THEME, task.getmTheme());
        newProgramValues.put(KEY_SPEAKERID, task.getmSpeakerID());
        newProgramValues.put(KEY_LANG, task.getmLang());
        return db.insert(DB_PROGRAM_TABLE, null, newProgramValues);
    }

    public Cursor getALLProgram() {
        String[] columns = {KEY_ID, KEY_DATE, KEY_STARTH, KEY_STARTM, KEY_ENDH, KEY_ENDM,
                KEY_THEME, KEY_SPEAKERID, KEY_LANG};
        return db.query(DB_PROGRAM_TABLE, columns, null, null, null, null, null);
    }

    public ProgramTask getProgram(long id) {
        String[] columns = {KEY_ID, KEY_DATE, KEY_STARTH, KEY_STARTM, KEY_ENDH, KEY_ENDM,
                KEY_THEME, KEY_SPEAKERID, KEY_LANG};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_PROGRAM_TABLE, columns, where, null, null, null, null);
        ProgramTask task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new ProgramTask(id, cursor.getInt(DATE_COLUMN), cursor.getInt(STARTH_COLUMN),
                    cursor.getInt(STARTM_COLUMN), cursor.getInt(ENDH_COLUMN), cursor.getInt
                    (ENDM_COLUMN), cursor.getString(THEME_COLUMN), cursor.getInt
                    (SPEAKERID_COLUMN), cursor.getString(LANG_COLUMN));
        }
        return task;
    }

    public void test() {
        Cursor cursor = getALLProgram();
        if (cursor != null && cursor.moveToFirst()) {
            ProgramTask task = null;
            for (int i = 0; !cursor.isNull(i); i++) {
                task = new ProgramTask(cursor.getInt(ID_COLUMN), cursor.getInt(DATE_COLUMN), cursor.getInt
                        (STARTH_COLUMN), cursor.getInt(STARTM_COLUMN), cursor.getInt(ENDH_COLUMN), cursor.getInt
                        (ENDM_COLUMN), cursor.getString(THEME_COLUMN), cursor.getInt
                        (SPEAKERID_COLUMN), cursor.getString(LANG_COLUMN));
                Log.i(DEBUG_TAG, "Test:" + task.toString());
                if(!cursor.isLast())
                    cursor.moveToNext();
                else
                    break;
            }
        }
    }

}
