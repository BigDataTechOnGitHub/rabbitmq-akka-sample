package de.alexander_hanschke.samples.rabbitmq_akka;

/**
 * @author <a href="mailto:dev@alexander-hanschke.de">Alexander Hanschke</a>
 */
public class Task {

    private final String content;

    public Task(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
