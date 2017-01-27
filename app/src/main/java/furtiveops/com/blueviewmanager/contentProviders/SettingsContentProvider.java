package furtiveops.com.blueviewmanager.contentProviders;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import furtiveops.com.blueviewmanager.utils.ContentProvidersDbOpenHelper;
import furtiveops.com.blueviewmanager.utils.DBSchema;

/**
 * Created by lorenrogers on 1/27/17.
 */

public class SettingsContentProvider extends ContentProvider
{
    // helper constants for use with the UriMatcher
    private static final int SETTINGS_LIST = 1;
    private static final int SETTINGS_ID = 2;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();
    private static final UriMatcher URI_MATCHER;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(SettingsContract.AUTHORITY,
                "settings",
                SETTINGS_LIST);
        URI_MATCHER.addURI(SettingsContract.AUTHORITY,
                "settings/#",
                SETTINGS_ID);
    }
    private ContentProvidersDbOpenHelper helper;


    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = helper.getWritableDatabase();
        mIsInBatchMode.set(true);
        // the next line works because SQLiteDatabase
        // uses a thread local SQLiteSession object for
        // all manipulations
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(SettingsContract.Settings.CONTENT_URI, null);
            return retResult;
        }
        finally {
            mIsInBatchMode.remove();
            db.endTransaction();
        }
    }

    private boolean isInBatchMode()
    {
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        helper = new ContentProvidersDbOpenHelper(context);
        return (helper == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        if (URI_MATCHER.match(uri) != SETTINGS_LIST)
        {
            throw new IllegalArgumentException(
                    "Unsupported URI for insertion: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Uri returnUri = null;
        if (URI_MATCHER.match(uri) == SETTINGS_LIST) {
            long id =
                    db.insert(
                            DBSchema.TBL_SETTINGS,
                            null,
                            values);
            returnUri = getUriForId(id, uri);
        }

        return returnUri;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                // notify all listeners of changes:
                getContext().
                        getContentResolver().
                        notifyChange(itemUri, null);
            }
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException(
                "Problem while inserting into uri: " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        boolean useAuthorityUri = false;
        switch (URI_MATCHER.match(uri)) {
            case SETTINGS_LIST:
                builder.setTables(DBSchema.TBL_SETTINGS);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = SettingsContract.Settings.DEFAULT_SORT_ORDER;
                }
                break;
            case SETTINGS_ID:
                builder.setTables(DBSchema.TBL_SETTINGS);
                // limit query to one row at most:
                builder.appendWhere(SettingsContract.Settings._ID + " = " +
                        uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
        Log.i(getClass().getSimpleName(), "Selection = " + selection);
        for(int i = 0; i < selectionArgs.length; i++)
        {
            Log.i(getClass().getSimpleName(), "SelectionArgs = " + selectionArgs[i]);
        }

        Cursor cursor =
                builder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
        // if we want to be notified of any changes:
        if (useAuthorityUri) {
            cursor.setNotificationUri(
                    getContext().getContentResolver(),
                    SettingsContract.BASE_CONTENT_URI);
        }
        else {
            cursor.setNotificationUri(
                    getContext().getContentResolver(),
                    uri);
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case SETTINGS_LIST:
                delCount = db.delete(
                        DBSchema.TBL_SETTINGS,
                        selection,
                        selectionArgs);
                break;
            case SETTINGS_ID:
                String idStr = uri.getLastPathSegment();
                String where = SettingsContract.Settings._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = db.delete(
                        DBSchema.TBL_SETTINGS,
                        where,
                        selectionArgs);
                break;
            default:
                // no support for deleting photos or entities –
                // photos are deleted by a trigger when the item is deleted
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // notify all listeners of changes:
        if (delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case SETTINGS_LIST:
                updateCount = db.update(
                        DBSchema.TBL_SETTINGS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SETTINGS_ID:
                String idStr = uri.getLastPathSegment();
                String where = SettingsContract.Settings._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(
                        DBSchema.TBL_SETTINGS,
                        values,
                        where,
                        selectionArgs);
                break;
            default:
                // no support for updating photos or entities!
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // notify all listeners of changes:
        if (updateCount > 0 && !isInBatchMode())
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (URI_MATCHER.match(uri)) {
            case SETTINGS_LIST:
                return SettingsContract.Settings.CONTENT_TYPE;
            case SETTINGS_ID:
                return SettingsContract.Settings.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }
}
