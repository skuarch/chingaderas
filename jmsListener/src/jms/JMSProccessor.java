package jms;


import java.io.Serializable;
import java.net.InetAddress;
import java.util.Enumeration;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TopicSubscriber;
import org.apache.log4j.Logger;
import util.PropertieWrapper;
import util.SerializableObject;

/**
 *
 * @author skuarch
 */
public class JMSProccessor extends JMS {

    private static final Logger logger = Logger.getLogger(JMSProccessor.class);
    private TopicSubscriber topicSubscriber = null;
    private ObjectMessage objectMessage = null;
    private MessageProducer messageProducer = null;
    private Message message = null;
    private String myName = null;

    //==========================================================================
    public JMSProccessor() {
        super();
        this.topicSubscriber = getTopicSubcriber();
        this.myName = PropertieWrapper.getMyName();
        objectMessage = getObjectMessage();
        messageProducer = getMessageProducer();
    } // end JMSProccessor

    //==========================================================================
    public synchronized void receive() {

        Enumeration enumeration = null;
        
        try {

            logger.info(" :: ready to receive messages :: ");

            while (true) {

                message = topicSubscriber.receive();

                if (message != null) {

                        objectMessage = (ObjectMessage) message;
                        System.out.println("==============================================================================================================================================================================");
                        logger.info(message);
                        
                }

            } // end while

        } catch (Exception e) {
            logger.error("receive", e);
        }

    } // end receive

    //==========================================================================
    public synchronized void send(String string, String select, String sendTo, String tagTo, String type, String key, Object object) {

        if (object == null) {
            throw new NullPointerException("object is null");
        }

        String sendBy = null;
        String tagBy = null;

        try {

            sendBy = InetAddress.getLocalHost().getHostName();
            tagBy = "srs";

            logger.info("publish message to " + sendTo + " " + string);

            objectMessage.setStringProperty("select", select);
            objectMessage.setStringProperty("sendTo", sendTo);
            objectMessage.setStringProperty("sendBy", sendBy);
            objectMessage.setStringProperty("tagTo", tagTo);
            objectMessage.setStringProperty("tagBy", tagBy);
            objectMessage.setStringProperty("type", type);
            objectMessage.setStringProperty("key", key);
            objectMessage.setObject((Serializable) new SerializableObject().getSerializableObject(object));
            messageProducer.send(objectMessage);

        } catch (Exception e) {
            logger.error("send", e);
        } finally {
            shutdownConnection();
        }

    } // end send1

    //==========================================================================
    public synchronized void send(String string, Message message, Object object) {

        if (message == null) {
            throw new NullPointerException("message is null");
        }

        if (object == null) {
            throw new NullPointerException("object is null");
        }

        try {

            send(string, message.getStringProperty("select"), message.getStringProperty("sendBy"), message.getStringProperty("tagBy"), "response", message.getStringProperty("key"), object);

        } catch (Exception e) {
            logger.error("send2", e);
        }

    } // end send2
} // end class

