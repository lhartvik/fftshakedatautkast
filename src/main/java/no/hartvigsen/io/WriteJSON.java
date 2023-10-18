package no.hartvigsen.io;

import no.hartvigsen.model.Pilltime;
import no.hartvigsen.model.VibrationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

public class WriteJSON {
    public void write(List<Pilltime> pilltimes, List<VibrationData> vibrationDataList){

        JSONObject fileJSON = new JSONObject();

        fileJSON.put("Shakerecords", vibrationDataJSON(vibrationDataList));
        fileJSON.put("Pilltimes", pilltimeJSON(pilltimes));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vibration_data.json"))){
            writer.write(fileJSON.toString());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private JSONObject vibrationDataJSON(List<VibrationData> vibrationDataList){
        JSONArray jsonArray = new JSONArray();
        for (VibrationData vibrationData : vibrationDataList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timestamp", vibrationData.getTimestamp().toString());
            jsonObject.put("vibrationdata", vibrationData.getVibrationdata());
            jsonArray.put(jsonObject);
        }
        JSONObject container = new JSONObject();
        container.put("processedVibrationData", jsonArray);
        return container;
    }

    private JSONObject pilltimeJSON(List<Pilltime> pilltimes) {
        JSONObject container = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // Iterate over the list and add each object to the JSON array
        for (Pilltime pilltime : pilltimes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timestamp", pilltime.tid().withZoneSameInstant(ZoneId.of("Z")).toString());
            jsonObject.put("medisin", new JSONObject()
                    .put("navn", pilltime.pill().legemiddel())
                    .put("Styrke", pilltime.pill().styrkeMg()));
            jsonArray.put(jsonObject);
        }

        container.put("correctedData", jsonArray);
        return container;
    }

}
