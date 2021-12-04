package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

public class FileReadUtil {

    private static final FileReadUtil fileReadUtil = new FileReadUtil();

    public static List<String> readAllLines(String file) {
        return fileReadUtil.readAllLinesInternal(file);
    }

    List<String> readAllLinesInternal(String file) {
        try {
            URL resource = getClass().getClassLoader().getResource(file);
            if (resource == null) {
                throw new RuntimeException(format("Can't find file at [ %s ].", file));
            }
            return Files.readAllLines(Path.of(resource.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(format("Can't read file [ %s ].", file), e);
        }
    }
}
