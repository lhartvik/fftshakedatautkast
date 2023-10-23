package no.hartvigsen;

import no.hartvigsen.beregning.Beregninger;
import no.hartvigsen.beregning.TidSidenSistePilleMappingtabell;
import no.hartvigsen.io.ReadJSON;
import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.TidSkjelving;
import no.hartvigsen.model.VibrationData;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class MainTidSidenSistePille {
    public static void main(String[] args) throws URISyntaxException {
        Month[] ALLE_MÅNEDER = Month.values();
        ReadJSONResults readJSONResults = new ReadJSON()
                .read("vibration_data.json")
                .filterFor(ALLE_MÅNEDER);
        Map<ZonedDateTime, Double> noiselevels = Beregninger.noiselevels(
                readJSONResults.vibrationDataList());

        TidSidenSistePilleMappingtabell tidSidenSistePille =
                new TidSidenSistePilleMappingtabell(
                        readJSONResults.pilltimes(),
                        readJSONResults.vibrationDataList().stream().map(VibrationData::getTimestamp).toList());

        Map<Integer, List<TidSkjelving>> timeIntensities = noiselevels.entrySet().stream()
                .map(e -> new TidSkjelving(e.getKey(), e.getValue(), tidSidenSistePille.get(e.getKey()).duration(), tidSidenSistePille.get(e.getKey()).styrke()))
                .filter(ti -> erMerEnn30Minutter(ti.tidSidenSistePille()))
                .filter(ti -> erMindreEnn12Timer(ti.tidSidenSistePille()))
                .sorted(comparing(TidSkjelving::time))
                .collect(Collectors.groupingBy(TidSkjelving::medisinstyrke));
        timeIntensities.forEach((key1, value1) -> System.out.println(key1 + "mg: " + value1.size()));
        // times - minutt på døgnet

        timeIntensities.forEach((key, value) -> {
            System.out.println("*****************" + key + "*****************");
            extracted(value);
        });
    }

    private static void extracted(List<TidSkjelving> timeIntensities) {
        List<Double> list = timeIntensities.stream().map(TidSkjelving::intensity).toList();
        System.out.println("antall values = " + list.size());
        List<Long> tidSiden = timeIntensities.stream()
                .map(TidSkjelving::tidSidenSistePille)
                .map(Duration::getSeconds)
                .map(l -> l / 60)
                .toList();
        System.out.println("tidSiden.size() = " + tidSiden.size());
        System.out.println("times = " + timeIntensities.stream().map(t -> t.time().getHour() * 60 + t.time().getMinute()).toList());
        System.out.println("values = " + list);
        System.out.println("tidSidenSistePille = " + tidSiden);
    }

    private static boolean erMerEnn30Minutter(Duration duration) {
        return duration.compareTo(Duration.ofMinutes(30)) > 0;
    }

    private static boolean erMindreEnn12Timer(Duration duration) {
        return duration.compareTo(Duration.ofHours(12)) < 0;
    }

}
