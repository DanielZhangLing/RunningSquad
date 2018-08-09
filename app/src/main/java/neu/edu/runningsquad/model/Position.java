package neu.edu.runningsquad.model;

public class Position {
    public Float latitude;
    public Float longitude;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Position(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
