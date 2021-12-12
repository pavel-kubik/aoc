package cz.pk.adventofcode.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CodeTimer {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSSSSS");

    Map<String, Long> durations = new HashMap<>();

    private long lastStart;
    private String lastKey = null;

    public void start(String codeKey) {
        if (lastKey != null) {
            long duration = System.nanoTime() - lastStart;
            durations.compute(lastKey, (key, val) -> duration + ((val != null) ? val : 0));
        }
        lastStart = System.nanoTime();
        lastKey = codeKey;
    }

    public void stop() {
        if (lastKey != null) {
            long duration = System.nanoTime() - lastStart;
            durations.compute(lastKey, (key, val) -> duration + ((val != null) ? val : 0));
        }
    }

    public void log() {
        for (Map.Entry<String, Long> entry : durations.entrySet()) {
            System.out.printf("%s %s\n",
                    Duration.of(entry.getValue(), ChronoUnit.NANOS).toString(),
                    entry.getKey());
        }
    }

}
