package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class GroupProcessor {

    List<String> data;

    public GroupProcessor(String file) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        data = Files.readAllLines(Path.of(resource.getPath()));
    }

    protected abstract int processGroup(List<String> groupLines);

    /**
     * Parse group delimited by new line.
     */
    public int processGroups() {
        int result = 0;
        List<String> lines = new ArrayList<>();
        for (int i=0;i<data.size();i++) {
            String line = data.get(i);
            if (line.length() == 0) {
                result += processGroup(lines);
                lines = new ArrayList<>();
            } else {
                lines.add(line);
            }
        }
        result += processGroup(lines);
        return result;
    }
}
