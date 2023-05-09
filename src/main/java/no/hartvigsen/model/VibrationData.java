package no.hartvigsen.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class VibrationData {
    private ZonedDateTime timestamp;

    private List<BigDecimal> vibrationdata;

    public VibrationData(String timestamp) {
        this.timestamp = ZonedDateTime.parse(timestamp);
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
