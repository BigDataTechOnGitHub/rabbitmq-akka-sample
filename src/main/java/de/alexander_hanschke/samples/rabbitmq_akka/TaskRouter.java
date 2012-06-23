package de.alexander_hanschke.samples.rabbitmq_akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author <a href="mailto:dev@alexander-hanschke.de">Alexander Hanschke</a>
 */
public class TaskRouter extends UntypedActor {

    private final ActorRef router;

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("taskSystem");

        @SuppressWarnings("serial")
        ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new TaskRouter();
            }
        }), "master");

        master.tell(new Messages.Init());
    }

    private TaskRouter() {
        this.router = this.getContext().actorOf(
                new Props(TaskActor.class).withRouter(
                    new RoundRobinRouter(Config.WORKER_COUNT)), "taskRouter");
    }

    private void assignTasks() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Config.HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Config.DEFAULT_QUEUE, false, false, false, null);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(Config.DEFAULT_QUEUE, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            router.tell(new Task(message));
        }
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Messages.Init) {
            try {
                assignTasks();
            } catch (Throwable cause) {
                throw new RuntimeException(cause);
            }
        } else {
            unhandled(message);
        }
    }

}
