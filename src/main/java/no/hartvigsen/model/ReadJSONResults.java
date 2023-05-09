package no.hartvigsen.model;

import no.hartvigsen.model.VibrationData;

import java.time.ZonedDateTime;
import java.util.List;

public record ReadJSONResults (List<VibrationData> vibrationDataList, List<ZonedDateTime> pilltimes) {
}
