package cz.pk.adventofcode.util;

public class StringCollector extends DataCollector<String> {
    public StringCollector(String file) {
        super(file);
    }

    @Override
    protected String processLine(String line) {
        return line;
    }
}
