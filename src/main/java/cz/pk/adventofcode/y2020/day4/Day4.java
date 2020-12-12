package cz.pk.adventofcode.y2020.day4;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day4 {

    String[] mandatoryFiedls = {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};

    public static void main(String[] args) throws IOException {
        System.out.println("Valid: " + new Day4().countValid());
    }

    boolean checkMandatoryFields(HashMap<String, String> fields) {
        return Arrays.stream(mandatoryFiedls)
                .filter(field -> fields.containsKey(field))
                .count() == mandatoryFiedls.length;
    }

    boolean checkPassport(HashMap<String, String> fields) {
        boolean isValid = checkMandatoryFields(fields)
                && checkBirthYear(fields.get("byr"))
                && checkIssueYear(fields.get("iyr"))
                && checkExpirationYear(fields.get("eyr"))
                && checkHeight(fields.get("hgt"))
                && checkHairColor(fields.get("hcl"))
                && checkEyeColor(fields.get("ecl"))
                && checkPassportID(fields.get("pid"));
        if (isValid) {
            System.out.println("Fields:" + fields);
        }
        return isValid;
    }

    boolean checkPassportID(String passportID) {
        if (passportID.length() != 9) {
            return false;
        }
        boolean number = true;
        for (int i = 0; i < passportID.length(); i++) {
            char value = passportID.charAt(i);
            number = number && (value >= '0' && value <= '9');
        }
        return number;
    }

    boolean checkEyeColor(String eyeColor) {
        return List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(eyeColor);
    }

    boolean checkHairColor(String hairColor) {
        if (hairColor.length() != 7) {
            return false;
        }
        if (hairColor.charAt(0) != '#') {
            return false;
        }
        boolean hexNumber = true;
        for (int i = 1; i < hairColor.length(); i++) {
            char value = hairColor.charAt(i);
            hexNumber = hexNumber && (value >= 'a' && value <= 'f' ||
                    value >= '0' && value <= '9');
        }
        return hexNumber;
    }

    boolean checkHeight(String height) {
        if (height.endsWith("cm")) {
            String heightNumber = height.substring(0, height.length() - 2);
            return checkNumber(heightNumber, 150, 193);
        } else if (height.endsWith("in")) {
            String heightNumber = height.substring(0, height.length() - 2);
            return checkNumber(heightNumber, 59, 76);
        } else {
            return false;
        }
    }

    boolean checkBirthYear(String birthYear) {
        return checkNumber(birthYear, 1920, 2002);
    }

    boolean checkIssueYear(String issueYear) {
        return checkNumber(issueYear, 2010, 2020);
    }

    boolean checkExpirationYear(String expirationYear) {
        return checkNumber(expirationYear, 2020, 2030);
    }

    boolean checkNumber(String number, int min, int max) {
        //        if (number.length() != 4) {
        //            return false;
        //        }
        try {
            int value = Integer.parseInt(number);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    int countValid() throws IOException {
        URL resource = getClass().getClassLoader().getResource("2020/day4.txt");
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));

        int valid = 0;
        HashMap<String, String> fields = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            if (line.length() == 0) {
                if (checkPassport(fields)) {
                    valid++;
                }
                fields.clear();
            } else {
                String[] pairs = line.split(" ");
                for (int j = 0; j < pairs.length; j++) {
                    String[] pair = pairs[j].split(":");
                    assert pair.length == 2;
                    fields.put(pair[0], pair[1]);
                }
            }
        }
        if (checkPassport(fields)) {
            valid++;
        }
        return valid;
    }
}
