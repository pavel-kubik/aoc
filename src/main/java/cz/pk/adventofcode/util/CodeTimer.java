package cz.pk.adventofcode.util;

import java.util.HashMap;
import java.util.Map;

public class CodeTimer {
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
            System.out.println("Key " + entry.getKey() + ": " + entry.getValue());
        }
    }

}
