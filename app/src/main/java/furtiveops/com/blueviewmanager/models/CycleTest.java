package furtiveops.com.blueviewmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorenrogers on 1/5/17.
 */

public class CycleTest implements Parcelable {

    protected String uid;
    protected String date;
    protected Double ammonia;
    protected Double nitrate;
    protected Double nitrite;
    protected String notes;

    public CycleTest() {

    }

    public CycleTest(final String uid, final String date, final Double ammonia, final Double nitrate, Double nitrite, final String notes) {
        this.uid = uid;
        this.date = date;
        this.ammonia = ammonia;
        this.nitrate = nitrate;
        this.nitrite = nitrite;
        this.notes = notes;
    }

    public CycleTest(Parcel in) {
        uid = in.readString();
        date = in.readString();
        ammonia = in.readDouble();
        nitrate = in.readDouble();
        nitrite = in.readDouble();
        notes = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public Double getAmmonia() {
        return ammonia;
    }

    public Double getNitrate() {
        return nitrate;
    }

    public Double getNitrite() {
        return nitrite;
    }

    public String getNotes() {
        return notes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ammonia", ammonia);
        result.put("date", date);
        result.put("nitrate", nitrate);
        result.put("nitrite", nitrite);
        result.put("notes", notes);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeDouble(ammonia);
        dest.writeDouble(nitrate);
        dest.writeDouble(nitrite);
        dest.writeString(notes);
    }

    public static final Parcelable.Creator<CycleTest> CREATOR = new Parcelable.Creator<CycleTest>() {
        @Override
        public CycleTest createFromParcel(final Parcel in) {
            return new CycleTest(in);
        }

        @Override
        public CycleTest[] newArray(final int size) {
            return new CycleTest[size];
        }
    };
}
