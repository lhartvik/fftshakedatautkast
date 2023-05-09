package no.hartvigsen;

import no.hartvigsen.model.DominantFrequencyData;
import no.hartvigsen.model.ReadJSONResults;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static no.hartvigsen.Beregninger.calculateDominantFrequency;

public class MainSpectrumAnalysis {
    public static void main(String[] args) {

        ReadJSONResults readJSONResults = new ReadJSON().read();
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
        System.out.println("time_stamps = " + dominantFrequencyDataList.stream().map(DominantFrequencyData::getTimestamp).map(t -> "'" + t + "'").toList());
        System.out.println("noiseTimes = " + noiseTimes.stream().map(t -> "'" + t + "'").toList());
        System.out.println("noise = " + scaledNoise);
        System.out.println("pill_times = " + readJSONResults.pilltimes().stream().map(t -> "'" + t + "'").toList());

    }
}


