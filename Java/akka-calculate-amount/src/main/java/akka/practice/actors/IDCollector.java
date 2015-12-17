package akka.practice.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class IDCollector extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final String dataSeparator;
    private final String filePath;

    public IDCollector(String filePath, String dataSeparator) {
        this.filePath = filePath;
        this.dataSeparator = dataSeparator;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == MessageEnum.COLLECT_ID) {
            log.info("Collecting unique ids from file...");
            final Set<Integer> ids = collectIdsFromFile(filePath);
            log.info("Collecting is finished");
            getSender().tell(ids, getSelf());
        } else {
            unhandled(message);
        }
    }

    private Set<Integer> collectIdsFromFile(String fileName) throws IOException {
        final Set<Integer> collectedIdSet = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splittedRow = line.split(dataSeparator);
                if (splittedRow.length > 0) {
                    collectedIdSet.add(Integer.valueOf(splittedRow[0]));
                }
            }
        }
        return collectedIdSet;
    }
}
