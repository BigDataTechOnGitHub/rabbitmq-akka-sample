package de.alexander_hanschke.samples.rabbitmq_akka;

/**
 * @author <a href="mailto:dev@alexander-hanschke.de">Alexander Hanschke</a>
 */
public class Config {

    public static final String DEFAULT_QUEUE = "tasks";

    public static final String HOST = "localhost";

    public static final int WORKER_COUNT = 3;

    public static final int MESSAGE_COUNT = 100;

}
