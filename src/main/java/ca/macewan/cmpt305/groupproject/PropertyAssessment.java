package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    private int id;
    private Location location;
    private Address address;
    private AssessmentClass assessmentClass;
    private Neighbourhood neighbourhood;
    private String garage;

    /**
     * Constructs a PropertyAssessment object with all necessary attributes.
     *
     * @param id               Unique property identifier
     * @param location         Geographic location of the property
     * @param address          Property's physical address
     * @param assessmentClass  Classification of the property
     * @param neighbourhood    Neighbourhood where the property is located
     * @param garage           Description of the garage ("Attached", "Detached", or "None")
     */
    public PropertyAssessment(int id, Location location, Address address, AssessmentClass assessmentClass, Neighbourhood neighbourhood, String garage) {
        this.id = id;
        this.location = location;
        this.address = address;
        this.assessmentClass = assessmentClass;
        this.neighbourhood = neighbourhood;
        this.garage = garage;
    }

    /**
     * Returns the unique ID of the property.
     *
     * @return Property ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the address of the property.
     *
     * @return Property address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the geographic location of the property.
     *
     * @return Property location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the assessment class of the property.
     *
     * @return Assessment class
     */
    public AssessmentClass getAssessmentClass() {
        return assessmentClass;
    }

    /**
     * Returns the neighbourhood where the property is located.
     *
     * @return Neighbourhood
     */
    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Returns the type of garage the property has.
     *
     * @return Garage type ("Attached", "Detached", or "None")
     */
    public String getGarage() {
        return garage;
    }

    /**
     * Returns a formatted string representation of the property assessment.
     *
     * @return Formatted string with property details
     */
    @Override
    public String toString() {
        return String.format("Account number = %d \n%s\n%s\n%s\n%s\n", this.id, this.address, this.assessmentClass, this.neighbourhood, this.location);
    }

    /**
     * Compares this PropertyAssessment object to another for equality.
     * Two PropertyAssessment objects are equal if they have the same ID,
     * location, address, assessment class, neighbourhood, and garage type.
     *
     * @param obj The object to compare to
     * @return true if the objects are considered equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) obj;
        return id == that.id &&
                Objects.equals(location, that.location) &&
                Objects.equals(address, that.address) &&
                Objects.equals(assessmentClass, that.assessmentClass) &&
                Objects.equals(neighbourhood, that.neighbourhood)
                && Objects.equals(garage, that.garage);
    }

    /**
     * Generates a hash code for the property assessment based on all fields.
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, location, address, assessmentClass, neighbourhood, garage);
    }

    /**
     * Compares this PropertyAssessment to another based on their IDs.
     * This allows PropertyAssessment objects to be sorted by ID.
     *
     * @param obj Another PropertyAssessment object
     * @return A negative integer, zero, or a positive integer if this
     *         object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(PropertyAssessment obj) {
        return Integer.compare(this.id, obj.id);
    }
}