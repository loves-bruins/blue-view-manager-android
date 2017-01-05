package furtiveops.com.blueviewmanager.models;

/**
 * Created by lorenrogers on 1/5/17.
 */

public class CycleTest {

    String uid;
    String date;
    Double ammonia;
    Double nitrate;
    Double nitrite;
    String notes;

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
}
