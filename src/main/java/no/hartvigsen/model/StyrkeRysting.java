package no.hartvigsen.model;

import java.time.Duration;

public record StyrkeRysting(int styrke, Duration duration){
    public static StyrkeRysting zero() {return new StyrkeRysting(0, Duration.ZERO);}

}
