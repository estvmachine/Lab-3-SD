

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import controlador.PaginaControlador;
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
                    
                    //Guardar en BD
                    contador= contador +1;
                    paginasControlador.agregar(titulo, texto, contador);
                    paginasControlador.buscar(contador);
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


