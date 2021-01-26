import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MarkovChains {
    private static Random r = new Random();


    private static String markov(String filePath, int keySize, int outputSize) throws IOException {
        if (keySize < 1) throw new IllegalArgumentException("Key size can't be less than 1"); //error checker
        Path path = Paths.get(filePath);//path to file name
        byte[] bytes = Files.readAllBytes(path); //bytes are all the charcters
        String[] words = new String(bytes).trim().split(" "); //everytime a space is hit, a new array entry is made
        if (outputSize < keySize || outputSize >= words.length) { //the output cannnot be smaller than the key, or bigger/equal to the file
            throw new IllegalArgumentException("Output size is out of range");
        }

        Map<String, List<String>> d = new HashMap<>();//declare array

        for (int i = 0; i < words.length - keySize; ++i) {
            StringBuilder key = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + keySize; ++j) {
                key.append(' ').append(words[j]);//puts a space between words
            }
            String value = (i + keySize < words.length) ? words[i + keySize] : "";
            if (!d.containsKey(key.toString())) {
                ArrayList<String> aList = new ArrayList<>();
                aList.add(value);
                d.put(key.toString(), aList);
            } else {
                d.get(key.toString()).add(value);
            }
        }

        int n = 0;
        int rn = r.nextInt(d.size());
        String prefix = (String) d.keySet().toArray()[rn];
        List<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));

        while (true) {
            System.out.println("first pre"+prefix);
            List<String> suffix = d.get(prefix);
            System.out.println("second pre" + prefix);
            if (suffix.size() == 1) {
                if (Objects.equals(suffix.get(0), "")){
                    System.out.println("emptry string");
                    return output.stream().reduce("", (a, b) -> a + " " + b);}
                output.add(suffix.get(0));
            } else {
                rn = r.nextInt(suffix.size());
                output.add(suffix.get(rn));
            }
            if (output.size() >= outputSize) return output.stream().limit(outputSize).reduce("", (a, b) -> a + " " + b);
            n++;
            prefix = output.stream().skip(n).limit(keySize).reduce("", (a, b) -> a + " " + b).trim();

        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(markov("starwars.txt", 3, 200));
    }
}





