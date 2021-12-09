package cz.pk.adventofcode.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vector2<T> {
    public T x;
    public T y;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[").append(x).append(",").append(y).append("]").toString();
    }

    public <TYPE> Vector2<TYPE> plus(Vector2<TYPE> vectorUnknown) {
        if (Integer.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector2<Integer> vectorInt = (Vector2<Integer>) vectorUnknown;
            int x = Integer.parseInt(this.x.toString());
            int y = Integer.parseInt(this.y.toString());
            return (Vector2<TYPE>)new Vector2<Integer>(x + vectorInt.x, y + vectorInt.y);
        } else if (Long.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector2<Long> vectorLong = (Vector2<Long>) vectorUnknown;
            long x = Long.parseLong(this.x.toString());
            long y = Long.parseLong(this.y.toString());
            return (Vector2<TYPE>)new Vector2<Long>(x + vectorLong.x, y + vectorLong.y);
        } else if (Double.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector2<Double> vectorDouble = (Vector2<Double>) vectorUnknown;
            double x = Double.parseDouble(this.x.toString());
            double y = Double.parseDouble(this.y.toString());
            return (Vector2<TYPE>)new Vector2<Double>(x + vectorDouble.x, y + vectorDouble.y);
        } else {
            throw new RuntimeException("Unsupported type");
        }
    }
}
