package akka.practice.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class ResultAmountAccumulator extends UntypedActor {

    private final int expectedResultsAmount;
    private final ActorRef actorForResponse;
    private volatile int currentResultsAmount = 0;
    private final Map<Integer, Double> resultAmountMap = new ConcurrentHashMap<>();

    public ResultAmountAccumulator(int expectedResultsAmount, ActorRef actorForResponse) {
        this.expectedResultsAmount = expectedResultsAmount;
        this.actorForResponse = actorForResponse;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Map) {
            resultAmountMap.putAll((Map<Integer, Double>) message);
            currentResultsAmount++;
            if (currentResultsAmount == expectedResultsAmount) {
                actorForResponse.tell(resultAmountMap, getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
