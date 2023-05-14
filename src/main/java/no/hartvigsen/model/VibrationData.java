package no.hartvigsen.model;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class VibrationData {
    ZoneId norwegianTimeZone = ZoneId.of("Europe/Oslo");
    private final ZonedDateTime timestamp;

    private final List<BigDecimal> vibrationdata;

    public VibrationData(String timestamp) {
        this.timestamp = ZonedDateTime.parse(timestamp).withZoneSameInstant(norwegianTimeZone);
        vibrationdata = new ArrayList<>();
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public List<BigDecimal> getVibrationdata() {
        return vibrationdata;
    }

    public void addVibrationdata(BigDecimal vibrationdata) {
        this.vibrationdata.add(vibrationdata);
    }

}
