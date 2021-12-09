package cz.pk.adventofcode;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import cz.pk.adventofcode.y2021.day1.Day1;
import cz.pk.adventofcode.y2021.day2.Day2;
import cz.pk.adventofcode.y2021.day3.Day3;
import cz.pk.adventofcode.y2021.day4.Day4;
import cz.pk.adventofcode.y2021.day5.Day5;
import cz.pk.adventofcode.y2021.day6.Day6;

@SpringBootApplication
public class AdventOfCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdventOfCodeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            cz.pk.adventofcode.y2020.day1.Day1.main(args);
            cz.pk.adventofcode.y2020.day2.Day2.main(args);
            cz.pk.adventofcode.y2020.day3.Day3.main(args);
            cz.pk.adventofcode.y2020.day4.Day4.main(args);
            cz.pk.adventofcode.y2020.day5.Day5.main(args);
            cz.pk.adventofcode.y2020.day6.Day6.main(args);
            cz.pk.adventofcode.y2020.day7.Day7.main(args);
            cz.pk.adventofcode.y2020.day8.Day8.main(args);
            cz.pk.adventofcode.y2020.day9.Day9.main(args);
            cz.pk.adventofcode.y2020.day10.Day10.main(args);
            cz.pk.adventofcode.y2020.day11.Day11.main(args);
            cz.pk.adventofcode.y2020.day12.Day12.main(args);
            cz.pk.adventofcode.y2020.day13.Day13.main(args);
            cz.pk.adventofcode.y2020.day14.Day14.main(args);
            cz.pk.adventofcode.y2020.day15.Day15.main(args);
            cz.pk.adventofcode.y2020.day16.Day16.main(args);
            cz.pk.adventofcode.y2020.day17.Day17.main(args);
            cz.pk.adventofcode.y2020.day18.Day18.main(args);
            //cz.pk.adventofcode.y2020.day19.Day19.main(args);  // takes too long :-)
            cz.pk.adventofcode.y2020.day20.Day20.main(args);
            cz.pk.adventofcode.y2020.day21.Day21.main(args);
            cz.pk.adventofcode.y2020.day22.Day22.main(args);    //TODO finish 2nd part
            //cz.pk.adventofcode.y2020.day23.Day23.main(args);  //TODO finish
            //cz.pk.adventofcode.y2020.day24.Day24.main(args);  //TODO finish
            //cz.pk.adventofcode.y2020.day25.Day25.main(args);  //TODO finish
            Day1.main(args);
            Day2.main(args);
            Day3.main(args);
            Day4.main(args);
            Day5.main(args);
            Day6.main(args);
        };
    }

}
