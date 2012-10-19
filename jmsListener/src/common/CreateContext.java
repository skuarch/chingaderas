package common;


import javax.naming.InitialContext;
import util.LoaderProperties;
import org.apache.log4j.Logger;

/**
 *
 * @author skuarch
 */
public class CreateContext {

    private static final Logger logger = Logger.getLogger(CreateContext.class.getName());
    

    //==========================================================================
    public CreateContext() {        
    } // end CreateContext

    //==========================================================================
    public InitialContext getInitialContext() {

        InitialContext initialContext = null;

        try {

            initialContext = new InitialContext(new LoaderProperties().getProperties("configuration/jndi.properties"));

        } catch (Exception e) {
            logger.error("getInitialContext", e);
        }

        return initialContext;

    } // end getInitialContext
} // end class
