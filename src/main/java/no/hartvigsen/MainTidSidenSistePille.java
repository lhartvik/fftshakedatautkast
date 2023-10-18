package no.hartvigsen;

import no.hartvigsen.beregning.Beregninger;
import no.hartvigsen.beregning.TidSidenSistePilleMappingtabell;
import no.hartvigsen.io.ReadJSON;
import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.TimeIntensity;
import no.hartvigsen.model.VibrationData;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static java.time.Month.APRIL;
import static java.util.Comparator.comparing;

public class MainTidSidenSistePille {
    public static void main(String[] args) throws URISyntaxException {
        ReadJSONResults readJSONResults = new ReadJSON()
                .read("vibration_data.json")
                .filterFor(APRIL);
        Map<ZonedDateTime, Double> noiselevels = Beregninger.noiselevels(
                readJSONResults.vibrationDataList());

        TidSidenSistePilleMappingtabell tidSidenSistePille =
                new TidSidenSistePilleMappingtabell(
                        readJSONResults.pilltimes(),
                        readJSONResults.vibrationDataList().stream().map(VibrationData::getTimestamp).toList());

        List<TimeIntensity> timeIntensities = noiselevels.entrySet().stream()
                .map(e -> new TimeIntensity(e.getKey(), e.getValue(), tidSidenSistePille.get(e.getKey())))
                .filter(ti -> erMerEnn30Minutter(ti.tidSidenSistePille()))
                .filter(ti -> erMindreEnn12Timer(ti.tidSidenSistePille()))
                .sorted(comparing(TimeIntensity::time))
                .toList();

        System.out.println("timeIntensities.size() = " + timeIntensities.size());
        // times - minutt på døgnet

        List<Double> list = timeIntensities.stream().map(TimeIntensity::intensity).toList();
        System.out.println("antall values = " + list.size());
        List<Long> tidSiden = timeIntensities.stream()
                .map(TimeIntensity::tidSidenSistePille)
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
