package ca.macewan.cmpt305.groupproject;

public class School implements Comparable<School>{
    private String id;
    private SchoolName name;
    private Address address;
    private SchoolType schoolType;
    private Catchment catchment;
    private Location location;

    public School(String id, String year, SchoolName name, SchoolType schoolType, Address address, Catchment catchment, Location location) {
        this.id = id;
        this.name = name;
        this.schoolType = schoolType;
        this.address = address;
        this.catchment = catchment;
        this.location = location;
    }

    // get and set methods
    public String getId() {
        return id;
    }
    public SchoolName getName() {
        return name;
    }
    public Address getAddress() {
        return address;
    }
    public SchoolType getSchoolType() {
        return schoolType;
    }
    public Catchment getCatchment() {
        return catchment;
    }
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        // To make clean string make a newline variable that is a line separator
        String newline = System.lineSeparator();

        return String.format("Name: %s" + newline + "Address: %s", name, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof School){
            return this.id.equals(((School)obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public int compareTo(School o) {
        return this.id.compareTo(o.id);
    }


}
