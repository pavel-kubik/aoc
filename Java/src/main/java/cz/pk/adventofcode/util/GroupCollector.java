package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class GroupCollector<T> {

    List<String> data;

    public GroupCollector(String file) {
        data = FileReadUtil.readAllLines(file);
    }

    protected abstract T processGroup(List<String> groupLines);

    /**
     * Parse group delimited by new line.
     */
    public List<T> processGroups() {
        List<T> result = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            if (line.length() == 0) {
                result.add(processGroup(lines));
                lines = new ArrayList<>();
            } else {
                lines.add(line);
            }
        }
        result.add(processGroup(lines));
        return result;
    }
}
