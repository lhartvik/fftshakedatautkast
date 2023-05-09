package no.hartvigsen.model;

public class FrequencyAmplitude {
    private final Double f;
    private final Double A;

    public Double getF() {
        return f;
    }

    public Double getA() {
        return A;
    }

    public FrequencyAmplitude(Double f, Double a, Double i) {
        this.f = f;
        this.A = Math.sqrt(Math.pow(a,2)*Math.pow(i,2));
    }

    @Override
    public String toString() {
        return String.format("%fHz = %f", f, A);
    }
}
