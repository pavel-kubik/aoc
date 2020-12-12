package cz.pk.adventofcode;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import cz.pk.adventofcode.y2020.day1.Day1;
import cz.pk.adventofcode.y2020.day10.Day10;
import cz.pk.adventofcode.y2020.day11.Day11;
import cz.pk.adventofcode.y2020.day2.Day2;
import cz.pk.adventofcode.y2020.day3.Day3;
import cz.pk.adventofcode.y2020.day4.Day4;
import cz.pk.adventofcode.y2020.day5.Day5;
import cz.pk.adventofcode.y2020.day6.Day6;
import cz.pk.adventofcode.y2020.day7.Day7;
import cz.pk.adventofcode.y2020.day8.Day8;
import cz.pk.adventofcode.y2020.day9.Day9;

@SpringBootApplication
public class AdventOfCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdventOfCodeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Day1.main(args);
            Day2.main(args);
            Day3.main(args);
            Day4.main(args);
            Day5.main(args);
            Day6.main(args);
            Day7.main(args);
            Day8.main(args);
            Day9.main(args);
            Day10.main(args);
            Day11.main(args);
        };
    }

}
