package akka.practice.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class FileReaderTask implements Callable<Map<Integer, Double>> {

    private String fileName;
    private Set<Integer> ids;
    private String dataSeparator;
    private String name;

    public FileReaderTask(String name, String fileName, Set<Integer> ids, String dataSeparator) {
        this.name = name;
        this.fileName = fileName;
        this.ids = ids;
        this.dataSeparator = dataSeparator;
    }

    @Override
    public Map<Integer, Double> call() throws Exception {
        final Map<Integer, Double> result = new HashMap<>();
        for (Integer id : ids) {
            result.put(id, 0.0);
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splittedRow = line.split(dataSeparator);
                if (splittedRow.length > 0) {
                    int currentId = Integer.valueOf(splittedRow[0]);
                    if (ids.contains(currentId)) {
                        result.put(currentId, result.get(currentId) + Double.valueOf(splittedRow[1]));
                    }
                }
            }
        }
        return result;
    }
}
