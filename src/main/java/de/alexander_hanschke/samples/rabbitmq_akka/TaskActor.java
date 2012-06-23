package de.alexander_hanschke.samples.rabbitmq_akka;

import akka.actor.UntypedActor;

/**
 * @author <a href="mailto:dev@alexander-hanschke.de">Alexander Hanschke</a>
 */
public class TaskActor extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof Task) {
            Task task = (Task) message;
            System.out.println(String.format("%s executing task: %s", getSelf(), task.getContent()));
        } else {
            unhandled(message);
        }
    }

}
