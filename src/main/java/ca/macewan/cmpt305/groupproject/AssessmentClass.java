package ca.macewan.cmpt305.groupproject;

import java.util.Arrays;

public class AssessmentClass {
    private int assessedValue;
    private String[] classNames;
    private String[] classPercents;

    /**
     * Constructs an AssessmentClass object with the specified assessed value, class names, and class percentages.
     *
     * @param assessedValue the assessed value of the property.
     * @param classNames the array of class names (up to three).
     * @param classPercents the array of class percentages (up to three).
     */
    public AssessmentClass(int assessedValue, String[] classNames, String[] classPercents) {
        this.assessedValue = assessedValue;
        this.classNames = classNames;
        this.classPercents = classPercents;
    }

    /**
     * Returns the assessed value of the property.
     *
     * @return the assessed value.
     */
    public int getAssessedValue() {
        return assessedValue;
    }

    /**
     * Returns the array of class names.
     *
     * @return an array of class names.
     */
    public String[] getClassNames() {
        return classNames;
    }

    /**
     * Returns the array of class percentages.
     *
     * @return an array of class percentages.
     */
    public String[] getClassPercents() {
        return classPercents;
    }

    /**
     * Returns a string representation of the AssessmentClass, including the assessed value
     * and a formatted list of class names and their corresponding percentages.
     *
     * <p>Handles cases where there may be one, two, or three class entries, omitting empty entries.</p>
     *
     * @return a string representation of the assessment class.
     */
    @Override
    public String toString() {
        String className1 = this.classNames[0];
        String className2 = this.classNames[1];
        String className3 = this.classNames[2];

        String classPercent1 = this.classPercents[0];
        String classPercent2 = this.classPercents[1];
        String classPercent3 = this.classPercents[2];

        if (className2 == null || className2.isEmpty() && (className3 == null || className3.isEmpty())) {
            return String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%]", this.assessedValue, className1, classPercent1);
        } else if (className3 == null || className3.isEmpty()) {
            return String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%, %s %s%%]", this.assessedValue, className1, classPercent1, className2, classPercent2);
        } else {
            return String.format("Assessed Value = $%,d\nAssessment Class = [%s %s%%, %s %s%%, %s %s%%]", this.assessedValue, className1, classPercent1, className2, classPercent2, className3, classPercent3);
        }
    }

    /**
     * Compares this AssessmentClass to the specified object for equality.
     * Two AssessmentClass objects are considered equal if they have the same assessed value,
     * class names, and class percentages.
     *
     * @param obj the object to compare to.
     * @return true if the specified object is equal to this AssessmentClass, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AssessmentClass that = (AssessmentClass) obj;
        if (assessedValue != that.assessedValue) return false;
        return Arrays.equals(classNames, that.classNames) && Arrays.equals(classPercents, that.classPercents);
    }

    /**
     * Returns a hash code value for the AssessmentClass based on the class names and class percentages arrays.
     *
     * @return a hash code value for this AssessmentClass.
     */
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(classNames);
        result = 31 * result + Arrays.hashCode(classNames);
        result = 31 * result + Arrays.hashCode(classPercents);
        return result;
    }
}