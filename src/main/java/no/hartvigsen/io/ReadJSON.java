package no.hartvigsen.io;

import no.hartvigsen.model.Pill;
import no.hartvigsen.model.Pilltime;
import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.VibrationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadJSON {
    ZoneId norwegianTimeZone = ZoneId.of("Europe/Oslo");
    public static final String STANDARD_FILNAVN = "vibration_data.json";

    public ReadJSONResults read() throws URISyntaxException {
        return read(STANDARD_FILNAVN);
    }

    public ReadJSONResults read(String filnavn) throws URISyntaxException {
        JSONObject jsonObject = new JSONObject(lesFil(filnavn));

        List<VibrationData> vibrationDataList = new ArrayList<>();
        List<Pilltime> pilltimes = new ArrayList<>();

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String s = keys.next();
            JSONObject j = (JSONObject) jsonObject.get(s);
            Iterator<String> keys1 = j.keys();
            while (keys1.hasNext()) {
                String s1 = keys1.next();
                JSONArray ja = (JSONArray) j.get(s1);
                for (Object value : ja) {
                    JSONObject next = (JSONObject) value;
                    if (s.equals("Shakerecords")) {
                        JSONArray array = (JSONArray) next.get("vibrationData");
                        VibrationData v = new VibrationData((String) next.get("timestamp"));
                        for (Object o : array) {
                            v.addVibrationdata((BigDecimal) o);
                        }
                        vibrationDataList.add(v);
                    } else if (s.equals("Pilltimes")) {
                        JSONObject medisin = next.optJSONObject("medisin");

                        Pilltime pilltime = new Pilltime(ZonedDateTime.parse((String) next.get("timestamp")).withZoneSameInstant(norwegianTimeZone), parseMedisin(medisin));
                        pilltimes.add(pilltime);
                    }
                }
            }
        }
        return new ReadJSONResults(vibrationDataList, pilltimes);
    }

    private Pill parseMedisin(JSONObject medisin) {
        if ((medisin != null)) {
            Object styrke = medisin.get("Styrke");
            String styrke3siffer = styrke.toString().substring(0, 3);
            int styrkeMg = Integer.parseInt(styrke3siffer);
            return new Pill("Sinemet", styrkeMg);
        } else {
            return new Pill("Sinemet", 100);
        }
    }

    private String lesFil(String filnavn) throws URISyntaxException {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(filnavn).toURI()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
