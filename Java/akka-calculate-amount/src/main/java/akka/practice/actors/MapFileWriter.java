package akka.practice.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.practice.util.Utils;

import java.util.Map;

import static akka.practice.actors.MessageEnum.WRITING_RESULTS_COMPLETED;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class MapFileWriter extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final String destinationFilePath;

    public MapFileWriter(String destinationFilePath) {
        this.destinationFilePath = destinationFilePath;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Map) {
            Map mapForWriting = (Map) message;

            log.info("Writing result to file: " + destinationFilePath);
            Utils.writeMapToFile(mapForWriting, destinationFilePath);
            log.info("Writing result to file: " + destinationFilePath + " is finished");
            getSender().tell(WRITING_RESULTS_COMPLETED, getSelf());
        } else {
            unhandled(message);
        }
    }
}