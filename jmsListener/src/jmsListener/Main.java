package jmsListener;

import jms.JMSProccessor;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author skuarch
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    
    //==========================================================================
    public Main(){
        PropertyConfigurator.configure("configuration/log4j.properties");
    }
    
    //==========================================================================
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            
            new Main();
            logger.info("** start program **");
            new JMSProccessor().receive();
           
            
        } catch (Exception e) {
            logger.error("main", e);
        }
        
    } // end main
    
    
} // end class
