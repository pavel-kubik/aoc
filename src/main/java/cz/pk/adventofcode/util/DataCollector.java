package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class DataCollector<TYPE> {

    List<TYPE> result = new ArrayList<>();
    List<String> data;

    public DataCollector(String file) {
        try {
            URL resource = getClass().getClassLoader().getResource(file);
            data = Files.readAllLines(Path.of(resource.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file " + file, e);
        }
    }

    protected abstract TYPE processLine(String line);

    /**
     * Parse group delimited by new line.
     */
    public List<TYPE> process() {
        for (int i=0;i<data.size();i++) {
            result.add(processLine(data.get(i)));
        }
        return result;
    }
}
