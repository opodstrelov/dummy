package akka.practice.util;

import akka.actor.*;
import akka.practice.actors.*;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class FileWithTestDataGenerator {

    public static void main(String[] args) {

        if (args.length != 6) {
            System.err.println("Incorrect input args");
            System.exit(-1);
        }

        final String fileName = args[0];
        final int minId = Integer.valueOf(args[1]);
        final int maxId = Integer.valueOf(args[2]);
        final double minAmount = Double.parseDouble(args[3]);
        final double maxAmount = Double.parseDouble(args[4]);
        final int recordsAmount = Integer.parseInt(args[5]);

        final ActorSystem system = ActorSystem.create("FileGeneration");
        ActorRef detailsFileWriter = system.actorOf(Props.create(RandomInformationFileWriter.class), "detailsFileWriter");
        system.actorOf(Props.create(Terminator.class, detailsFileWriter), "terminator");

        RandomInformationFileWriter.Details details = new RandomInformationFileWriter.Details(
                fileName, minId, maxId, minAmount, maxAmount, recordsAmount
        );
        detailsFileWriter.tell(details, null);
    }

}
