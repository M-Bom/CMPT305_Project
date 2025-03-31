package ca.macewan.cmpt305.groupproject;

public class Catchment {
    private String multiPolygon;

    public Catchment(String multiPolygon) {
        this.multiPolygon = multiPolygon;
    }

    public String getMultiPolygon() {
        return multiPolygon;
    }

    @Override
    public String toString() {
        return multiPolygon;
    }
}
