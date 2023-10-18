package no.hartvigsen.used;

import no.hartvigsen.io.ReadJSON;
import no.hartvigsen.io.WriteJSON;
import no.hartvigsen.model.Pill;
import no.hartvigsen.model.Pilltime;
import no.hartvigsen.model.ReadJSONResults;
import no.hartvigsen.model.VibrationData;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

public class MainKonverterData {
    private static final ZonedDateTime BEGYNTE_MED_DOBBEL = LocalDate.of(2023, 5, 27).atStartOfDay(ZoneId.systemDefault());
    private static final ZonedDateTime FIKK_HALVTABLETTER = LocalDate.of(2023, 6, 19).atTime(12, 0).atZone(ZoneId.systemDefault());
    private static final ZonedDateTime SNAKKET_MED_NEVROLOG_TLF = LocalDate.of(2023, 8, 10).atStartOfDay(ZoneId.systemDefault());
    public static void main(String[] args) throws URISyntaxException {

        ReadJSONResults readJSONResults = new ReadJSON().read("parkinsontracker-eb259-default-rtdb-export (2).json");

        List<Pilltime> pilltimes = readJSONResults.pilltimes();

        Pilltime prev = null;

        List<Pilltime> virkningstidspunkterOgUtlededeStyrker = new ArrayList<>();


        for (Pilltime current : pilltimes.stream().sorted(Comparator.comparing(Pilltime::tid)).toList()) {
            if (isNull(prev)) {
                add(virkningstidspunkterOgUtlededeStyrker, current, 100);
            } else {
                if (current.tid().isBefore(ChronoZonedDateTime.from(BEGYNTE_MED_DOBBEL))) {
                    add(virkningstidspunkterOgUtlededeStyrker, current, 100);
                    System.out.println("Før dobling = " + current);
                } else if (current.tid().isBefore(ChronoZonedDateTime.from(SNAKKET_MED_NEVROLOG_TLF))) {
                    Duration tidSidenForrige = Duration.between(prev.tid(), current.tid());
                    if (tidSidenForrige.toMinutes() < 5) {
                        virkningstidspunkterOgUtlededeStyrker.remove(virkningstidspunkterOgUtlededeStyrker.stream().reduce((a, b) -> b).orElse(null));
                        add(virkningstidspunkterOgUtlededeStyrker, current, 200);
                    } else {
                        virkningstidspunkterOgUtlededeStyrker.add(current);
                    }
                } else /* Etter at jeg begynte med 200/150/150/150 */ {
                    if (
                            (prev.tid().getDayOfYear() != current.tid().getDayOfYear() && current.tid().getHour() > 5)
                                    ||
                                    (prev.tid().getDayOfYear() == current.tid().getDayOfYear() && prev.tid().getHour() < 5 && current.tid().getHour() > 5)
                    )
                        // første for dagen
                        add(virkningstidspunkterOgUtlededeStyrker, current, 200);
                    else // ikke første for dagen
                        add(virkningstidspunkterOgUtlededeStyrker, current, 150);
                }
            }
            prev = current;
        }

        List<Pilltime> mod_juli = virkningstidspunkterOgUtlededeStyrker.stream()
                .map(p -> p.tid().isAfter(FIKK_HALVTABLETTER)
                        && p.tid().isBefore(SNAKKET_MED_NEVROLOG_TLF)
                        && p.pill().styrkeMg() == 100
                        ?
                        new Pilltime(p.tid(), new Pill(p.pill().legemiddel(), 150)) : p)
                .toList();

        List<VibrationData> vibrationDataList = readJSONResults.vibrationDataList().stream().sorted(Comparator.comparing(VibrationData::getTimestamp)).toList();

        new WriteJSON().write(mod_juli, vibrationDataList);
    }

    private static void add(List<Pilltime> virkningstidspunkterOgUtlededeStyrker, Pilltime pilltime, int styrkeMg) {
        pilltime = new Pilltime(pilltime.tid(), new Pill(pilltime.pill().legemiddel(), styrkeMg));
        virkningstidspunkterOgUtlededeStyrker.add(pilltime);
    }
}


