package furtiveops.com.blueviewmanager.contentProviders;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import furtiveops.com.blueviewmanager.BuildConfig;

/**
 * Created by lorenrogers on 1/27/17.
 */

public class SettingsContract {
    /**
     * The authority of the Purchases provider.
     */
    public static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider.Settings";
    /**
     * The content URI for the top-level
     * Purchases authority.
     */
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);


    /**
     * Constants for the Purchases table
     * of the Purchases provider.
     */
    public static final class Settings implements BaseColumns {
        // This class cannot be instantiated
        private Settings() {
        }

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(
                        SettingsContract.BASE_CONTENT_URI,
                        "settings");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.blueviewmanager.settings";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/vnd.com.blueviewmanager.settings";


        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "_id DESC";

        /**
         * The password
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_USERNAME = "user_name";

        /**
         * The password
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_PASSWORD = "password";

        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String[] PROJECTION_ALL =
                {_ID,
                        COLUMN_NAME_USERNAME,
                        COLUMN_NAME_PASSWORD
                };
    }
}
