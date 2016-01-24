/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Pagina;
import static org.bson.types.ObjectId.get;

public class PaginaControlador {
    
    private final DBCollection paginaCollection;
    private MongoClient cliente;
    private DB database;
    
    public PaginaControlador() {
          
        try {
        // Creo una instancia de cliente de MongoDB
            cliente = new MongoClient(new ServerAddress("localhost", 27017));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ex.toString());
        }
          
        //Creo conexion con base de datos
        database = cliente.getDB("DBIndex");
        
        paginaCollection = database.getCollection("paginas");
         
    }
    
    public boolean agregar(Pagina pagina){
        
        // Inserto el documento en la coleccion de transacciones en el database
        try {
            paginaCollection.insert(pagina);
            return true;
        } 
            catch (MongoException.DuplicateKey e) {
            System.out.println("Error en el guardado");
            return false;
        }
    }
    
    public boolean agregar(String title, String text, int id){

            //Determino la fecha para guardarla
            Date now = new Date();

            // Creo un documento para agregar a la coleccion.
            DBObject document = new BasicDBObject("title", title)
                    .append("text", text)
                    .append("id" , id)
                    .append("create_at", now);

            // Inserto el documento en la coleccion de transacciones en el database
        try {
            paginaCollection.insert(document);
            return true;
        } 
            catch (MongoException.DuplicateKey e) {
            System.out.println("Error en el guardado");
            return false;
        }
    

    }
    
      
    public List<DBObject> listar() {
        
        BasicDBObject index= new BasicDBObject("id", 1);
       try{
            DBCursor resultados = paginaCollection.find().hint(index);
            System.out.println(resultados.toArray());
            return resultados.toArray();
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    
     public List<DBObject> buscar(int id) {
        
       try{
            DBCursor resultados = paginaCollection.find(new BasicDBObject("id",id));
            System.out.println(resultados.toArray());
            return resultados.toArray();
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
     
     public void borrarTodo(){
         
        try{
            paginaCollection.drop();
        }
        catch(Exception e){
            System.out.println(e);
        }
     
     }
}