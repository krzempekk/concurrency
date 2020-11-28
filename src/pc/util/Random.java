package pc.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Random {
    private ArrayList<Integer> numbers;

    public Random(String fileName) {
        this.numbers = new ArrayList<>();
        try {
            String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getPath();
            Stream<String> linesStream = Files.lines(Paths.get(filePath));

            for (String line : linesStream.collect(Collectors.toList())) {
                numbers.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandomNumber() {
        return this.numbers.remove(0);
    }

    public boolean hasMoreNumbers() {
        return !this.numbers.isEmpty();
    }
}
