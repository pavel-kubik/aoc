package cz.pk.adventofcode.util;

public class StringUtil {

    public static String removeSuffix(String value, String suffix) {
        return value.substring(0, value.length() - suffix.length());
    }
}
