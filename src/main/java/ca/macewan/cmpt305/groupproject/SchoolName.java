package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class SchoolName {
    private String name;
    private String attend;

    /**
     * Constructor for SchoolName object
     * @param name
     * @param attend
     */
    public SchoolName(String name, String attend){
        this.name = name;
        this.attend = attend;
    }

    /**
     * Method to get name
     * @return name - String
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get Attend
     * @return attend - String
     */
    public String getAttend() {
        return attend;
    }

    /**
     * Override toSting
     * @return string
     */
    @Override
    public String toString() {
        // If name for school is not null
        if (name != null){
            // return
            return name;
        }
        // If school doesn't have a name then return the attend name
        return Objects.requireNonNullElse(attend, "");
    }
}
