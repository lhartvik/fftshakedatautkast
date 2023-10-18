package no.hartvigsen.beregning;

import no.hartvigsen.model.Pilltime;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;

public class TidSidenSistePilleMappingtabell {
    Map<ZonedDateTime, Duration> map;

    public Duration get(ZonedDateTime zdt) {
        return map.getOrDefault(zdt, Duration.ZERO);
    }

    public TidSidenSistePilleMappingtabell(List<Pilltime> pilltimes, List<ZonedDateTime> vbTimes) {
        Map<ZonedDateTime, Duration> durations = new TreeMap<>();
        List<ZonedDateTime> sortedVibrations = vbTimes.stream().sorted(ChronoZonedDateTime::compareTo).toList();

        for (ZonedDateTime currentVibrationTime : sortedVibrations) {
            pilltimes.stream()
                    .filter(p -> p.tid().isBefore(currentVibrationTime))
                    .max(comparing(Pilltime::tid))
                    .ifPresent((Pilltime sistePille) -> durations.put(currentVibrationTime, Duration.between(sistePille.tid(), currentVibrationTime)));
        }
        map = durations;
    }

}
