package com.fusesource.examples.activemq;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.*;

import AIchat.entity.Comment;
import AIchat.util.CustomResponse;
import AIchat.service.ComService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.jms.MessageListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;
@MessageDriven(mappedName = "queue/simple")
public class SimpleMDB implements MessageListener {


    private static final Log LOG = LogFactory.getLog(SimpleMDB.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final long MESSAGE_TIME_TO_LIVE_MILLISECONDS = 0;
    private static final int MESSAGE_DELAY_MILLISECONDS = 100;
    private static final int NUM_MESSAGES_TO_BE_SENT = 100;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/simple";

    @Resource(name = "queue/simple")
    private Queue answerQueue;
@Override
    public void onMessage(Message message) {

        try {
            Connection connection = null;

            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Destination destination = new ActiveMQQueue(DESTINATION_NAME);

            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                text = "MDB:I have received your message: " + text;
                TextMessage msg = session.createTextMessage(text);
                respond(session, msg);
            } else if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                Integer userId = mapMessage.getInt("userId");
                Integer chatId = mapMessage.getInt("chatId");
                String content = mapMessage.getString("content");

                Comment cmt = new Comment();
                cmt.setContent(content);
                cmt = ComService.createComment(userId, chatId, cmt);

                TextMessage msg = session.createTextMessage();
                msg.setText(CustomResponse.convert2Object(cmt).toString());
                respond(session, msg);
            }

        } catch (JMSException e) {
            LOG.error("Error processing message: " + e.getMessage(), e);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void respond(Session session, Message message) {
        try {
            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(answerQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Tell the producer to send the message
            producer.send(message);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}