package pe.edu.pucp.softpub.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBManager {
    //Atributos 
    private static final String ARCHIVO_CONFIGURACION = "jdbc.properties";
    private Connection conexion; //Debemos importar la conexión
    private String driver;
    private String tipo_de_driver;
    private String base_de_datos;   
    private String nombre_de_host;
    private String puerto;
    private String usuario;
    private String contraseña;
    private static DBManager dbManager = null;
    
    //Metodos
    public static DBManager getInstance(){
        if(DBManager.dbManager == null)
            createInstance(); //Es privado porque quiero que la cree solo una vez cuando sea nulo
        return DBManager.dbManager;
    }
    private static void createInstance(){
        if(DBManager.dbManager == null)
            DBManager.dbManager = new DBManager();
    }
    public Connection getConnection(){
        leer_archivo_propiedades(); //Lo que esta en el archivo lo ponemos en los atributos
        try {
            Class.forName(this.driver); //Instanciamos el driver para la conexion y statement try cath, clause try cath y pegamos
            //No puede haber getcontraseña es privado
            //Creamos la conexión
            this.conexion = DriverManager.getConnection(getURL(),this.usuario,this.contraseña); //Importamos el driver
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.conexion;
    }
    private String getURL(){
        //Este es el fomrato
        String url = this.tipo_de_driver.concat("://");
        url = url.concat(this.nombre_de_host);
        url = url.concat(":");
        url = url.concat(this.puerto);
        url = url.concat("/");
        url = url.concat(this.base_de_datos);
        return url;
    }
    private void leer_archivo_propiedades(){
        Properties pro = new Properties();
        String nomArchConf = "resoruce/" + ARCHIVO_CONFIGURACION; //Esta en esa carpeta
        try {
            pro.load(new FileInputStream(new File(nomArchConf))); //Es ncesario poner el Statemen trycath y el clause despues y abajo de esto pegamos
            this.driver = pro.getProperty("driver");
            this.tipo_de_driver = pro.getProperty("tipo_de_driver");
            this.base_de_datos = pro.getProperty("base_de_datos");
            this.nombre_de_host = pro.getProperty("nombre_de_host");
            this.usuario = pro.getProperty("usuario");
            this.contraseña = pro.getProperty("contraseña");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
