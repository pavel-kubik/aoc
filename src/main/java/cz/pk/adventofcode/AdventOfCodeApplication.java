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
        };
    }

}
