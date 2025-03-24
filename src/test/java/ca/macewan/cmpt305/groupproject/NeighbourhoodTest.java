package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeighbourhoodTest {
    private Neighbourhood neighbourhood1;
    private Neighbourhood neighbourhood2;
    private Neighbourhood neighbourhood3;
    private Neighbourhood neighbourhood4;
    private final String ward1 = "ward1";
    private final String ward2 = "ward2";
    private final String neighbourhoodName = "neighbourhood1";

    /**
     * Sets up test objects before each test is run.
     * Initializes multiple Neighbourhood objects for use in the tests.
     */
    @BeforeEach
    void setUp() {
        neighbourhood1 = new Neighbourhood(ward1, neighbourhoodName, "1");
        neighbourhood2 = new Neighbourhood(ward1, neighbourhoodName, "1");
        neighbourhood3 = new Neighbourhood(ward1, neighbourhoodName, "1");
        neighbourhood4 = new Neighbourhood(ward2, neighbourhoodName, "1");
    }

    /**
     * Tests {@link Neighbourhood#getWard()}.
     * Confirms that the correct ward is returned for the Neighbourhood.
     */
    @Test
    void getWard() {
        assertEquals(ward1, neighbourhood1.getWard());
        assertEquals(ward2, neighbourhood4.getWard());
    }

    /**
     * Tests {@link Neighbourhood#getNeighbourhoodName()}.
     * Confirms that the correct neighbourhood name is returned.
     */
    @Test
    void getNeighbourhoodName() {
        assertEquals(neighbourhoodName, neighbourhood1.getNeighbourhoodName());
    }

    /**
     * Tests {@link Neighbourhood#getNeighbourhoodId()}.
     * Confirms that the correct neighbourhood ID is returned.
     */
    @Test
    void getNeighbourhoodId() {
        assertEquals("1", neighbourhood1.getNeighbourhoodId());
    }

    /**
     * Tests {@link Neighbourhood#toString()}.
     * Validates the string representation of the Neighbourhood object.
     * Verifies the format: "Neighbourhood = <name> (<ward>)".
     */
    @Test
    void testToString() {
        String expected1 = "Neighbourhood = " + neighbourhoodName + " (" + ward1 + ")";
        String actual1 = neighbourhood1.toString();
        assertEquals(expected1, actual1);

        String expected2 = "Neighbourhood = " + neighbourhoodName + " (" + ward2 + ")";
        String actual2 = neighbourhood4.toString();
        assertEquals(expected2, actual2);
    }

    /**
     * Tests the {@link Neighbourhood#equals(Object)} method.
     * Validates reflexivity, symmetry, transitivity, and comparisons to null and different objects.
     */
    @Test
    void testEquals() {
        // Reflexive
        assertEquals(neighbourhood1, neighbourhood1 );

        // Symmetric
        assertEquals(neighbourhood1, neighbourhood2 );
        assertEquals(neighbourhood2, neighbourhood3 );

        // Transitive
        assertEquals(neighbourhood1, neighbourhood2);
        assertEquals(neighbourhood2, neighbourhood3);
        assertEquals(neighbourhood1, neighbourhood3);

        // Null comparison
        assertNotEquals(null, neighbourhood1);

        // Different objects
        assertNotEquals(neighbourhood1, neighbourhood4);

        // Different class comparison
        assertNotEquals(new Object(), neighbourhood1);
    }

    /**
     * Tests the {@link Neighbourhood#hashCode()} method.
     * Confirms that equal Neighbourhood objects have the same hash code and unequal objects have different hash codes.
     */
    @Test
    void testHashCode() {
        assertEquals(neighbourhood2.hashCode(), neighbourhood1.hashCode());
        assertNotEquals(neighbourhood1.hashCode(), neighbourhood4.hashCode());
    }
}