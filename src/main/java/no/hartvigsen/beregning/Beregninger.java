package no.hartvigsen.beregning;

import com.tambapps.fft4j.FastFouriers;
import no.hartvigsen.model.DominantFrequencyData;
import no.hartvigsen.model.FrequencyAmplitude;
import no.hartvigsen.model.VibrationData;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public final class Beregninger {

    private Beregninger(){}
    public static OptionalDouble of(List<BigDecimal> signal) {
        double average = signal.stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getAverage();
        return signal.stream().mapToDouble(BigDecimal::doubleValue).map(d -> Math.abs(d - average)).average();
    }

    public static Map<ZonedDateTime, Double> noiselevels(List<VibrationData> vibrationDataList) {
        return vibrationDataList.stream()
                .collect(toMap(
                        VibrationData::getTimestamp,
                        v -> Beregninger.of(v.getVibrationdata()),
                        (a, b) -> b
                ))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getAsDouble(),
                        (a, b) -> b, TreeMap::new));
    }

    private static double glidendeSnitt(double[] ar, int x, int range) {
        return Arrays.stream(Arrays.copyOfRange(ar, x - range, x + range)).sum() / (range * 2);
    }

    public static List<FrequencyAmplitude> computeSpectrum(List<BigDecimal> vibrationdata) {
        double[] real = vibrationdata.stream().mapToDouble(BigDecimal::doubleValue).toArray();
        double[] imaginary = new double[real.length];

        double time = 10.0; // sec
        int N = real.length;
        double hz = N / time;

        List<Double> frequencies = IntStream.rangeClosed(0, N - 1).mapToDouble(k -> k * hz / N).boxed().toList();
        FastFouriers.BASIC.transform(real, imaginary);

        List<FrequencyAmplitude> frequencyAmplitudes = new ArrayList<>();
        for (int i = 10; i < N / 2; i++)
            frequencyAmplitudes.add(new FrequencyAmplitude(frequencies.get(i), glidendeSnitt(real, i, 5), glidendeSnitt(imaginary, i, 5)));

        return frequencyAmplitudes;
    }

    public static FrequencyAmplitude getMainFrequency(List<FrequencyAmplitude> list) {
        return list.stream().max(Comparator.comparingDouble(FrequencyAmplitude::getA)).orElseThrow();
    }

    public static final Function<VibrationData, DominantFrequencyData> calculateDominantFrequency =
            v -> {
                FrequencyAmplitude mainFrequency = getMainFrequency(computeSpectrum(v.getVibrationdata()));
                return new DominantFrequencyData(v.getTimestamp(), mainFrequency.getF(), mainFrequency.getA());
            };
}
