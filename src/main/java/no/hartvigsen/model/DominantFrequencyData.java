package no.hartvigsen.model;

import java.time.ZonedDateTime;

public class DominantFrequencyData {
    private final ZonedDateTime timestamp;
    private final Double f;

    private final Double A;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Double getF() {
        return f;
    }

    public Double getA() {
        return A;
    }

    public DominantFrequencyData(ZonedDateTime timestamp, Double f, Double a) {
        this.timestamp = timestamp;
        this.f = f;
        A = a;
    }



}
