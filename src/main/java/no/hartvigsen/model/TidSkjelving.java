package no.hartvigsen.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public record TidSkjelving(LocalTime time, Double intensity, Duration tidSidenSistePille, int medisinstyrke){
    public TidSkjelving(ZonedDateTime zdt, Double i, Duration tidSidenSistePille, int medisinstyrke){
        this(zdt.toLocalTime().truncatedTo(ChronoUnit.MINUTES), i, tidSidenSistePille, medisinstyrke);
    }
}
