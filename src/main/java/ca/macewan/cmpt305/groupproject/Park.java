package ca.macewan.cmpt305.groupproject;

public class Park implements Comparable<Park>{
    private String id;
    private String officialName;
    private String commonName;
    private Address address;
    private Location location;

    public Park(String id, String officialName, String commonName, Address address, Location location) {
        this.id = id;
        this.officialName = officialName;
        this.commonName = commonName;
        this.address = address;
        this.location = location;
    }

    public String getId() {
        return id;
    }
    public String getOfficialName() {
        return officialName;
    }
    public String getCommonName() {
        return commonName;
    }
    public Address getAddress() {
        return address;
    }
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        if (officialName != null) {
            return String.format("Name: %s" + officialName, address);

        }
        return String.format("Name: %s" + commonName, address);
    }

    @Override
    public int compareTo(Park o) {
        return id.compareTo(o.id);
    }

}
