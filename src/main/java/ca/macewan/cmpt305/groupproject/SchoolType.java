package ca.macewan.cmpt305.groupproject;

public class SchoolType {
    private String type;
    private String grades;

    public SchoolType(String type, String grades) {
        this.type = type;
        this.grades = grades;
    }
    public String getType() {
        return type;
    }

    public String getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return "SchoolType{" + "type=" + type + ", grades=" + grades + '}';
    }

}
