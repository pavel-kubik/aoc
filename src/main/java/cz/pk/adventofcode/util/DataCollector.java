package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public abstract class DataCollector<TYPE> {

    List<TYPE> result = new ArrayList<>();
    List<String> data;
    protected int lineNumber;

    public DataCollector(String file) {
        data = FileReadUtil.readAllLines(file);
    }

    protected abstract TYPE processLine(String line);

    public List<TYPE> process() {
        for (int i = 0; i < data.size(); i++) {
            lineNumber = i;
            TYPE item = processLine(data.get(i));
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }
}
