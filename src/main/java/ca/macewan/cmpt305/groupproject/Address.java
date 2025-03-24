package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class Address {
    private String street;
    private String houseNumber;
    private String suiteNumber;

    /**
     * Constructs an Address object with the specified street, house number, and suite number.
     *
     * @param street the street name.
     * @param houseNumber the house number.
     * @param suiteNumber the suite number (can be null or blank if not applicable).
     */
    public Address(String street, String houseNumber, String suiteNumber) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.suiteNumber = suiteNumber;
    }

    /**
     * Returns the street name.
     *
     * @return the street name.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Returns the house number.
     *
     * @return the house number.
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Returns the suite number.
     *
     * @return the suite number, or null if no suite number is present.
     */
    public String getSuiteNumber() {
        return suiteNumber;
    }

    /**
     * Returns a string representation of the address.
     * If no suite number is provided, the address will include only the house number and street.
     * If a suite number is present, it will be included in the output.
     *
     * @return a formatted string representation of the address.
     */
    @Override
    public String toString() {
        if (this.suiteNumber == null || this.suiteNumber.isBlank()) {
            return "Address = " + this.houseNumber + " " + this.street;
        }
        return "Address = " + this.houseNumber + " " + this.street + " Suite " + this.suiteNumber;
    }

    /**
     * Checks whether this address is equal to another object.
     * Two addresses are considered equal if they have the same street name,
     * house number, and suite number.
     *
     * @param o the object to compare to.
     * @return true if the addresses are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(suiteNumber, address.suiteNumber);
    }

    /**
     * Returns a hash code for this address.
     * The hash code is based on the street name, house number, and suite number.
     *
     * @return the hash code for this address.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.houseNumber, this.street, this.suiteNumber);
    }
}

