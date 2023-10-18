package no.hartvigsen;

import no.hartvigsen.beregning.Beregninger;
import no.hartvigsen.io.ReadJSON;
import no.hartvigsen.model.DominantFrequencyData;
import no.hartvigsen.model.Pilltime;
import no.hartvigsen.model.ReadJSONResults;

import java.net.URISyntaxException;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static no.hartvigsen.beregning.Beregninger.calculateDominantFrequency;

public class MainSpectrumAnalysis {
    public static void main(String[] args) throws URISyntaxException {

        ReadJSONResults readJSONResults = new ReadJSON()
                .read("parkinsontracker-eb259-default-rtdb-export (2).json")
                .filterFor(Month.OCTOBER);

        Map<ZonedDateTime, Double> noiselevels = Beregninger.noiselevels(readJSONResults.vibrationDataList());

        List<DominantFrequencyData> dominantFrequencyDataList =
                readJSONResults.vibrationDataList().stream().map(calculateDominantFrequency).toList();

        List<Double> amplitudes = dominantFrequencyDataList.stream().map(DominantFrequencyData::getA).toList();

        List<ZonedDateTime> noiseTimes = dominantFrequencyDataList.stream().map(DominantFrequencyData::getTimestamp).sorted().toList();
        List<Double> noises = noiseTimes.stream().map(noiselevels::get).toList();

        Double maxAmp = amplitudes.stream().max(Double::compare).orElseThrow();
        Double maxNoise = noises.stream().max(Double::compare).orElseThrow();

        List<Double> scaledNoise = noises.stream().map(d -> d * maxAmp / maxNoise).toList();

        System.out.println("data_points = " + amplitudes);
        System.out.println("frequencies = " + dominantFrequencyDataList.stream().map(DominantFrequencyData::getF).toList());
        System.out.println("time_stamps = " + dominantFrequencyDataList.stream().map(DominantFrequencyData::getTimestamp)
                .map(formatterDato)
                .map(t -> "'" + t + "'").toList());
        System.out.println("noiseTimes = " + noiseTimes.stream()
                .map(formatterDato)
                .map(t -> "'" + t + "'").toList());
        System.out.println("noise = " + scaledNoise);
        System.out.println("pill_times = " + readJSONResults.pilltimes().stream().map(Pilltime::tid)
                        .map(formatterDato)
                .map(t -> "'" + t + "'").toList());
    }

    static Function<ZonedDateTime, String> formatterDato = t -> t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

}


