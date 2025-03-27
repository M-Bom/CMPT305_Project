package ca.macewan.cmpt305.groupproject;

import java.util.Objects;

public class SchoolName {
    private String name;
    private String attend;

    public SchoolName(String name, String attend){
        this.name = name;
        this.attend = attend;
    }
    public String getName() {
        return name;
    }
    public String getAttend() {
        return attend;
    }

    @Override
    public String toString() {
        if (name != null){
            return name;
        }
        return Objects.requireNonNullElse(attend, "");
    }
}
