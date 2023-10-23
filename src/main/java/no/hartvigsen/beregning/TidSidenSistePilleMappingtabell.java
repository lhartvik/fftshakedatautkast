package no.hartvigsen.beregning;

import no.hartvigsen.model.Pilltime;
import no.hartvigsen.model.StyrkeRysting;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static no.hartvigsen.model.StyrkeRysting.zero;

public class TidSidenSistePilleMappingtabell {
    Map<ZonedDateTime, StyrkeRysting> map;

    public StyrkeRysting get(ZonedDateTime zdt) {
        return map.getOrDefault(zdt, zero());
    }

    public TidSidenSistePilleMappingtabell(List<Pilltime> pilltimes, List<ZonedDateTime> vbTimes) {
        Map<ZonedDateTime, StyrkeRysting> durations = new TreeMap<>();
        List<ZonedDateTime> sortedVibrations = vbTimes.stream().sorted(ChronoZonedDateTime::compareTo).toList();

        for (ZonedDateTime currentVibrationTime : sortedVibrations) {
            pilltimes.stream()
                    .filter(p -> p.tid().isBefore(currentVibrationTime))
                    .max(comparing(Pilltime::tid))
                    .ifPresent((Pilltime sistePille) -> durations.put(currentVibrationTime, new StyrkeRysting(sistePille.pill().styrkeMg(), Duration.between(sistePille.tid(), currentVibrationTime))));
        }
        map = durations;
    }

}
