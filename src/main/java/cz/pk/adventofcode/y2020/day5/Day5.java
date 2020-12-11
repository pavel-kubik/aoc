package cz.pk.adventofcode.y2020.day5;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Day5 {

    int findNumber(int startMin, int startMax, String code, char low, char high) {
        int min = startMin;
        int max = startMax;
        for (int i=0;i<code.length();i++) {
            int middle = (min + max) / 2;
            //System.out.println(format("Min %d, max %d, middle %d", min, max, middle));
            if (code.charAt(i) == low) {
                max = middle;
            } else if (code.charAt(i) == high) {
                min = middle + 1;
            } else {
                assert false;
            }
        }
        assert min == max;
        return min;
    }

    int findSeat(String code) {
        assert code.length() == 10;
        int row = findNumber(0, 127, code.substring(0, 7), 'F', 'B');
        int column = findNumber(0, 7, code.substring(7, 10), 'L', 'R');
        int seatId = row*8 + column;
        System.out.println("Row: " + row + ", column: " + column + ", seatID: " + seatId);
        return seatId;
    }

    int findMaxSeatId() throws IOException {
        URL resource = getClass().getClassLoader().getResource("2020/day5.txt");
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));
        int max = -1;
        for (int i=0; i<data.size(); i++) {
            int currentSeatId = findSeat(data.get(i));
            if (currentSeatId > max) {
                max = currentSeatId;
            }
        }
        return max;
    }

    int findMySeatId() throws IOException {
        URL resource = getClass().getClassLoader().getResource("2020/day5.txt");
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));
        List<Integer> seats = new ArrayList();
        for (int i=0;i<1016;i++) {
            seats.add(i);
        }
        for (int i=0; i<data.size(); i++) {
            int currentSeatId = findSeat(data.get(i));
            seats.remove((Integer)currentSeatId);
        }
        for(Integer seat : seats) {
            System.out.println(seat);   // 548
        }
        return 0;
    }

    public static void main(String[] args) throws IOException {
        int seatId = new Day5().findMySeatId();
        System.out.println(seatId);
    }
}
