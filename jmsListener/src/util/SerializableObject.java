package util;

import java.io.Serializable;

/**
 *
 * @author skuarch
 */
public class SerializableObject {
    
    //==========================================================================
    public SerializableObject(){
    
    }
    
    //==========================================================================
    public Object getSerializableObject(Object object){
        return (Serializable) object;
    }
    
}
