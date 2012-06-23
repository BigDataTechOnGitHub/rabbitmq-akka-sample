package de.alexander_hanschke.samples.rabbitmq_akka;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author <a href="mailto:dev@alexander-hanschke.de">Alexander Hanschke</a>
 */
public class TaskManager {

    public static void main(String[] args) throws IOException {
        new TaskManager().produceTasks();
    }

    public void produceTasks() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Config.HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Config.DEFAULT_QUEUE, false, false, false, null);

        for (int index = 0; index < Config.MESSAGE_COUNT; index++) {
            String message = String.format("%s-message[%s]", System.nanoTime(), index);
            channel.basicPublish("", Config.DEFAULT_QUEUE, null, message.getBytes());
            System.out.println(String.format(" [x] sent '%s'", message));
        }

        channel.close();
        connection.close();
    }

}
