package akka.practice.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import static akka.practice.util.Utils.LINE_SEPARATOR;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class RandomInformationFileWriter extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final Random random = new Random();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Details) {
            Details details = (Details) message;
            log.info(details.toString());

            int generatedRecords = 0;
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(details.fileName))) {
                log.info("Writing to file: " + details.fileName);
                while (generatedRecords < details.randomRecordsAmount) {
                    int randomId = random.nextInt(details.maxIdValue - details.minIdValue + 1) + details.minIdValue;
                    double randomAmount = details.minAmountValue + (random.nextDouble() * (details.maxAmountValue - details.minAmountValue));
                    bufferedWriter.write(String.valueOf(randomId) + ";" + String.valueOf(randomAmount) + LINE_SEPARATOR);
                    generatedRecords++;
                }
            }
            log.info("Writing to file: " + details.fileName + " is finished");
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

    public static class Details {

        private final String fileName;
        private final int minIdValue;
        private final int maxIdValue;
        private final double minAmountValue;
        private final double maxAmountValue;
        private final int randomRecordsAmount;

        public Details(String fileName, int minIdValue, int maxIdValue, double minAmountValue, double maxAmountValue, int randomRecordsAmount) {
            this.fileName = fileName;
            this.minIdValue = minIdValue;
            this.maxIdValue = maxIdValue;
            this.minAmountValue = minAmountValue;
            this.maxAmountValue = maxAmountValue;
            this.randomRecordsAmount = randomRecordsAmount;
        }

        @Override
        public String toString() {
            return "Generated file: " + fileName + "\n"
                    + "ID values will be generated in the following range: [" + minIdValue + ";" + maxIdValue + "]\n"
                    + "Amount values will be generated in the following range: [" + minAmountValue + ";" + maxAmountValue + "]\n"
                    + "Totally " + randomRecordsAmount + " records will be generated\n";

        }
    }

}