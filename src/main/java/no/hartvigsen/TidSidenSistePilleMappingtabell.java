package no.hartvigsen;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TidSidenSistePilleMappingtabell {
    Map<ZonedDateTime, Duration> map;

    public Duration get(ZonedDateTime zdt) {
        return map.getOrDefault(zdt, Duration.ZERO);
    }

    public TidSidenSistePilleMappingtabell(List<ZonedDateTime> pilltimes, List<ZonedDateTime> vbTimes) {

        pilltimes = pilltimes.stream().sorted().toList();
        Iterator<ZonedDateTime> it = pilltimes.iterator();

        ZonedDateTime currentPillTime = it.next();
        ZonedDateTime nextPillTime = it.next();
        vbTimes = vbTimes.stream().sorted().toList();

        Map<ZonedDateTime, Duration> durations = new TreeMap<>();

        for (ZonedDateTime vbTime : vbTimes) {
            if (nextPillTime != null && vbTime.isAfter(nextPillTime)) {
                currentPillTime = nextPillTime;
                nextPillTime = it.hasNext() ? it.next() : null;
            }
            if (vbTime.isAfter(currentPillTime)) durations.put(vbTime, Duration.between(currentPillTime, vbTime));
        }
        this.map = durations;
    }
}
