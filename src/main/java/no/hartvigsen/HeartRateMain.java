package no.hartvigsen;

import no.hartvigsen.io.ReadHeartRateJSON;
import no.hartvigsen.io.ReadPillTimeJSON;
import no.hartvigsen.model.HeartRate;
import no.hartvigsen.model.Pilltime;

import java.net.URISyntaxException;
import java.util.List;

public class HeartRateMain {
    public static void main(String[] args) throws URISyntaxException {
        List<HeartRate> heartRates = ReadHeartRateJSON.read();
        List<Pilltime> pillTimes = ReadPillTimeJSON.read();

        System.out.println("read = " + pillTimes);
    }
}
