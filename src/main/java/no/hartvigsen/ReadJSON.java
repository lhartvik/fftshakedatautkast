package no.hartvigsen;

import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.VibrationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
public static final String FILNAVN = "parkinsontracker-export.json";
    String content = lesFil();

    public ReadJSONResults read() {
        JSONObject jsonObject = new JSONObject(content);

        List<VibrationData> vibrationDataList = new ArrayList<>();
        List<ZonedDateTime> pilltimes = new ArrayList<>();

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
                        pilltimes.add(ZonedDateTime.parse((String) next.get("timestamp")).withZoneSameInstant(norwegianTimeZone));
                    }
                }
            }
        }
        return new ReadJSONResults(vibrationDataList, pilltimes);
    }

    private String lesFil() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(FILNAVN).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
