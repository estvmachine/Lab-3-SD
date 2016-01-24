package modelo;

import com.mongodb.BasicDBObject;
import java.util.Date;

public class Palabra extends BasicDBObject{
    
    
    public Palabra (int id_page, String palabra, int count){   
        Date now = new Date();
        this.append("id_page", id_page)
            .append("palabra", palabra)
            .append("count", count)
            .append("created_at", now);
    }
}