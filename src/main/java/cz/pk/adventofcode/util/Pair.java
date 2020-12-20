package cz.pk.adventofcode.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pair<T> {
    public T a;
    public T b;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[").append(a).append(",").append(b).append("]").toString();
    }
}
