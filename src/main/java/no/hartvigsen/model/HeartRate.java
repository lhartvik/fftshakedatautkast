package no.hartvigsen.model;

import java.time.ZonedDateTime;

public record HeartRate(ZonedDateTime tid, int beatsPerMinute, int variablility) implements Tidspunkt{
}
