package furtiveops.com.blueviewmanager.models;

/**
 * Created by lorenrogers on 1/5/17.
 */

public class CycleTest {

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
}
