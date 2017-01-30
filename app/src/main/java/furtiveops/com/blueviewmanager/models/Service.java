package furtiveops.com.blueviewmanager.models;

/**
 * Created by lorenrogers on 1/5/17.
 */

public class Service {
    String service_id;
    Double price;
    String title;

    String uid;
    Long date;
    String notes;

    public Service() {

    }

    public Service (String service_id, Double price, String title, String uid, Long date, String notes) {
        this.service_id = service_id;
        this.price = price;
        this.title = title;
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

    public String getTitle() {
        return title;
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
}
