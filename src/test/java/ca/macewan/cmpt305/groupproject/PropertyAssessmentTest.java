package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentTest {
    private PropertyAssessment assessment1;
    private PropertyAssessment assessment2;
    private PropertyAssessment assessment3;
    private PropertyAssessment assessment4;
    private PropertyAssessment assessment5;
    private final String[] classNames1 = {"res", "com", "farm"};
    private final String[] classNames2 = {"res", "com", null};
    private final String[] classNames3 = {"res", null, null};
    private final String[] classPercents1 = {"50", "25", "25"};
    private final String[] classPercents2 = {"75", "25", null};
    private final String[] classPercents3 = {"100", null, null};

    private final Location location = new Location("123", "123");
    private final Address address1 = new Address("123", "123", "123");
    private final Address address2 = new Address("123", "123", null);
    private final AssessmentClass aC1 = new AssessmentClass(40000, classNames1, classPercents1);
    private final AssessmentClass aC2 = new AssessmentClass(40000, classNames2, classPercents2);
    private final AssessmentClass aC3 = new AssessmentClass(40000, classNames3, classPercents3);
    private final Neighbourhood neighbourhood = new Neighbourhood("T ward", "OLIVER", "4");

    /**
     * Sets up test objects before each test is run.
     * Initializes multiple PropertyAssessment objects for use in the tests.
     */
    @BeforeEach
    void setUp() {
        assessment1 = new PropertyAssessment(1, location, address1, aC1, neighbourhood,"Y");
        assessment2 = new PropertyAssessment(1, location, address1, aC1, neighbourhood, "Y");
        assessment3 = new PropertyAssessment(2, location, address2, aC2, neighbourhood,"Y");
        assessment4 = new PropertyAssessment(1, location, address1, aC1, neighbourhood, "Y");
        assessment5 = new PropertyAssessment(3, location, address2, aC3, neighbourhood, "N");
    }

    /**
     * Tests {@link PropertyAssessment#getId()}.
     * Confirms that the correct ID is returned for the PropertyAssessment.
     */
    @Test
    void getId() {
        assertEquals(1, assessment1.getId());
    }

    /**
     * Tests {@link PropertyAssessment#getAddress()}.
     * Confirms that the correct address is returned for the PropertyAssessment.
     */
    @Test
    void getAddress() {
        assertEquals(address1, assessment1.getAddress());
    }

    /**
     * Tests {@link PropertyAssessment#getLocation()}.
     * Confirms that the correct location is returned for the PropertyAssessment.
     */
    @Test
    void getLocation() {
        assertEquals(location, assessment1.getLocation());
    }

    /**
     * Tests {@link PropertyAssessment#getAssessmentClass()}.
     * Confirms that the correct assessment class is returned for the PropertyAssessment.
     */
    @Test
    void getAssessmentClass() {
        assertEquals(aC1, assessment1.getAssessmentClass());
        assertEquals(aC2, assessment3.getAssessmentClass());
        assertEquals(aC3, assessment5.getAssessmentClass());
    }

    /**
     * Tests {@link PropertyAssessment#getNeighbourhood()}.
     * Confirms that the correct neighbourhood is returned for the PropertyAssessment.
     */
    @Test
    void getNeighbourhood() {
        assertEquals(neighbourhood, assessment1.getNeighbourhood());
    }

    /**
     * Tests {@link PropertyAssessment#getGarage()}.
     * Confirms that the correct garage status ("Y" or "N") is returned for the PropertyAssessment.
     */
    @Test
    void getGarage() {
        assertEquals("Y", assessment1.getGarage());
        assertNotEquals("N", assessment1.getGarage());
        assertEquals("N", assessment5.getGarage());
    }

    /**
     * Tests {@link PropertyAssessment#toString()}.
     * Validates the string representation of the PropertyAssessment object.
     * Verifies the format: "Account number = <id> \n<address>\n<assessment class>\n<neighbourhood>\n<location>".
     */
    @Test
    void testToString() {
        String expected1 = String.format("Account number = %d \n%s\n%s\n%s\n%s\n", 1, address1, aC1, neighbourhood, location);
        String actual1 = assessment1.toString();
        assertEquals(expected1, actual1);

        String expected2 = String.format("Account number = %d \n%s\n%s\n%s\n%s\n", 2, address2, aC2, neighbourhood, location);
        String actual2 = assessment3.toString();
        assertEquals(expected2, actual2);

        String expected3 = String.format("Account number = %d \n%s\n%s\n%s\n%s\n", 3, address2, aC3, neighbourhood, location);
        String actual3 = assessment5.toString();
        assertEquals(expected3, actual3);
    }

    /**
     * Tests the {@link PropertyAssessment#equals(Object)} method.
     * Validates reflexivity, symmetry, transitivity, and comparisons to null and different objects.
     */
    @Test
    void testEquals() {
        // Reflexive
        assertEquals(assessment1, assessment1);

        // Symmetric
        assertEquals(assessment1, assessment2);
        assertEquals(assessment2, assessment1);

        // Transitive
        assertEquals(assessment1, assessment2);
        assertEquals(assessment2, assessment4);
        assertEquals(assessment1, assessment4);

        // Null comparison
        assertNotEquals(null, assessment1);

        // Different objects
        assertNotEquals(assessment1, assessment3);

        // Different class comparison
        assertNotEquals(new Object(), assessment1);
    }

    /**
     * Tests the {@link PropertyAssessment#hashCode()} method.
     * Confirms that equal PropertyAssessment objects have the same hash code and unequal objects have different hash codes.
     */
    @Test
    void testHashCode() {
        assertEquals(assessment1.hashCode(), assessment2.hashCode());
        assertNotEquals(assessment1.hashCode(), assessment3.hashCode());
    }

    /**
     * Tests the {@link PropertyAssessment#compareTo(PropertyAssessment)} method.
     * Verifies that PropertyAssessment objects are correctly compared based on their ID.
     * Returns a negative value when comparing smaller IDs, a positive value for larger IDs, and 0 for equal IDs.
     */
    @Test
    void compareTo() {
        assertTrue(assessment1.compareTo(assessment3) < 0);
        assertTrue(assessment3.compareTo(assessment1) > 0);
        assertEquals(0, assessment1.compareTo(assessment2));
    }
}