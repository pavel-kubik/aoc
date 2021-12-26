package cz.pk.adventofcode.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
        URL resource = getClass().getClassLoader().getResource(file);
        try {
            File fileDescription = new File(resource.toURI());
            return Files.readAllLines(Path.of(fileDescription.getPath()));
        } catch (NullPointerException e) {
            throw new RuntimeException(format("File [ %s ] not found.", file), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(format("File [ %s ] not found.", file), e);
        } catch (IOException e) {
            throw new RuntimeException(format("Can't read file [ %s ].", file), e);
        }
    }
}
