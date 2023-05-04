package no.hartvigsen;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.Map;

public class MainFFT {
    public static void main(String[] args) throws PythonExecutionException, IOException {

        ReadJSONResults readJSONResults = new ReadJSON().read();
        Map<ZonedDateTime, Double> noiselevels = Noiselevel.noiselevels(readJSONResults.vibrationDataList());

        List<Double> y = noiselevels.values().stream().toList();

        System.out.println("data_points = " + noiselevels.values());
        System.out.println("time_stamps = " + readJSONResults.vibrationDataList().stream().map(VibrationData::getTimestamp).map(t -> "'"+t+"'").toList());
        System.out.println("pill_times = " + readJSONResults.pilltimes().stream().map(t -> "'"+t+"'").toList());

//        Plot plt = Plot.create();
//        plt.plot().add(x, y, "t").label("A");
//        plt.legend().loc("upper right");
//        plt.title("scatter");
//        plt.show();
    }
}


