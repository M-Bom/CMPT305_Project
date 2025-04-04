package ca.macewan.cmpt305.groupproject;

public class SchoolType {
    private String type;
    private String grades;

    /**
     * Constructor for SchoolType object
     * @param type
     * @param grades
     */
    public SchoolType(String type, String grades) {
        this.type = type;
        this.grades = grades;
    }

    /**
     * Method to get type
     * @return type - String
     */
    public String getType() {
        return type;
    }

    /**
     * Method to get grade
     * @return grade - String
     */
    public String getGrades() {
        return grades;
    }

    /**
     * Override toString method
     * @return string
     */
    @Override
    public String toString() {
        return "SchoolType{" + "type=" + type + ", grades=" + grades + '}';
    }

}
