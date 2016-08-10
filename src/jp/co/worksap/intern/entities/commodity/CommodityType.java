package jp.co.worksap.intern.entities.commodity;

/**
 * Created by yuminchen on 16/7/24.
 */
public enum CommodityType {
    popcorn(10.0), sodas(5.0), juice(5.5), chips(4.5), doll(14.0), glasses(38.0), toy(25);

    CommodityType(double cost){
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    private double cost;


    @Override
    public String toString() {
        switch (this) {
            case popcorn:
                return "popcorn";
            case sodas:
                return "sodas";
            case juice:
                return "juice";
            case chips:
                return "chips";
            case doll:
                return "doll";
            case glasses:
                return "glasses";
            case toy:
                return "toy";
            default:
                return "";
        }
    }

}