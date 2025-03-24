package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentClassTest {
    private AssessmentClass assessmentClass1;
    private AssessmentClass assessmentClass2;
    private AssessmentClass assessmentClass3;
    private final String[] classNames1 = {"res", "com", "farm"};
    private final String[] classNames2 = {"res", "com", null};
    private final String[] classNames3 = {"res", null, null};
    private final String[] classPercents1 = {"50", "25", "25"};
    private final String[] classPercents2 = {"75", "25", null};
    private final String[] classPercents3 = {"100", null, null};

    /**
     * Sets up test objects before each test is run.
     */
    @BeforeEach
    void setUp() {
        assessmentClass1 = new AssessmentClass(400000, classNames1, classPercents1);
        assessmentClass2 = new AssessmentClass(400000, classNames2, classPercents2);
        assessmentClass3 = new AssessmentClass(400000, classNames3, classPercents3);
    }

    /**
     * Tests {@link AssessmentClass#getAssessedValue()}.
     * Confirms the correct assessed value is returned.
     */
    @Test
    void getAssessedValue() {
        assertEquals(400000, assessmentClass1.getAssessedValue());
    }

    /**
     * Tests {@link AssessmentClass#getClassNames()}.
     * Confirms the correct class names array is returned.
     */
    @Test
    void getClassNames() {
        assertArrayEquals(classNames1, assessmentClass1.getClassNames());
        assertArrayEquals(classNames2, assessmentClass2.getClassNames());
        assertArrayEquals(classNames3, assessmentClass3.getClassNames());
    }

    /**
     * Tests {@link AssessmentClass#getClassPercents()}.
     * Confirms the correct class percents array is returned.
     */
    @Test
    void getClassPercents() {
        assertArrayEquals(classPercents1, assessmentClass1.getClassPercents());
        assertArrayEquals(classPercents2, assessmentClass2.getClassPercents());
        assertArrayEquals(classPercents3, assessmentClass3.getClassPercents());
    }

    /**
     * Tests {@link AssessmentClass#toString()}.
     * Validates the string representation under different class/percent combinations.
     */
    @Test
    void testToString() {
        // Test 1 all elements in class names and percent are not null
        String className1 = classNames1[0];
        String className2 = classNames1[1];
        String className3 = classNames1[2];

        String classPercent1 = classPercents1[0];
        String classPercent2 = classPercents1[1];
        String classPercent3 = classPercents1[2];

        String expected1 = String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%, %s %s%%, %s %s%%]", 400000, className1, classPercent1, className2, classPercent2, className3, classPercent3);
        String actual1 = assessmentClass1.toString();
        assertEquals(expected1, actual1);

        // Test 2 missing 1 element for each percent and class
        String className11 = classNames2[0];
        String className22 = classNames2[1];
        String className33 = classNames2[2];

        String classPercent11 = classPercents2[0];
        String classPercent22 = classPercents2[1];
        String classPercent33 = classPercents2[2];

        assertNull(className33);
        assertNull(classPercent33);
        String expected2 = String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%, %s %s%%]", 400000, className11, classPercent11, className22, classPercent22);
        String actual2 = assessmentClass2.toString();
        assertEquals(expected2, actual2);

        // Test 3 missing 2 elements for each percent and class
        String className111 = classNames3[0];
        String className222 = classNames3[1];
        String className333 = classNames3[2];

        String classPercent111 = classPercents3[0];
        String classPercent222 = classPercents3[1];
        String classPercent333 = classPercents3[2];

        assertNull(className222);
        assertNull(classPercent222);
        assertNull(className333);
        assertNull(classPercent333);
        String expected3 = String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%]", 400000, className111, classPercent111);
        String actual3 = assessmentClass3.toString();
        assertEquals(expected3, actual3);
    }

    /**
     * Tests the {@link AssessmentClass#equals(Object)} method.
     * Validates reflexivity, symmetry, transitivity, and comparisons to null and different objects.
     */
    @Test
    void testEquals() {
        // Reflexive
        assertEquals(assessmentClass1, assessmentClass1 );

        // Symmetric
        AssessmentClass assessmentClass4 = new AssessmentClass(400000, classNames1, classPercents1);
        assertEquals(assessmentClass1, assessmentClass4 );
        assertEquals(assessmentClass4, assessmentClass1);

        // Transitive
        AssessmentClass assessmentClass5 = new AssessmentClass(400000, classNames1, classPercents1);
        assertEquals(assessmentClass1, assessmentClass4);
        assertEquals(assessmentClass4, assessmentClass5);
        assertEquals(assessmentClass1, assessmentClass5);

        // Null comparison
        assertNotEquals(null, assessmentClass1);

        // Different objects
        assertNotEquals(assessmentClass1, assessmentClass2);

        // Different class comparison
        assertNotEquals(new Object(), assessmentClass1);
    }

    /**
     * Tests the {@link AssessmentClass#hashCode()} method.
     * Confirms that equal objects have the same hash code and unequal objects have different hash codes.
     */
    @Test
    void testHashCode() {
        assertEquals(assessmentClass1.hashCode(), assessmentClass1.hashCode());
        assertNotEquals(assessmentClass1.hashCode(), assessmentClass2.hashCode());
    }
}