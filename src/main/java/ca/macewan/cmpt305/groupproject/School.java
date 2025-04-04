package ca.macewan.cmpt305.groupproject;

public class School implements Comparable<School>{
    private String id;
    private SchoolName name;
    private String year;
    private Address address;
    private SchoolType schoolType;
    private Catchment catchment;
    private Location location;

    /**
     * Constructs a PropertyAssessment object with all necessary attributes.
     *
     * @param id               School id
     * @param year             School year
     * @param name             school name
     * @param schoolType       school type (type, grade)
     * @param location         Geographic location of the property
     * @param address          school address
     * @param catchment        school catchment polygon
     */
    public School(String id, String year, SchoolName name, SchoolType schoolType, Address address, Catchment catchment, Location location) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.schoolType = schoolType;
        this.address = address;
        this.catchment = catchment;
        this.location = location;
    }

    // get methods for each attribute in School

    /**
     * Method to get id
     * @return id - string
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get name
     * @return name - string
     */
    public SchoolName getName() {
        return name;
    }

    /**
     * Method to get address
     * @return address - Address object
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Method to get schoolType
     * @return schoolType - SchoolType object
     */
    public SchoolType getSchoolType() {
        return schoolType;
    }

    /**
     * Method to get catchment
     * @return catchment - Catchment object
     */
    public Catchment getCatchment() {
        return catchment;
    }

    /**
     * Method to get location
     * @return location - Location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * toString method for School object
     * */
    @Override
    public String toString() {
        // To make clean string make a newline variable that is a line separator
        String newline = System.lineSeparator();

        return String.format("Name: %s" + newline + "Address: %s", name, address);
    }

    /**
     * Override equals method to compare by id
     * */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof School){
            return this.id.equals(((School)obj).id);
        }
        return false;
    }

    /**
     * Override hashcode method to make hash the id
     * */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Override compareTo by year
     * */
    @Override
    public int compareTo(School o) {
        return this.year.compareTo(o.year);
    }


}
