package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    private Address address1;
    private Address address2;
    private Address address3;
    private Address address4;

    /**
     * Sets up test fixtures before each test method is run.
     * Creates multiple Address objects to be used in tests.
     */
    @BeforeEach
    void setUp() {
        address1 = new Address("123ST", "1", "4");
        address2 = new Address("123ST", "1", "4");
        address3 = new Address("123ST", "1", "4");
        address4 = new Address("456ST", "4", null);
    }

    /**
     * Tests the {@link Address#getStreet()} method.
     * Verifies the street name is correctly returned.
     */
    @Test
    void getStreet() {
        assertEquals("123ST", address1.getStreet());
        assertEquals("456ST", address4.getStreet());
    }

    /**
     * Tests the {@link Address#getHouseNumber()} method.
     * Verifies the house number is correctly returned.
     */
    @Test
    void getHouseNumber() {
        assertEquals("1", address1.getHouseNumber());
        assertEquals("4", address4.getHouseNumber());
    }

    /**
     * Tests the {@link Address#getSuiteNumber()} method.
     * Verifies the suite number is correctly returned, including handling of null values.
     */
    @Test
    void getSuiteNumber() {
        assertEquals("4", address1.getSuiteNumber());
        assertNull(address4.getSuiteNumber());
    }

    /**
     * Tests the {@link Address#toString()} method.
     * Verifies the string representation is correctly formatted, both with and without a suite number.
     */
    @Test
    void testToString() {
        String expected1 = "Address = 1 123ST Suite 4";
        assertEquals(expected1, address1.toString());

        String expected2 = "Address = 4 456ST";
        assertEquals(expected2, address4.toString());
    }

    /**
     * Tests the {@link Address#equals(Object)} method.
     * Verifies reflexivity, symmetry, transitivity, and null/different object comparisons.
     */
    @Test
    void testEquals() {
        // Reflexive
        assertEquals(address1, address1);

        // Symmetric
        assertEquals(address1, address2 );
        assertEquals(address2, address3 );

        // Transitive
        assertEquals(address1, address2);
        assertEquals(address2, address3);
        assertEquals(address1, address3);

        // Null comparison
        assertNotEquals(null, address1);

        // Different objects
        assertNotEquals(address1, address4);

        // Different class comparison
        assertNotEquals(new Object(), address1);
    }

    /**
     * Tests the {@link Address#hashCode()} method.
     * Verifies that equal objects have the same hash code, and different objects have different hash codes.
     */
    @Test
    void testHashCode() {
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1.hashCode(), address4.hashCode());
    }
}