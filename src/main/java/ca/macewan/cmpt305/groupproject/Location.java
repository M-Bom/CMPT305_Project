package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class Location {
    private String latitude;
    private String longitude;

    /**
     * Constructs a Location object with the specified latitude and longitude.
     *
     * @param latitude  the latitude coordinate of the location.
     * @param longitude the longitude coordinate of the location.
     */
    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude of the location.
     *
     * @return the latitude coordinate as a String.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the location.
     *
     * @param latitude the new latitude coordinate.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the location.
     *
     * @return the longitude coordinate as a String.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param longitude the new longitude coordinate.
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns a string representation of the location in the format:
     * Location = (latitude, longitude).
     *
     * @return a string representation of the location.
     */
    @Override
    public String toString() {
        return "Location = (" + this.latitude + ", " + this.longitude + ")";
    }

    /**
     * Compares this location to the specified object for equality.
     * Two locations are considered equal if they have the same latitude and longitude.
     *
     * @param o the object to compare to.
     * @return true if the specified object is equal to this location, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(latitude, location.latitude) && Objects.equals(longitude, location.longitude);
    }

    /**
     * Returns a hash code value for this location based on its latitude and longitude.
     *
     * @return a hash code value for this location.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }
}