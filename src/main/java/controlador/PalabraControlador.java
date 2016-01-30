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
import modelo.Palabra;
import static org.bson.types.ObjectId.get;

public class PalabraControlador {
    
    private final DBCollection palabraCollection;
    private MongoClient cliente;
    private DB database;
    
    public PalabraControlador() {
          
        try {
        // Creo una instancia de cliente de MongoDB
            cliente = new MongoClient(new ServerAddress("localhost", 27017));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ex.toString());
        }
          
        //Creo conexion con base de datos
        database = cliente.getDB("DBIndex");
        
        palabraCollection = database.getCollection("palabras");
         
    }
    
    public boolean agregar(Palabra palabra){
        
        // Inserto el documento en la coleccion de transacciones en el database
        try {
            palabraCollection.insert(palabra);
            return true;
        } 
            catch (MongoException.DuplicateKey e) {
            System.out.println("Error en el guardado");
            return false;
        }
    }
    
    public boolean agregar(int id_page, String palabra, int count){

            //Determino la fecha para guardarla
            Date now = new Date();

            // Creo un documento para agregar a la coleccion.
            DBObject document = new BasicDBObject("id_page", id_page)
                    .append("palabra", palabra)
                    .append("count" , count)
                    .append("create_at", now);

            // Inserto el documento en la coleccion de transacciones en el database
        try {
            palabraCollection.insert(document);
            return true;
        } 
            catch (MongoException.DuplicateKey e) {
            System.out.println("Error en el guardado");
            return false;
        }
    

    }
    
      
    public List<DBObject> listar() {
        
        BasicDBObject index= new BasicDBObject("palabra", 1);
       try{
            DBCursor resultados = palabraCollection.find();
            System.out.println(resultados.toArray());
            return resultados.toArray();
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    
     public List<DBObject> buscar(String palabra) {
        
       try{
            DBCursor resultados = palabraCollection.find(new BasicDBObject("palabra",palabra));
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
            palabraCollection.drop();
        }
        catch(Exception e){
            System.out.println(e);
        }
     
     }
}