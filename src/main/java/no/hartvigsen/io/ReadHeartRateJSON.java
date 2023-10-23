package no.hartvigsen.io;

import no.hartvigsen.model.HeartRate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadHeartRateJSON {
    public static final String EKSEMPEL_FILNAVN = "oura_heart-rate_2023-10-13T06-57-27.json";

    private ReadHeartRateJSON() {
    }

    public static List<HeartRate> read() throws URISyntaxException {
        List<HeartRate> heartRates = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(lesFil(EKSEMPEL_FILNAVN));

        JSONArray heartRateArray = jsonObject.getJSONArray("heart_rate");
        for (int i = 0; i < heartRateArray.length(); i++) {
            JSONObject heartRateObject = heartRateArray.getJSONObject(i);
            ZonedDateTime tid = ZonedDateTime
                    .parse(heartRateObject.getString("timestamp"))
                    .withZoneSameInstant(ZoneId.of("Europe/Oslo"));
            heartRates.add(new HeartRate(
                            tid,
                            heartRateObject.getInt("bpm"),
                            heartRateObject.optInt("hrv")
                    )
            );
        }
        return heartRates;
    }

    private static String lesFil(String filnavn) throws URISyntaxException {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(filnavn).toURI()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
