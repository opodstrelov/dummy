package akka.practice.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class Utils {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Utils() {
    }

    /**
     * Splits the original Set into smaller parts.
     *
     * @param original original set for splitting
     * @param count    number of future smaller sets
     * @return List of sets which were created after splitting
     */
    public static <T> List<Set<T>> split(Set<T> original, int count) {
        List<Set<T>> splittedResult = new ArrayList<>(count);
        Iterator<T> it = original.iterator();
        final int eachSetSize = original.size() / count + 1;
        for (int i = 0; i < count; i++) {

            Set<T> set = new HashSet<>(eachSetSize);
            splittedResult.add(set);

            for (int j = 0; j < eachSetSize && it.hasNext(); j++) {
                set.add(it.next());
            }
        }
        return splittedResult;
    }

    public static void writeMapToFile(Map inputMap, String filePath) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            final Set entrySet = inputMap.entrySet();
            for (Object entry : entrySet) {
                bufferedWriter.write(entry.toString() + LINE_SEPARATOR);
            }
        }
    }

}
