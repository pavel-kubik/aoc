package cz.pk.adventofcode.y2019.day1;

import java.util.List;

import cz.pk.adventofcode.util.LongCollector;

public class Day1 {

    public static long countFuel(long mass) {
        return mass / 3 - 2;
    }

    public long countAllMass(String file) {
        List<Long> masses = new LongCollector(file).process();
        long out = 0;
        for (Long mass : masses) {
            out += countFuel(mass);
        }
        return out;
    }

    public static long countFuelWithFuelWeight(long mass) {
        long out = 0;
        long lastFuel = mass;
        do {
            lastFuel = countFuel(lastFuel);
            if (lastFuel <= 0) {
                break;
            }
            out += lastFuel;
        } while (true);
        return out;
    }

    public long countAllMassWithFuelWeight(String file) {
        List<Long> masses = new LongCollector(file).process();
        long out = 0;
        for (Long mass : masses) {
            out += countFuelWithFuelWeight(mass);
        }
        return out;
    }

    public static void main(String[] args) {
        assert countFuel(12) == 2;
        assert countFuel(14) == 2;
        assert countFuel(1969) == 654;
        assert countFuel(100756) == 33583;

        final long totalMass = new Day1().countAllMass("2019/day1.txt");
        System.out.println("Total mass: " + totalMass);
        assert totalMass == 3297626;

        assert countFuelWithFuelWeight(14) == 2;
        assert countFuelWithFuelWeight(1969) == 966;
        assert countFuelWithFuelWeight(100756) == 50346;

        final long totalMassWithFuelWeight = new Day1().countAllMassWithFuelWeight("2019/day1.txt");
        System.out.println("Total mass with fuel weight: " + totalMassWithFuelWeight);
        assert totalMassWithFuelWeight == 4943578;
        // not
        // 4943559
        // 4943538
    }
}
