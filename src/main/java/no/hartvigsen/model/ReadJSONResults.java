package no.hartvigsen.model;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

public record ReadJSONResults(List<VibrationData> vibrationDataList, List<Pilltime> pilltimes) {
    public ReadJSONResults filterFor(Month... months) {
        return new ReadJSONResults(
                this.vibrationDataList.stream().filter(v -> Arrays.stream(months).toList().contains(v.getTimestamp().getMonth())).toList(),
                this.pilltimes().stream().filter(p -> Arrays.stream(months).toList().contains(p.tid().getMonth())).toList()
        );
    }
}
