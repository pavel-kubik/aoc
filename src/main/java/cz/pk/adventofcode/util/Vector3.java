package cz.pk.adventofcode.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector3<T> {
    public T x;
    public T y;
    public T z;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[").append(x).append(",").append(y).append(",").append(z).append("]").toString();
    }

    public Vector3(T[] v3) {
        assert v3.length == 3;
        this.x = v3[0];
        this.y = v3[1];
        this.z = v3[2];
    }

    public <TYPE> Vector3<TYPE> plus(Vector3<TYPE> vectorUnknown) {
        if (Integer.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector3<Integer> vectorInt = (Vector3<Integer>) vectorUnknown;
            int x = Integer.parseInt(this.x.toString());
            int y = Integer.parseInt(this.y.toString());
            int z = Integer.parseInt(this.z.toString());
            return (Vector3<TYPE>)new Vector3<Integer>(x + vectorInt.x, y + vectorInt.y, z + vectorInt.z);
        } else if (Long.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector3<Long> vectorLong = (Vector3<Long>) vectorUnknown;
            long x = Long.parseLong(this.x.toString());
            long y = Long.parseLong(this.y.toString());
            long z = Long.parseLong(this.z.toString());
            return (Vector3<TYPE>)new Vector3<Long>(x + vectorLong.x, y + vectorLong.y, z + vectorLong.z);
        } else if (Double.class.isAssignableFrom(vectorUnknown.x.getClass())) {
            Vector3<Double> vectorDouble = (Vector3<Double>) vectorUnknown;
            double x = Double.parseDouble(this.x.toString());
            double y = Double.parseDouble(this.y.toString());
            double z = Double.parseDouble(this.z.toString());
            return (Vector3<TYPE>)new Vector3<Double>(x + vectorDouble.x, y + vectorDouble.y, z + vectorDouble.z);
        } else {
            throw new RuntimeException("Unsupported type");
        }
    }

}
