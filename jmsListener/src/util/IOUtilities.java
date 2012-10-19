package util;


import java.io.InputStream;

/**
 *
 * @author skuarch
 */
public class IOUtilities {

    //==========================================================================
    public static void closeInputStream(InputStream is) {

        try {

            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is = null;
        }

    } // end closeInputStream
} // end class
