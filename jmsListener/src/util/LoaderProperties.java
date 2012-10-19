package util;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import util.IOUtilities;

/**
 *
 * @author skuarch
 */
public class LoaderProperties {

    private final static Logger logger = Logger.getLogger(LoaderProperties.class);

    //==========================================================================
    public LoaderProperties() {        
    } // end LoaderProperties

    //==========================================================================
    public Properties getProperties(String path) {

        if (path == null || path.length() < 1) {
            logger.warn("path is null or empty");
        }

        FileInputStream fis = null;
        Properties properties = null;

        try {

            fis = new FileInputStream(path);
            properties = new Properties();
            properties.load(fis);

        } catch (Exception e) {
            logger.error("getProperties", e);
        } finally {
            IOUtilities.closeInputStream(fis);
            fis = null;
        }

        return properties;

    } // end getProperties
} // end class
