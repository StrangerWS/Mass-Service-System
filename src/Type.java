public enum Type {
    EXPONENT(0.35),
    NORMAL(0),
    CONST(0);

    public double lambda;

    Type(double lambda){
        this.lambda = lambda;
    }
}
