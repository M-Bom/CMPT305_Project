package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class Neighbourhood {
    private String ward;
    private String neighbourhoodName;
    private String neighbourhoodId;

    /**
     * Constructs a Neighbourhood object with the given ward, name, and ID.
     *
     * @param ward              The ward that the neighbourhood is part of.
     * @param neighbourhoodName The name of the neighbourhood.
     * @param neighbourhoodId   A unique identifier for the neighbourhood.
     */
    public Neighbourhood(String ward, String neighbourhoodName, String neighbourhoodId) {
        this.ward = ward;
        this.neighbourhoodName = neighbourhoodName;
        this.neighbourhoodId = neighbourhoodId;
    }

    /**
     * Returns the ward the neighbourhood belongs to.
     *
     * @return The ward name.
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the name of the neighbourhood.
     *
     * @return The neighbourhood name.
     */
    public String getNeighbourhoodName() {
        return neighbourhoodName;
    }

    /**
     * Returns the unique identifier of the neighbourhood.
     *
     * @return The neighbourhood ID.
     */
    public String getNeighbourhoodId() {
        return neighbourhoodId;
    }

    /**
     * Returns a string representation of the neighbourhood.
     * This includes the neighbourhood name and the ward it belongs to.
     *
     * @return Formatted string representation.
     */
    @Override
    public String toString() {
        return "Neighbourhood = " + this.neighbourhoodName + " (" + this.ward + ")";
    }

    /**
     * Checks if this Neighbourhood object is equal to another object.
     * Two Neighbourhood objects are considered equal if they have the same
     * ward, name, and ID.
     *
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Neighbourhood that = (Neighbourhood) obj;
        return Objects.equals(ward, that.ward) &&
                Objects.equals(neighbourhoodName, that.neighbourhoodName) &&
                Objects.equals(neighbourhoodId, that.neighbourhoodId);
    }

    /**
     * Generates a hash code for this Neighbourhood object.
     * The hash code is based on the ward, name, and ID.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(ward, neighbourhoodName, neighbourhoodId);
    }
}