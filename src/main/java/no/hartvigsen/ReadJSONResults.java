package no.hartvigsen;

import java.time.ZonedDateTime;
import java.util.List;

public record ReadJSONResults (List<VibrationData> vibrationDataList, List<ZonedDateTime> pilltimes) {
}
