package ca.macewan.cmpt305.groupproject;
import java.util.Objects;

public class School implements Comparable<School>{
    private String id;
    private SchoolName name;
    private Address address;
    private SchoolType schoolType;

    public School(String id, String year, SchoolName name, SchoolType schoolType, Address address) {
        this.id = id;
        this.name = name;
        this.schoolType = schoolType;
        this.address = address;
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
    public int compareTo(School o) {
        return this.id.compareTo(o.id);
    }


}
