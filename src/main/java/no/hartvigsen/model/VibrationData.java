package no.hartvigsen.model;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

public class VibrationData implements Tidspunkt{
    private final ZonedDateTime timestamp;

    private final List<BigDecimal> datalist;

    public VibrationData(String timestamp) {
        this.timestamp = ZonedDateTime.parse(timestamp).withZoneSameInstant(ZoneId.of("Europe/Oslo"));
        datalist = new ArrayList<>();
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
    public ZonedDateTime tid() {
        return timestamp;
    }

    public List<BigDecimal> getVibrationdata() {
        return datalist;
    }

    public void addVibrationdata(BigDecimal vibrationdata) {
        this.datalist.add(vibrationdata);
    }

    @Override
    public String toString() {
        return "VibrationData " + timestamp.format(ofPattern("yyyy-MM-dd, HH:mm"));
    }
}
