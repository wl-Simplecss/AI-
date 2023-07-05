

package com.fusesource.examples.activemq;

import javax.jms.*;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;

public class SimpleProducer {
    private static final Log LOG = LogFactory.getLog(SimpleProducer.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final long MESSAGE_TIME_TO_LIVE_MILLISECONDS = 0;
    private static final int MESSAGE_DELAY_MILLISECONDS = 100;
    private static final int NUM_MESSAGES_TO_BE_SENT = 100;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/simple";

    public  void send(String str) {
        Connection connection = null;

        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Destination destination = new ActiveMQQueue(DESTINATION_NAME);

            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);

            producer.setTimeToLive(MESSAGE_TIME_TO_LIVE_MILLISECONDS);


            TextMessage message = session.createTextMessage(str);
            LOG.info("Sending to destination: " + destination.toString() + " this text: '" + message.getText());
            producer.send(message);
            Thread.sleep(MESSAGE_DELAY_MILLISECONDS);


            // Cleanup
            producer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error(t);
        } finally {
            // Cleanup code
            // In general, you should always close producers, consumers,
            // sessions, and connections in reverse order of creation.
            // For this simple example, a JMS connection.close will
            // clean up all other resources.
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOG.error(e);
                }
            }
        }
    }
}
