package no.hartvigsen.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public record TimeIntensity(LocalTime time, Double intensity, Duration tidSidenSistePille){
    public TimeIntensity (ZonedDateTime zdt, Double i, Duration tidSidenSistePille){
        this(zdt.toLocalTime().truncatedTo(ChronoUnit.MINUTES), i, tidSidenSistePille);
    }
}
