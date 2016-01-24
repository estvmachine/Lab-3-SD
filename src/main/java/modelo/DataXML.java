
package modelo;

import com.mongodb.BasicDBObject;
import java.util.Date;

public class DataXML extends BasicDBObject{
    Date now = new Date();
    public DataXML (String title, String text, int id){       
        this.append("title", title)
            .append("text", text)
            .append("id", id)
            .append("created_at", now);
    }
}
