package no.hartvigsen;

import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.TimeIntensity;
import no.hartvigsen.model.VibrationData;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainTidSidenSistePille {
    public static void main(String[] args) {
        ReadJSONResults readJSONResults = new ReadJSON().read();
        Map<ZonedDateTime, Double> noiselevels = Beregninger.noiselevels(readJSONResults.vibrationDataList());

        TidSidenSistePilleMappingtabell tidSidenSistePille = new TidSidenSistePilleMappingtabell(readJSONResults.pilltimes(), readJSONResults.vibrationDataList().stream().map(VibrationData::getTimestamp).toList());
        List<TimeIntensity> timeIntensities = noiselevels.entrySet().stream()
                .map(e -> new TimeIntensity(e.getKey(), e.getValue(), tidSidenSistePille.get(e.getKey())))
                .filter(ti -> erMerEnn30Minutter(ti.tidSidenSistePille()))
                .sorted(Comparator.comparing(TimeIntensity::time))
                .toList();

        System.out.println("timeIntensities.size() = " + timeIntensities.size());
        // times - minutt på døgnet
        System.out.println("times = " + timeIntensities.stream()
                .map(t -> t.time().getHour() * 60 + t.time().getMinute()).toList());
        System.out.println("values = " + timeIntensities.stream().map(TimeIntensity::intensity).toList());
        System.out.println("tidSidenSistePille = " + timeIntensities.stream()
                .map(TimeIntensity::tidSidenSistePille)
                .map(Duration::getSeconds)
                .map(l -> l / 3600)
                .toList());
    }

    private static boolean erMerEnn30Minutter(Duration duration) {
        return duration.compareTo(Duration.ofMinutes(30)) > 0;
    }
}
