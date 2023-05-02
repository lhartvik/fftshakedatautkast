package no.hartvigsen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadJSON {
    public ReadJSONResults read() {
        String content = lesFil("parkinsontracker-export.json");
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
                Iterator<Object> iterator = ja.iterator();
                while (iterator.hasNext()) {
                    JSONObject next = (JSONObject) iterator.next();
                    if (s.equals("Shakerecords")) {
                        JSONArray array = (JSONArray) next.get("vibrationData");
                        VibrationData v = new VibrationData((String) next.get("timestamp"));
                        Iterator<Object> iterator1 = array.iterator();
                        while (iterator1.hasNext()) {
                            v.addVibrationdata((BigDecimal) iterator1.next());
                        }
                        vibrationDataList.add(v);
                    } else if (s.equals("Pilltimes")) {
                        pilltimes.add(ZonedDateTime.parse((String) next.get("timestamp")));
                    }
                }
            }
        }
        return new ReadJSONResults(vibrationDataList, pilltimes);
    }

    private String lesFil(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(filename).toURI())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
