package cz.pk.adventofcode.util;

import org.junit.jupiter.api.Test;

public class MatrixTest {

    @Test
    public void simpleConvolution() {
        Matrix<Integer> output = Matrix.instance(new Integer[][]{
                {1, 1 ,1},
                {1, 1 ,1},
                {1, 1 ,1}
        }).convolution(
                Matrix.instance(new Integer[][]{
                        {1, 1, 1},
                        {1, 0, 1},
                        {1, 1, 1}}),
                (in_i_j, c_i_j) -> in_i_j*c_i_j,
                0, (sum, windowOpResult) -> sum + windowOpResult);
        System.out.println(output);
    }

}
