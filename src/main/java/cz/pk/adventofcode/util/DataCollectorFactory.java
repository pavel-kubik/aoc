package cz.pk.adventofcode.util;

import java.util.List;
import java.util.function.Function;

public class DataCollectorFactory {

    public static <T> List<T> collectData(String file, Function<String, T> lineProcessor) {
        return new DataCollector<T>(file) {
            @Override
            protected T processLine(String line) {
                return lineProcessor.apply(line);
            }
        }.process();
    }
}
