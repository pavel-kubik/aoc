package cz.pk.adventofcode.y2020.day2;

import cz.pk.adventofcode.util.FileReadUtil;
import cz.pk.adventofcode.y2020.day1.Day1;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

public class Day2 {

    public static void main(String[] args) throws IOException {
        System.out.println(Day2.class);
        new Day2().checkPasswords();
    }

    private boolean isValid(int min, int max, char sign, String password) {
        int numOfChar = 0;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) == sign) {
                numOfChar++;
            }
        }
        return numOfChar >= min && numOfChar <= max;
    }

    private boolean isValid2(int min, int max, char sign, String password) {
        int numOfChar = 0;
        char signA = password.charAt(min - 1);
        char signB = password.charAt(max - 1);
        return signA == sign && signB != sign || signA != sign && signB == sign;
    }

    public void checkPasswords() throws IOException {
        List<String> data = FileReadUtil.readAllLines("2020/day2.txt");
        int valid = 0;
        for (String line : data) {
            //num-num char: string
            String[] parts = line.split(" ");
            assert parts.length == 3;

            String[] nums = parts[0].split("-");
            assert nums.length == 2;
            int min = Integer.valueOf(nums[0]);
            int max = Integer.valueOf(nums[1]);

            char sign = parts[1].charAt(0);

            String password = parts[2];

            System.out.println(format("%d-%d %c: %s", min, max, sign, password));

            int numOfChar = 0;
            for (int i = 0; i < password.length(); i++) {
                if (password.charAt(i) == sign) {
                    numOfChar++;
                }
            }

            if (isValid2(min, max, sign, password)) {
                System.out.println("..ok");
                valid++;
            } else {
                System.out.println("..ee");
            }
        }
        System.out.println("Valid passwords: " + valid);
    }
}
