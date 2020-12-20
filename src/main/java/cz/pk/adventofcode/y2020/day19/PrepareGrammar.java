package cz.pk.adventofcode.y2020.day19;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PrepareGrammar {

    private List<String> loadFile(String file) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));
        return data;
    }

//    private void saveFile(String file, List<String> lines) throws IOException {
//        URL resource = getClass().getClassLoader().getResource(file);
//        Files.writeString(Path.of(resource.getPath()), lines.toString());
//    }

    public void transformToGrammar(String from) throws IOException {
        List<String> lines = loadFile(from);
        List<String> out = new ArrayList<>();
        for (String line : lines) {
            StringBuilder sb = new StringBuilder();
            String[] rule = line.split(":");
            assert rule.length == 2;
            sb.append("rule").append(rule[0]).append(": ");
            for (String or : rule[1].trim().split("\\|")) {
                for (String match: or.trim().split(" ")) {
                    if (match.startsWith("\"")) {
                        sb.append(match.replace('"', '\''));
                    } else {
                        sb.append(" rule").append(match);
                    }
                }
                sb.append(" |");
            }
            sb.setCharAt(sb.length()-1, ';');
            System.out.println(sb.toString());
            out.add(sb.toString());
        }

    }

    public static void main(String[] args) throws IOException {
        //new PrepareGrammar().transformToGrammar("grammar_test.txt");
        new PrepareGrammar().transformToGrammar("2020/grammar.txt");
    }
}
