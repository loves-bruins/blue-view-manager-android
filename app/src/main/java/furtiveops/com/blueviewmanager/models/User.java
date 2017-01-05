package furtiveops.com.blueviewmanager.models;

/**
 * Created by lorenrogers on 11/1/16.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    protected String uid;
    protected String userName;
    protected String role;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(final String uid, final String userName, final String role) {
        this.uid = uid;
        this.userName = userName;
        this.role  = role;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
