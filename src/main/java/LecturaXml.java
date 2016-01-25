

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import controlador.PaginaControlador;
import controlador.PalabraControlador;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class LecturaXml {
    private static DB database;
    private static MongoClient cliente;
    private static PaginaControlador paginasControlador;
    private static PalabraControlador palabrasControlador;
    
     private boolean isTexto;
     private boolean isTitulo;
     private String texto ;
     private String titulo;
     private StringBuilder content;
    
   public static void main(String argv[]) {
    
    try {
        
        //Inicio conexion con BD Mongo
        try {
        // Creo una instancia de cliente de MongoDB
            cliente = new MongoClient(new ServerAddress("localhost", 27017));
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }
        //Creo conexion con base de datos
        database = cliente.getDB("DBIndex");
        //Instancio controlador de DataXML, con este se puede agregar, listas, borrar de MongoDB, etc
        paginasControlador= new PaginaControlador();
        
        System.out.println("Iniciar BD Mongo");
        System.out.println("Borrando datos antiguos");
        paginasControlador.borrarTodo();
       
        //Leo desde prueba.xml
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
        File file = new File("ismael.xml");
        

	DefaultHandler handler = new DefaultHandler() {

        boolean isTexto = false;
	boolean isTitulo = false;
        String texto = "";
        String titulo= "";
        StringBuilder content;
        int contador=0;

        @Override
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
            
                content= new StringBuilder();
	              
                if (qName.equalsIgnoreCase("title")) {
			isTitulo = true;
                        titulo= new String();
		}
                    
		if (qName.equalsIgnoreCase("text")) {
			isTexto = true;
                        texto= new String();
		}    
	}

        @Override
        public void endElement(String uri, String localName,
		String qName) throws SAXException {

		if (isTitulo) {
                    titulo = content.toString();
                    isTitulo=false;
		}
                else if(isTexto){
                    texto=content.toString();
                    isTexto=false;
                    
                    //Guardar en BD la pagina
                    contador= contador +1;
                    paginasControlador.agregar(titulo, texto,contador);
                    paginasControlador.buscar(contador);
                    
                    //Guardar en BD las palabras de una pagina
                    //palabrasControlador.agregar(id_pagina, palabra, count);
                    
                    //1. Necesitas borrar con replaceAll todas las palabras del Blacklist
                    //2. Necesitas borrar con replaceFirst y empezar a comparar palabra por palabra y empezar a contar
                    //Si el texto reemplazado es igual al texto original de esa iteracion es que no se puede filtrar mas
                    //y tu count final, es la cantidad de esa palabra en el texto.
                    //Revisar http://ideone.com/kcH6YL
                    /*
                     import java.util.*;
                     import java.lang.*;
                     import java.io.*;
                            class Ideone
                            {
                                    public static void main (String[] args) throws java.lang.Exception
                                    {
                                             // Cadena sobre la que realizaremos la sustitución
                                    String cadena1 = "En un lugar de La Mancha";

                                    // Cadena en la que almacenaremos el resultado
                                    String cadena2 = null;

                                    cadena2 = cadena1.replaceAll("en", "");
                                    if(cadena2.equals(cadena1)){
                                            System.out.println("No se encuentra palabra");
                                    }
                                    else {
                                            System.out.println("Se encuentra palabra");
                                            System.out.println(cadena2);
                                    }

                                    }
                            }
                    */
                    
                }
	}

        @Override
	public void characters(char ch[], int start, int length) throws SAXException {
                if (isTitulo || isTexto){
                   content.append(ch, start, length);
                }      
             
	}

     };
  
       saxParser.parse(file, handler);
 
     } catch (ParserConfigurationException | SAXException | IOException e) {
     }  
   }
}


