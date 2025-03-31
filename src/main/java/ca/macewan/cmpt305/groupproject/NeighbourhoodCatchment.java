package ca.macewan.cmpt305.groupproject;

public class NeighbourhoodCatchment {
    private Neighbourhood neighbourhood;
    private Catchment catchment;

    public NeighbourhoodCatchment (Neighbourhood neighbourhood, Catchment catchment){
        this.neighbourhood = neighbourhood;
        this.catchment = catchment;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }
    public Catchment getCatchment() {
        return catchment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NeighbourhoodCatchment){
            return this.neighbourhood.equals(((NeighbourhoodCatchment) obj).neighbourhood);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return neighbourhood != null ? neighbourhood.hashCode() : 0;
    }

}
