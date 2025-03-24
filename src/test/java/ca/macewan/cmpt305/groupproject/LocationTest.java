package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    private Location location1;
    private Location location2;
    private Location location3;
    private Location location4;

    /**
     * Sets up test objects before each test is run.
     */
    @BeforeEach
    void setUp() {
        location1 = new Location("123", "123");
        location2 = new Location("123", "123");
        location3 = new Location("123", "123");
        location4 = new Location("456", "456");
    }

    /**
     * Tests {@link Location#getLatitude()}.
     * Confirms the correct latitude value is returned.
     */
    @Test
    void getLatitude() {
        assertEquals("123", location1.getLatitude());
        assertEquals("456", location4.getLatitude());
    }

    /**
     * Tests {@link Location#setLatitude(String)}.
     * Confirms the latitude value can be set correctly.
     */
    @Test
    void setLatitude() {
        location2.setLatitude("345");
        assertEquals("345", location2.getLatitude());
    }

    /**
     * Tests {@link Location#getLongitude()}.
     * Confirms the correct longitude value is returned.
     */
    @Test
    void getLongitude() {
        assertEquals("123", location1.getLongitude());
        assertEquals("456", location4.getLongitude());
    }

    /**
     * Tests {@link Location#setLongitude(String)}.
     * Confirms the longitude value can be set correctly.
     */
    @Test
    void setLongitude() {
        location3.setLongitude("345");
        assertEquals("345", location3.getLongitude());
    }

    /**
     * Tests {@link Location#toString()}.
     * Validates the string representation of the Location object.
     */
    @Test
    void testToString() {
        String expected = "Location = (123, 123)";
        assertEquals(expected, location1.toString());
    }

    /**
     * Tests the {@link Location#equals(Object)} method.
     * Validates reflexivity, symmetry, transitivity, and comparisons to null and different objects.
     */
    @Test
    void testEquals() {
        // Reflexive
        assertEquals(location1, location1);

        // Symmetric
        assertEquals(location1, location2 );
        assertEquals(location2, location3 );

        // Transitive
        assertEquals(location1, location2);
        assertEquals(location2, location3);
        assertEquals(location1, location3);

        // Null comparison
        assertNotEquals(null, location1);

        // Different objects
        assertNotEquals(location1, location4);

        // Different class comparison
        assertNotEquals(new Object(), location1);
    }

    /**
     * Tests the {@link Location#hashCode()} method.
     * Confirms that equal Location objects have the same hash code and unequal objects have different hash codes.
     */
    @Test
    void testHashCode() {
        assertEquals(location1.hashCode(), location2.hashCode());
        assertNotEquals(location1.hashCode(), location4.hashCode());
    }
}