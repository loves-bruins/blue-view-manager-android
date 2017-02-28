package furtiveops.com.blueviewmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorenrogers on 1/5/17.
 */

public class Service implements Parcelable {
    String service_id;
    String uid;
    Long date;
    Double price;
    String notes;

    public Service() {

    }

    public Service (Parcel in) {

    }

    public Service (String service_id, String uid, Long date, Double price, String notes) {
        this.service_id = service_id;
        this.price = price;
        this.uid = uid;
        this.date = date;
        this.notes = notes;
    }

    public String getService_id() {
        return service_id;
    }

    public Double getPrice() {
        return price;
    }

    public String getUid() {
        return uid;
    }

    public Long getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("notes", notes);
        result.put("price", price);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(final Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(final int size) {
            return new Service[size];
        }
    };
}
