package no.hartvigsen.model;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record Pilltime(ZonedDateTime tid, Pill pill) {
    @Override
    public String toString() {
        return String.format("%s %dmg %2d.%s %02d:%02d",
                pill.legemiddel(),
                pill.styrkeMg(),
                tid.getDayOfMonth(), tid.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                tid.getHour(), tid.getMinute());
    }
}
