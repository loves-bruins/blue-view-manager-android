package furtiveops.com.blueviewmanager.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lorenrogers on 1/27/17.
 */

public class ContentProvidersDbOpenHelper extends SQLiteOpenHelper
{

    private static final String NAME = DBSchema.DB_NAME;
    private static final int VERSION = 1;

    public ContentProvidersDbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.DDL_CREATE_TBL_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this is no sample for how to handle SQLite databases
        // thus I simply drop and recreate the database here.
        //
        // NEVER do this in real apps. Your users wouldn't like
        // loosing data just because you decided to change the schema
        db.execSQL(DBSchema.DDL_DROP_TBL_SETTINGS);
        onCreate(db);
    }
}
