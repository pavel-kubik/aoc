package cz.pk.adventofcode.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pair<T> {
    public T first;
    public T second;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[").append(first).append(",").append(second).append("]").toString();
    }
}
