
package com.fusesource.examples.activemq;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;

public class SimpleConsumer {
    private static final Log LOG = LogFactory.getLog(SimpleConsumer.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/simple";
    private static final int MESSAGE_TIMEOUT_MILLISECONDS = 1500;

    public  String receive() {
        Connection connection = null;
        String answer="";

        try {



            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Destination destination = new ActiveMQQueue(DESTINATION_NAME);

            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);

            LOG.info("Start consuming messages from " + destination.toString() + " with " + MESSAGE_TIMEOUT_MILLISECONDS + "ms timeout");

            // Synchronous message consumer
            int i = 1;
            while (true) {
                Message message = consumer.receive(MESSAGE_TIMEOUT_MILLISECONDS);
                if (message != null) {
                    if (message instanceof TextMessage) {
                        String text = ((TextMessage) message).getText();
                        answer=text;
                        System.out.println(text);
                        LOG.info("Got " + (i++) + ". message: " + text);
                    }
                } else {
                    break;
                }
            }

            consumer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error(t);
        } finally {


            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOG.error(e);
                }
            }
        }
        return answer;
    }
}