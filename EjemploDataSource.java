import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Ejemplo simple de uso de BasicDataSource.
 *
 * @author Chuidiang.
 */
public class EjemploDataSource {

    /** Pool de conexiones. */
    private DataSource dataSource;

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        new EjemploDataSource();
    }

    /**
     * Inicializa el pool de conexiones BasicDataSource y realiza una insercion.
     * y una consulta
     */
    public EjemploDataSource() throws InterruptedException {
        inicializaDataSource();
        //inserta();
        realizaConsulta();

    }

    /**
     * Inicializacion de BasicDataSource.
     */
    private void inicializaDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("vitalnoc");
        basicDataSource.setUrl("jdbc:mysql://192.168.208.15/sniffer_web_192_168_208_56");
        basicDataSource.setMaxActive(20);
        basicDataSource.setMaxIdle(2);

        // Opcional. Sentencia SQL que le puede servir a BasicDataSource
        // para comprobar que la conexion es correcta.
        basicDataSource.setValidationQuery("select 1");

        dataSource = basicDataSource;
    }

    /**
     * Realiza una insercion, pidiendo una conexion al dataSource y cerrandola
     * inmediatamente despues, para liberarla.
     */
    private void inserta() {
        Connection conexion = null;
        try {
            // BasicDataSource nos reserva una conexion y nos la devuelve.
            conexion = dataSource.getConnection();

            // La insercion.
            Statement ps = conexion.createStatement();
            ps.executeUpdate("insert into person values (null,22,'Pedro','Martinez')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            liberaConexion(conexion);
        }
    }

    /**
     * Cierra la conexion. Al provenir de BasicDataSource, en realidad no se
     * esta cerrando. La llamada a close() le indica al BasicDataSource que
     * hemos terminado con dicha conexion y que puede asignarsela a otro que la
     * pida.
     *
     * @param conexion
     */
    private void liberaConexion(Connection conexion) {
        try {
            if (null != conexion) {
                // En realidad no cierra, solo libera la conexion.
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexion = null;
        }
    }

    /**
     * Realiza una consulta a la base de datos y muestra los resultados en
     * pantalla.
     */
    private void realizaConsulta() throws InterruptedException {
        Connection conexion = null;
        try {
            conexion = dataSource.getConnection();
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from nombre_puertos");

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);

            // La tabla tiene cuatro campos.
            while (crs.next()) {
                java.lang.Thread.sleep(100);
                System.out.println(crs.getObject("portNum"));
                System.out.println(crs.getObject("protocolo"));
                System.out.println(crs.getObject("descripcion"));
                System.out.println("--------------");
            }


            try {
                rs.close();
                sentencia.close();
                crs.close();
            } finally {
                rs = null;
                sentencia = null;
                crs = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // En el finally, para asegurar que se ejecuta, se cierra la
            // conexion.
            liberaConexion(conexion);
        }
    }
}

