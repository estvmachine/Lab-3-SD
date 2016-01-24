
package modelo;

import com.mongodb.BasicDBObject;
import java.util.Date;

public class Pagina extends BasicDBObject{
    
    public Pagina (String title, String text, int id){   
        
        Date now = new Date();
        this.append("title", title)
            .append("text", text)
            .append("id", id)
            .append("created_at", now);
    }
}
