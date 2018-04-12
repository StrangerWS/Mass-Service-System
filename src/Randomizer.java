public class Randomizer {

    private static final double LAMBDA = 0.35;
    private static final double U = 0.3;


    private Type randomType;

    public Randomizer(Type randomType) {
        this.randomType = randomType;
    }

    public double getTimestamp(boolean forArrive){//for arrive or for serving boolean
        return forArrive ? (-1d / LAMBDA) * Math.log(Math.random()) : (-1d / U) * Math.log(Math.random());
    }
}
