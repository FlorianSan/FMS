/*
 * Class LatitudeLongitude, coordinates of points
 */
package parser;

/**
 * Class LatitudeLongitude, coordinates of points
 *
 * @author yoann
 */
public class LatitudeLongitude {

    /**
     * Latitude
     */
    private String latitude;
    /**
     * Longitude
     */
    private String longitude;

    /**
     * Latitude Constructor
     *
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public LatitudeLongitude(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get Latitude
     *
     * @return Latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * Get Longitude
     *
     * @return Longitude
     */
    public String getLongitude() {
        return this.longitude;

    }
}
