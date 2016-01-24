
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class LecturaXml {

   public static void main(String argv[]) {
    
    try {

	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
        File file = new File("prueba.xml");
        

	DefaultHandler handler = new DefaultHandler() {

        boolean texto = false;
	boolean id = false;
	boolean titulo = false;
        boolean comentarios = false;
        String string = "";

        @Override
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
	              
                if (qName.equalsIgnoreCase("title")) {
			titulo = true;
		}
                    
		if (qName.equalsIgnoreCase("text")) {
			texto = true;
		}

		if (qName.equalsIgnoreCase("id")) {
			id = true;
		}	
             /*   if (qName.equalsIgnoreCase("comment")) {
			comentarios = true;
		}*/
	}

        @Override
        public void endElement(String uri, String localName,
		String qName) throws SAXException {

		if (qName.equalsIgnoreCase("text")) {
			texto = false;
                         System.out.println("\n");
                      
                       // System.out.println("Texto : " + string);
		}                 
	}

        @Override
	public void characters(char ch[], int start, int length) throws SAXException {
                String Aux = new String(ch, start, length);
	
               
                if (titulo) {
			System.out.println("Titulo : " + new String(ch, start, length));
			titulo = false;
		}

		if (texto) {
			System.out.println("Texto : " + Aux);
			string += Aux;
                        texto = false;
                        		}

		if (id) {
			System.out.println("Id : " + new String(ch, start, length));
			id = false;
		}          
             
	}

     };
  
       saxParser.parse(file, handler);
 
     } catch (ParserConfigurationException | SAXException | IOException e) {
     }  
   }
}

