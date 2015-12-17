package akka.practice.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.practice.tasks.FileReaderTask;
import akka.practice.util.Utils;
import scala.concurrent.Future;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static akka.dispatch.Futures.future;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class TotalAmountCalculator extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final String sourceFilePath;
    private final String dataSeparator;
    private final int futuresNumber;
    private final ActorRef idCollector;
    private final ActorRef resultAmountAccumulator;
    private final ActorRef mapFileWriter;

    public TotalAmountCalculator(String sourceFilePath, String dataSeparator, int futuresNumber, String destinationFilePath) {
        this.sourceFilePath = sourceFilePath;
        this.dataSeparator = dataSeparator;
        this.futuresNumber = futuresNumber;
        idCollector = getContext().actorOf(Props.create(IDCollector.class, sourceFilePath, dataSeparator), "idCollector");
        resultAmountAccumulator = getContext().actorOf(Props.create(ResultAmountAccumulator.class, futuresNumber, getSelf()), "resultAmountAccumulator");
        mapFileWriter = getContext().actorOf(Props.create(MapFileWriter.class, destinationFilePath), "mapFileWriter");
    }

    @Override
    public void preStart() throws Exception {
        idCollector.tell(MessageEnum.COLLECT_ID, getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Set) {
            Set<Integer> ids = (Set<Integer>) message;

            log.info("Ids distribution beetween futures...");
            final List<Set<Integer>> idsList = Utils.split(ids, futuresNumber);

            log.info("Calculation of each id amount's summary...");
            for (int i = 0; i < futuresNumber; i++) {
                FileReaderTask fileReaderTask = new FileReaderTask(String.valueOf(1), sourceFilePath, idsList.get(i), dataSeparator);
                Future<Map<Integer, Double>> f = future(fileReaderTask, getContext().dispatcher());
                akka.pattern.Patterns.pipe(f, getContext().dispatcher()).to(resultAmountAccumulator);
            }
        } else if (message instanceof Map) {
            log.info("Calculation of each id amount's summary is finished");
            mapFileWriter.tell(message, getSelf());
        } else if (message == MessageEnum.WRITING_RESULTS_COMPLETED) {
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
