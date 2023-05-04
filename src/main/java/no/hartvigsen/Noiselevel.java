package no.hartvigsen;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

public class Noiselevel {
    public static OptionalDouble of(List<BigDecimal> signal) {
        double average = signal.stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getAverage();
        OptionalDouble averageAbsoluteNoiselevel = signal.stream().mapToDouble(BigDecimal::doubleValue).map(d -> Math.abs(d - average)).average();
        return averageAbsoluteNoiselevel;
    }

    public static Map<ZonedDateTime, Double> noiselevels(List<VibrationData> vibrationDataList) {
        return vibrationDataList.stream()
                .collect(toMap(
                        VibrationData::getTimestamp,
                        v -> Noiselevel.of(v.getVibrationdata())
                ))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getAsDouble(),
                        (a,b) -> b, TreeMap::new));
    }
}
