package sniffer;

import common.SendSplitFiles;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.jms.ObjectMessage;
import jms.JMSProccessor;
import org.apache.log4j.Logger;
import util.HashMapUtilitie;
import util.SplitFile;

/**
 *
 * @author skuarch
 */
public class Thresholds implements Runnable {

    private static final Logger logger = Logger.getLogger(Thresholds.class);
    private ObjectMessage objectMessage = null;

    //==========================================================================
    public Thresholds(ObjectMessage objectMessage) {
        this.objectMessage = objectMessage;
    } // end Thresholds

    //==========================================================================
    @Override
    public void run() {

        String view = HashMapUtilitie.getProperties(objectMessage, "view");
        String[] threshold = new String[4];

        try {

            System.out.println(view);
            
            if (view.equalsIgnoreCase("threshold have captures")) {
                
                //check if exists captures, if exists captures send true
                //this is for test
                new JMSProccessor().send("response have captures", objectMessage, true);
                
            } else if (view.equalsIgnoreCase("threshold save")) {
                //save the threshold
                HashMapUtilitie.getPropertiesNames(objectMessage);
                System.out.println(HashMapUtilitie.getProperties(objectMessage, "threshold bandwidth"));
                setConfigurationCaptures();
                
            } else if(view.equalsIgnoreCase("threshold get")) {
                //send information about of threshold
                threshold = getConfigurationCaptures();
                new JMSProccessor().send("response threshold", objectMessage, threshold);
            } else if (view.equalsIgnoreCase("threshold day")) {                
                new JMSProccessor().send("threshold day", objectMessage, new String[]{"Thu 26 Apr 2012", "Wed 25 Apr 2012"});
            } else if (view.equalsIgnoreCase("threshold captures")) {
                new JMSProccessor().send("threshold captures", objectMessage, new String[]{"14:07:08.612--14:08:08.620", "14:14:39.448--14:15:39.482", "18:23:27.912--18:24:27.912"});
            } else if(view.equalsIgnoreCase("threshold capture")){
                //send capture
                //new JMSProccessor().sendStream("response threshold capture", objectMessage, "configuration/configuration.properties");                
                //new JMSProccessor().sendStream("response threshold capture", objectMessage, "/home/skuarch/Desktop/ZendStudio-8.0.0-x86.tar.gz");
                //new JMSProccessor().sendStream("response threshold capture", objectMessage, "/home/skuarch/Desktop/CV Template 2010 IBM AS y MEX.doc");
                //new JMSProccessor().sendStream("response threshold capture", objectMessage, "/home/skuarch/Desktop/gl.jar");
                
                new SendSplitFiles().sendFiles(new SplitFile().getFiles(new File("/home/skuarch/Desktop/test1.iso")), objectMessage);
                
                //sendCapture();
            }   
        } catch (Exception e) {
            logger.error("run", e);
        }
        
    } // end run
    
    //==========================================================================
    /**
     * cambiar a send file
     */
    private void sendCapture(){
    
        
        File file = null;
        double mb = 0.0;
        FileInputStream fileInputStream = null;
        byte[] bytesRead = new byte[1024*1024];
        int flag = 0;
        FileOutputStream fileOutputStream = null;        
        int i = 1;
        BufferedInputStream bufferedInputStream = null;
        
        try {
            
            
            //in
            file = new File("/home/skuarch/Desktop/gl.jar");
            fileInputStream = new FileInputStream(file);            
            bufferedInputStream = new BufferedInputStream(fileInputStream);
    
            mb = (file.length() / 1024) / 1024;
            System.out.println("se enviaran " + mb + " partes");
            
            flag = bufferedInputStream.read(bytesRead);
            
            System.out.println("flag " + flag);
            
            while(flag > 0){
                
                System.out.println("writing part " + i);
                fileOutputStream = new FileOutputStream(new File("/home/skuarch/Desktop/parts/part" + i));
                fileOutputStream.write(bytesRead);
                fileOutputStream.flush();
                fileOutputStream.close();
                
                flag = bufferedInputStream.read(bytesRead);
                i++;
            }
            
            fileInputStream.close();
            bufferedInputStream.close();
            
        } catch (Exception e) {
            logger.error("sendCapture",e);
        }
    
    }

    //==========================================================================
    
    private static String[] getConfigurationCaptures() {
        String configAlarma[] = null;
        DataBaseConnection db = null;
        ResultSet rs = null;

        try {
            db = new DataBaseConnection();
            configAlarma = new String[6];

            rs = db.executeQuery("SELECT umbral_alarma, unidad_alarma, activa_alarma, snaplen_packet_alarma, file_size_alarma, capture_time_alarma FROM configuracion_alarma");

            rs.next();
            configAlarma[0] = rs.getString(1);
            configAlarma[1] = rs.getString(2);
            if(rs.getString(3).equals("1")){
                configAlarma[2] = "true";
            } else {
                configAlarma[2] = "false";
            }
            
            configAlarma[3] = rs.getString(4);
            configAlarma[4] = rs.getString(5);
            configAlarma[5] = rs.getString(6);
            db.closeResultSet(rs);

            db = null;

        } catch (SQLException ex) {
            logger.error("ERROR al obtener el umbral: " + ex);
        } finally {
            rs = null;
            db = null;
        }
        return configAlarma;
    }

    //==========================================================================

    private void setConfigurationCaptures(){
        DataBaseConnection db = null;
        String insertConfigAlarma = null;
        String activa = "0";
        String file_size_alarma = "104857600";
        String capture_time_alarma = "60";
        try {
            activa = HashMapUtilitie.getProperties(objectMessage, "threshold is active");
            db = new DataBaseConnection();

            if (activa.equalsIgnoreCase("true")) {
                activa = "1";
            } else {
                activa = "0";
            }



            db.update("TRUNCATE TABLE configuracion_alarma");

            insertConfigAlarma = "INSERT IGNORE INTO configuracion_alarma "
                    + "(umbral_alarma, unidad_alarma ,usuario_alarma, activa_alarma, snaplen_packet_alarma, "
                    + "file_size_alarma, capture_time_alarma) values ("
                    + "'" + HashMapUtilitie.getProperties(objectMessage, "threshold bandwidth") + "', "
                    + "'" + HashMapUtilitie.getProperties(objectMessage, "threshold measurement") + "', "
                    + "'Default', "
                    + activa + ", "
                    + HashMapUtilitie.getProperties(objectMessage, "threshold snapLen") + ", "
                    + file_size_alarma + ","
                    + capture_time_alarma + ")";

            db.update(insertConfigAlarma);
        } catch (Exception e) {
            logger.error("ERROR al actualizar configuracion de alarmas: " + e);
        } finally{
            db = null;
        }
    }

} // end class

