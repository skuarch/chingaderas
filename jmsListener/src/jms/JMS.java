package jms;

import common.CreateContext;
import javax.jms.*;
import javax.naming.Context;
import org.apache.log4j.Logger;
import util.JMSUtilities;
import util.PropertieWrapper;

/**
 *
 * @author skuarch
 */
public class JMS {
    
    private final static Logger logger = Logger.getLogger(JMS.class);
    private static String JMS_CONNECTION_FACTORY = PropertieWrapper.getJMSConnectionFactory();
    private static String JMS_TOPIC = PropertieWrapper.getJMSTopic();
    private Context context = null;
    private TopicConnectionFactory topicConnectionFactory = null;
    private Topic topic = null;
    private TopicSession topicSession = null;
    private TopicConnection topicConnection = null;
    private MessageProducer messageProducer = null;
    private TopicSubscriber topicSubcriber = null;
    private ObjectMessage objectMessage = null;

    //==========================================================================
    public JMS() {        
        this.context = new CreateContext().getInitialContext();        
        onLoad();
    } // end JMS

    //==========================================================================
    private void onLoad() {
        
        try {

            //setup connection
            topicConnectionFactory = (TopicConnectionFactory) context.lookup(JMS_CONNECTION_FACTORY);
            topic = (Topic) context.lookup(JMS_TOPIC);
            
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicConnection.start();
            topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

            //for send
            messageProducer = topicSession.createProducer(topic);
            objectMessage = topicSession.createObjectMessage();

            //for receive
            topicSubcriber = topicSession.createSubscriber(topic);
            
        } catch (Exception e) {
            logger.error("onLoad", e);
        }
        
    } // end onLoad

    //==========================================================================
    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    //==========================================================================
    public ObjectMessage getObjectMessage() {
        return objectMessage;
    }

    //==========================================================================
    public TopicSubscriber getTopicSubcriber() {
        return topicSubcriber;
    }

    //==========================================================================
    public void shutdownConnection() {
        JMSUtilities.closeTopicConnection(topicConnection);
        JMSUtilities.closeTopicSession(topicSession);
        JMSUtilities.closeTopicSubscriber(topicSubcriber);
    }    
} // end class
