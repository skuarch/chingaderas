/*
 * @(#)AsynchQueueExample.java	1.3 02/05/02
 * 
 * Copyright (c) 2000-2002 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
import javax.jms.*;

/**
 * The AsynchQueueExample class consists only of a main method, which fetches 
 * one or more messages from a queue using asynchronous message delivery.
 * <p>
 * Compile TextListener.java before you run this program.
 * <p>
 * Run this program in conjunction with SenderToQueue.  Specify a queue name
 * on the command line when you run the program.
 *
 * @author Kim Haase
 * @version 1.6, 08/14/00
 */
public class AsynchQueueExample {

    /**
     * Main method.
     *
     * @param args	the queue used by the example
     */
    public static void main(String[] args) {
        String               queueName = null;
        ConnectionFactory    connectionFactory = null;
        Connection           connection = null;
        Session              session = null;
        Queue                queue = null;
        MessageConsumer      msgConsumer = null;
        TextListener         textListener = null;
        int                  exitResult = 0;
        
    	if (args.length != 1) {
    	    System.out.println("Usage: java AsynchQueueExample <queue_name>");
    	    System.exit(1);
    	}   	
        queueName = new String(args[0]);
        System.out.println("Queue name is " + queueName);
    	    
        try {
            connectionFactory = 
                SampleUtilities.getConnectionFactory();
            connection = 
                connectionFactory.createConnection();
            session = connection.createSession(false, 
                Session.AUTO_ACKNOWLEDGE);
            queue = SampleUtilities.getQueue(queueName, session);
        } catch (Exception e) {
            System.out.println("Connection problem: " + e.toString());
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ee) {}
            }
    	    System.exit(1);
        } 

        /*
         * Create consumer.
         * Register message listener (TextListener).
         * Start message delivery; listener displays the message obtained.
         * Block until producer issues a control message indicating
         * end of publish stream.
         */
        try {
            msgConsumer = session.createConsumer(queue);
            textListener = new TextListener();
            msgConsumer.setMessageListener(textListener);
            connection.start();
            
            textListener.monitor.waitTillDone();
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
            exitResult = 1;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    exitResult = 1;
                }
            }
        }
        SampleUtilities.exit(exitResult);
    }
}
