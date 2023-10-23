package no.hartvigsen.io;

import no.hartvigsen.model.Pill;
import no.hartvigsen.model.Pilltime;
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
import java.util.Iterator;
import java.util.List;

public class ReadPillTimeJSON {
    private ReadPillTimeJSON(){}
    public static final String STANDARD_FILNAVN = "vibration_data.json";
    public static List<Pilltime> read() throws URISyntaxException {
        JSONObject jsonObject = new JSONObject(lesFil(STANDARD_FILNAVN));

        List<Pilltime> pilltimes = new ArrayList<>();

        JSONObject j = (JSONObject) jsonObject.get("Pilltimes");
        Iterator<String> keys = j.keys();
        while (keys.hasNext()) {
            String s1 = keys.next();
            JSONArray ja = (JSONArray) j.get(s1);
            for (Object value : ja) {
                JSONObject next = (JSONObject) value;
                JSONObject medisin = next.optJSONObject("medisin");

                Pilltime pilltime = new Pilltime(
                        ZonedDateTime.parse((String) next.get("timestamp")).withZoneSameInstant(ZoneId.of("Europe/Oslo")),
                        new Pill("Sinemet", medisin.getInt("Styrke"))
                );
                pilltimes.add(pilltime);
            }
        }
        return pilltimes;
    }

    private static String lesFil(String filnavn) throws URISyntaxException {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(filnavn).toURI()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
