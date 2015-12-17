package akka.practice.actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Oleksandr Podstrelov
 * @version 1.0
 * @since 15/15/2015.
 */
public class Terminator extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final ActorRef ref;

    public Terminator(ActorRef ref) {
        this.ref = ref;
        getContext().watch(ref);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            log.info("{} has terminated, shutting down system", ref.path());
            getContext().system().terminate();
        } else {
            unhandled(msg);
        }
    }

}
