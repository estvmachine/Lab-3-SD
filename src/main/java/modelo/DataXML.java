
package modelo;

import com.mongodb.BasicDBObject;
import java.util.Date;

public class DataXML extends BasicDBObject{
    Date now = new Date();
    public DataXML (String title, String text){       
        this.append("title", title)
            .append("text", text)
            .append("created_at", now);
    }
}
