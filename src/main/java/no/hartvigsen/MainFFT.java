package no.hartvigsen;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalDouble;

public class MainFFT {
    public static void main(String[] args) {

        ReadJSONResults readJSONResults = new ReadJSON().read();

        List<BigDecimal> vibrationdata = readJSONResults.vibrationDataList().stream().findAny().get().getVibrationdata();

        double average = vibrationdata.stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getAverage();

        OptionalDouble average1 = vibrationdata.stream().mapToDouble(BigDecimal::doubleValue).map(d -> Math.abs(d - average)).average();

        System.out.println("average1 = " + average1);
    }
}


