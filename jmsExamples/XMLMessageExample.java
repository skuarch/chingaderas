/*
 * @(#)XMLMessageExample.java	1.10 04/01/05
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
import java.io.*;
import javax.jms.*;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The XMLMessageExample class consists a main method which creates a
 * Producer and a Consumer objects (Producer and Consumer classes are defined
 * in this file).  The Producer reads a XML file to a StreamMessage and sends
 * it to a queue.  The Consumer receives the StreamMessage and read the XML
 * document from it then using JAXP API to parse the XML document into a
 * DOM object.
 * <p>
 * The command line options for running this program include (in order)
 * a Sun Java(tm) System Message Queue Queue name
 * an XML filename (sample.xml and its DTD file - sample.dtd are provided)
 * an optional system ID URL (for use by the XML parser to resolve any
 *                            external entity URI)
 *
 */
public class XMLMessageExample {

    /**
     * Main method.
     *
     * @param args  the queue used by the example
     *              the xml filename used by the example
     *              and optionally, the system identifier 
     */
    public static void main(String[] args) {
    	if ( (args.length < 2) || (args.length > 3) ) {
    	    System.out.println("Usage: java XMLMessageExample "
                    + "<queue_name> <xml_filename> [<systemid_url>]");
    	    System.exit(1);
    	} 
        String queueName = new String(args[0]);
        System.out.println("Queue name is " + queueName);

        Producer producer = null;
        Consumer consumer = null;
        try {
            producer = new Producer(queueName);
            consumer = new Consumer(queueName);
            producer.send(args[1]);
            consumer.receive(args.length > 2 ? args[2]:null);
        }
        catch (Exception e) {
            System.out.println("Exception occurred : " + e.toString());
            e.printStackTrace();
        }
        finally {
            if (producer != null) producer.close();
            if (consumer != null) consumer.close();
        }
    }

}

class Producer {
    ConnectionFactory    connectionFactory = null;
    Connection           connection = null;
    Session              session = null;
    Queue                queue = null;
    MessageProducer      msgProducer = null;

    public Producer(String queueName) throws Exception {
        try {
            connectionFactory = SampleUtilities.getConnectionFactory();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = SampleUtilities.getQueue(queueName, session);
            msgProducer = session.createProducer(queue);
        }
        catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                 connection.close();
                 connection = null;
            } catch (JMSException e) {} 
        }
    }

    public void send(String xmlfile) throws Exception {
        StreamMessage           streamMessage = null;

        /*
         * Create input stream from the xml file
         * Read bytes from the input stream into a buffer
         * and construct a StreamMessage
         * Send the message
         */
        File f = new File(xmlfile);
        int length = (int)f.length();
        FileInputStream inStream = new FileInputStream(f);
        byte[] buf = new byte[length];
        inStream.read(buf);
        inStream.close();

        streamMessage = session.createStreamMessage();
        streamMessage.writeObject(buf);
        System.out.println("Write " + length + " bytes into message");

        /*
         * Set a property so that the consumer can check to know
         * this message has a XML document body.  This is helpful
         * if there are other messages in the queue.
         */
        streamMessage.setBooleanProperty("MyXMLMessage", true);
        msgProducer.send(streamMessage);
    }
}

class Consumer {
    DocumentBuilder      docBuilder = null;
    ConnectionFactory    connectionFactory = null;
    Connection           connection = null;
    Session              session = null;
    Queue                queue = null;
    MessageConsumer      msgConsumer = null;

    public Consumer(String queueName) throws Exception {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
 
            connectionFactory = SampleUtilities.getConnectionFactory();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = SampleUtilities.getQueue(queueName, session);
            msgConsumer = session.createConsumer(queue);
            connection.start();
        }
        catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                 connection.close();
                 connection = null;
            } catch (JMSException ee) {}
        }
    }

    public void  receive(String systemid) throws Exception {
        StreamMessage           streamMessage = null;
        Message                 message = null;

        /*
         * Receive the message from the queue
         * Process the message  
         */
        while(true) {
            message = msgConsumer.receive();
            if (!(message instanceof StreamMessage)) {
                //not our XML message 
                continue;
            }
            streamMessage = (StreamMessage)message;
            try {
                if (!streamMessage.getBooleanProperty("MyXMLMessage")) {
                    //not our XML message
                    continue;
                }
            } catch (NullPointerException e) { //the property not exist 
                //not our XML message
                continue;
            }

            //got our XML message
            byte[] bytes = (byte[])streamMessage.readObject();
            System.out.println("Read " + bytes.length + " bytes from message");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            InputSource is =new InputSource(bais);
            if (systemid !=  null) {
                is.setSystemId(systemid);
            }
            parse(is);
            break;
        }
    }

    private void parse(InputSource is) throws Exception {
        try {
            Document doc = docBuilder.parse(is);

            // normalize text representation
            doc.getDocumentElement().normalize();
 
            System.out.println ("Root element of the doc is " +
                             doc.getDocumentElement().getNodeName());
 
        } catch (SAXParseException e) {
            System.out.println ("** Parsing error"
                        + ", line " + e.getLineNumber()
                        + ", uri " + e.getSystemId());
            System.out.println("   " + e.getMessage());
            Exception   x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            throw e;

        } catch (SAXException e) {
            Exception   x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            throw e;
 
        } 
    }
}

