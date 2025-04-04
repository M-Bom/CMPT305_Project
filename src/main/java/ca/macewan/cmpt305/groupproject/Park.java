package ca.macewan.cmpt305.groupproject;

public class Park implements Comparable<Park>{
    private String id;
    private String officialName;
    private String commonName;
    private Address address;
    private Location location;

    /**
     * Constructs a PropertyAssessment object with all necessary attributes.
     *
     * @param id               Park id
     * @param officialName     official name of Park - some only have this
     * @param commonName       common name of park - while some only have this
     * @param location         Geographic location of the property
     * @param address          school address
     */
    public Park(String id, String officialName, String commonName, Address address, Location location) {
        this.id = id;
        this.officialName = officialName;
        this.commonName = commonName;
        this.address = address;
        this.location = location;
    }

    // get methods for attributes

    /**
     * Method to get id
     * @return id - String
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get official name
     * @return official name - String
     */
    public String getOfficialName() {
        return officialName;
    }

    /**
     * Method to get common name
     * @return commonName - String
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * Method to get address
     * @return address - Address object
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Method to get location
     * @return location - Location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Override toString method for Park object
     * */
    @Override
    public String toString() {
        // If park has an official name (some don't)
        if (officialName != null) {
            return String.format("Name: %s" + officialName, address);

        }
        // else use the common name for the park
        return String.format("Name: %s" + commonName, address);
    }

    /**
     * Override compareTo by ID
     * */
    @Override
    public int compareTo(Park o) {
        return id.compareTo(o.id);
    }

}
