package cz.pk.adventofcode.util;

public class LongCollector extends DataCollector<Long> {
    public LongCollector(String file) {
        super(file);
    }

    @Override
    protected Long processLine(String line) {
        return Long.parseLong(line);
    }
}
