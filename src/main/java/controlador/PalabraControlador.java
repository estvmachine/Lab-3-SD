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
    
    public boolean agregar(int id_page, String palabra){

            //Determino la fecha para guardarla
            Date now = new Date();

            // Creo un documento para agregar a la coleccion.
            DBObject document = new BasicDBObject("id_page", id_page)
                    .append("palabra", palabra)
                    .append("count" , 1)
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
    
    public boolean agregarOeditar(int id_page, String palabra){
    
        //Elemento buscado
        BasicDBObject searched_document= new BasicDBObject("id_page", id_page).append("palabra", palabra);
        
        //Contenido de lo que se quiere actualizar
        BasicDBObject update_document = new BasicDBObject().append("$inc", new BasicDBObject().append("count", 1));
            // Inserto el documento en la coleccion de transacciones en el database
        try {
            //Al metodo update se adiciona el booleano para $set=true, solo cambiar lo que se actualiza
            //y que si no existe el elemento, lo agrege.
            palabraCollection.update( searched_document , update_document, true, false);
            //System.out.println("Edite todo bien");
            return true;
        } 
            catch (MongoException.DuplicateKey e) {
            System.out.println("Error en el guardado");
                System.out.println(e);
            return false;
        }
    

    }
    
    public List<DBObject> listar() {
        
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
    
     public List<DBObject> buscar(int id_page, String palabra) {
        
       try{
            DBCursor resultados = palabraCollection.find(new BasicDBObject("palabra",palabra).append("id_page", id_page));
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