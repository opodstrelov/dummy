package akka.practice;

import akka.actor.*;
import akka.practice.actors.*;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class Main {

    public static void main(String[] args) {

        final String inputFilePath = args[0];
        final String dataSeparator = args[1];
        final int futuresNumber = Integer.valueOf(args[2]);
        final String resultFilePath = args[3];

        final ActorSystem system = ActorSystem.create("ProcessFile");
        ActorRef totalAmountCalculator = system.actorOf(
                Props.create(TotalAmountCalculator.class, inputFilePath, dataSeparator, futuresNumber, resultFilePath),
                "totalAmountCalculator"
        );
        system.actorOf(Props.create(Terminator.class, totalAmountCalculator), "terminator");
    }

}
