package ca.macewan.cmpt305.groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentsTest {
    private PropertyAssessments assessments;
    private PropertyAssessments filteredAssessments;

    /**
     * Sets up the test environment by loading the property assessment data.
     * This method is called before each test to initialize the assessments object
     * and prepare filtered data.
     * @throws IOException If an error occurs during file reading.
     */
    @BeforeEach
    void setUp() throws IOException {
        assessments = new PropertyAssessments("src/main/resources/Property_Assessment_Data_2024.csv");

        filteredAssessments = assessments.getFilteredData("gorman");
    }

    /**
     * Tests the {@link PropertyAssessments#getPropertyAssessment(String)} method.
     * This method checks if valid and invalid property IDs return the expected results.
     */
    @Test
    void getPropertyAssessment() {
        String validPropertyId  = "1103530";
        String invalidPropertyId = "1";
        String nonNumericId = "abc";

        String result1 = assessments.getPropertyAssessment(validPropertyId);
        assertNotEquals("Error: Invalid account number...\n", result1);

        String result2 = assessments.getPropertyAssessment(invalidPropertyId);
        assertEquals("Error: Invalid account number...\n", result2);

        String result3 = assessments.getPropertyAssessment(nonNumericId);
        assertEquals("Error: Invalid account number...\n", result3);
    }

    /**
     * Tests the {@link PropertyAssessments#getFilteredData(String)} method.
     * Validates that the data is filtered correctly based on a search term.
     */
    @Test
    void getFilteredData() {
        PropertyAssessments filtered = assessments.getFilteredData("downtown");
        assertNotNull(filtered);
        assertTrue(filtered.getSize() < assessments.getSize());

        PropertyAssessments filtered2 = assessments.getFilteredData("uptown");
        assertNull(filtered2);

        PropertyAssessments filtered3 = assessments.getFilteredData("y");
        assertNotNull(filtered3);
        assertTrue(filtered3.getSize() < assessments.getSize());
    }

    /**
     * Tests the data reading functionality from the CSV file.
     * Ensures that the data is correctly read into a list and contains valid PropertyAssessment objects.
     * @throws IOException If there is an issue with reading the file.
     */
    @Test
    void readData() throws IOException {
        assessments = new PropertyAssessments("src/main/resources/Property_Assessment_Data_2024.csv");

        List<PropertyAssessment> data = assessments.readData("src/main/resources/Property_Assessment_Data_2024.csv");
        assertNotNull(data);
        assertFalse(data.isEmpty());
        PropertyAssessment first = data.getFirst();
        assertNotNull(first);
        assertTrue(first.getAssessmentClass().getAssessedValue() > 0);
    }

    /**
     * Tests the {@link PropertyAssessments#getData()} method.
     * Verifies that the correct amount of data is returned.
     */
    @Test
    void getData() {
        assertEquals(assessments.getSize(), assessments.getData().size());
    }

    /**
     * Tests the {@link PropertyAssessments#getSize()} method.
     * Ensures that the size of the assessments is greater than zero.
     */
    @Test
    void getSize() {
        assessments.getSize();
        assertTrue(assessments.getSize() > 0);
    }

    /**
     * Tests the {@link PropertyAssessments#getAllAssessedValues(List)} method.
     * Verifies that a list of all assessed values is generated and the size matches the number of assessments.
     */
    @Test
    void getAllAssessedValues() {
        List<Integer> assessedValues = assessments.getAllAssessedValues(assessments.getData());
        assertNotNull(assessedValues);
        assertEquals(assessedValues.size(), assessments.getSize());
    }

    /**
     * Tests the {@link PropertyAssessments#getAssessmentClasses(String[])} method.
     * Verifies that the correct assessment classes are returned based on account information.
     */
    @Test
    void getAssessmentClasses() {
        String[] accountInfo1 = new String[18];
        accountInfo1[15] = "RESIDENTIAL";
        accountInfo1[16] = "COMMERCIAL";
        accountInfo1[17] = "FARMLAND";

        String[] result1 = assessments.getAssessmentClasses(accountInfo1);
        assertNotNull(result1);
        assertEquals(3, result1.length);
        assertEquals(accountInfo1[15], result1[0]);
        assertEquals(accountInfo1[16], result1[1]);
        assertEquals(accountInfo1[17], result1[2]);

        String[] accountInfo2 = new String[17];
        accountInfo2[15] = "RESIDENTIAL";
        accountInfo2[16] = "COMMERCIAL";

        String[] result2 = assessments.getAssessmentClasses(accountInfo2);
        assertNotNull(result2);
        assertEquals(3, result2.length);
        assertEquals(accountInfo2[15], result2[0]);
        assertEquals(accountInfo2[16], result2[1]);
        assertNull(result2[2]);

        String[] accountInfo3 = new String[16];
        accountInfo2[15] = "RESIDENTIAL";

        String[] result3 = assessments.getAssessmentClasses(accountInfo3);
        assertNotNull(result2);
        assertEquals(3, result3.length);
        assertEquals(accountInfo3[15], result3[0]);
        assertNull(result3[1]);
        assertNull(result3[2]);
    }

    /**
     * Tests the {@link PropertyAssessments#getAssessmentPercent(String[])} method.
     * Validates that the correct assessment percentages are returned based on account information.
     */
    @Test
    void getAssessmentPercent() {
        String[] accountInfo1 = new String[18];
        accountInfo1[12] = "50";
        accountInfo1[13] = "25";
        accountInfo1[14] = "25";

        String[] result1 = assessments.getAssessmentPercent(accountInfo1);
        assertNotNull(result1);
        assertEquals(3, result1.length);
        assertEquals(accountInfo1[12], result1[0]);
        assertEquals(accountInfo1[13], result1[1]);
        assertEquals(accountInfo1[14], result1[2]);

        String[] accountInfo2 = new String[17];
        accountInfo2[12] = "75";
        accountInfo2[13] = "25";

        String[] result2 = assessments.getAssessmentPercent(accountInfo2);
        assertNotNull(result2);
        assertEquals(3, result2.length);
        assertEquals(accountInfo2[12], result2[0]);
        assertEquals(accountInfo2[13], result2[1]);
        assertNull(result2[2]);

        String[] accountInfo3 = new String[16];
        accountInfo2[12] = "100";

        String[] result3 = assessments.getAssessmentPercent(accountInfo3);
        assertNotNull(result2);
        assertEquals(3, result3.length);
        assertEquals(accountInfo3[12], result3[0]);
        assertNull(result3[1]);
        assertNull(result3[2]);
    }

    /**
     * Tests the {@link PropertyAssessments#maxAssessedValue()} method.
     * Verifies that the maximum assessed value is correct.
     */
    @Test
    void maxAssessedValue() {
        int maxValue = assessments.maxAssessedValue();

        assertEquals(Collections.max(assessments.getAllAssessedValues(assessments.getData())), maxValue);
    }

    /**
     * Tests the {@link PropertyAssessments#minAssessedValue()} method.
     * Verifies that the minimum assessed value is correct.
     */
    @Test
    void minAssessedValue() {
        int minValue = assessments.minAssessedValue();

        assertEquals(Collections.min(assessments.getAllAssessedValues(assessments.getData())), minValue);
    }

    /**
     * Tests the {@link PropertyAssessments#getRange()} method.
     * Verifies that the range of assessed values is correct.
     */
    @Test
    void getRange() {
        int rangeValue = assessments.getRange();

        assertEquals((assessments.maxAssessedValue()-assessments.minAssessedValue()), rangeValue);
    }

    /**
     * Tests the {@link PropertyAssessments#getSortedAssessedValues(List)} method.
     * Verifies that the list of assessed values is sorted correctly.
     */
    @Test
    void getSortedAssessedValues() {
        List<Integer> sortedAssessedValues = assessments.getSortedAssessedValues(assessments.getAllAssessedValues(assessments.getData()));

        assertNotNull(sortedAssessedValues);
        assertEquals(sortedAssessedValues.size(), assessments.getAllAssessedValues(assessments.getData()).size());
    }

    /**
     * Tests the {@link PropertyAssessments#getMean()} method.
     * Verifies that the mean of the assessed values is calculated correctly.
     */
    @Test
    void getMean() {
        assessments.getMean();
        assertTrue(assessments.getMean() > 0);
        assertEquals(478463, assessments.getMean(), 0.001);

        filteredAssessments.getMean();
        assertTrue(filteredAssessments.getMean() > 0);
        assertEquals(1489458, filteredAssessments.getMean(), 0.001);
    }

    /**
     * Tests the {@link PropertyAssessments#getMedian()} method.
     * Verifies that the median of the assessed values is calculated correctly.
     */
    @Test
    void getMedian() {
        assessments.getMedian();
        assertTrue(assessments.getMedian() > 0);
        assertEquals(334000, assessments.getMedian());

        filteredAssessments.getMedian();
        assertTrue(filteredAssessments.getMedian() > 0);
        assertEquals(86750, filteredAssessments.getMedian());
    }

    /**
     * Tests the {@link PropertyAssessments#toString()} method.
     * Validates the string representation of the PropertyAssessments object.
     */
    @Test
    void testToString() {
        String actualOutput = assessments.toString();
        String expectedOutput = String.format("n = %d \nmin = $%,d \nmax = $%,d \nrange = $%,d \nmean = $%,.0f \nmedian = $%,d \n%n", assessments.getSize(), assessments.minAssessedValue(), assessments.maxAssessedValue(), assessments.getRange(), assessments.getMean(), assessments.getMedian());
        assertEquals(expectedOutput, actualOutput);
    }
}