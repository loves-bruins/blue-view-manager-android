package furtiveops.com.blueviewmanager.utils;

/**
 * Created by lorenrogers on 1/27/17.
 */

public interface DBSchema {

    String DB_NAME = "preferences.db";

    String TBL_SETTINGS = "settings";

    // BE AWARE: Normally you would store the LOOKUP_KEY
    // of a contact from the device. But this would
    // have needless complicated the sample. Thus I
    // omitted it.
    String DDL_CREATE_TBL_SETTINGS =
            "CREATE TABLE IF NOT EXISTS settings ( \n" +
                    "_id INTEGER  PRIMARY KEY AUTOINCREMENT, \n" +
                    "user_name TEXT NOT NULL, \n" +
                    "password TEXT NOT NULL)";

    String DDL_DROP_TBL_SETTINGS =
            "DROP TABLE IF EXISTS settings";
}