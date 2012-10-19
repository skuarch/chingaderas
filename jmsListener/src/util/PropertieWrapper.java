package util;

/**
 *
 * @author skuarch
 */
public class PropertieWrapper {        
    
    //==========================================================================
    public static String getJMSConnectionFactory(){
        return new LoaderProperties().getProperties("configuration/configuration.properties").getProperty("jms.connection.factory");
    }
    
    //==========================================================================
    public static String getJMSTopic(){
        return new LoaderProperties().getProperties("configuration/configuration.properties").getProperty("jms.topic");
    }
    
    //==========================================================================
    public static String getMyName(){
        return new LoaderProperties().getProperties("configuration/configuration.properties").getProperty("my.name");
    }
    
} // end class
