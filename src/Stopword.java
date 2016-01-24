
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Stopword{
    private ArrayList<String> stopwords;
        
   public void loadStopWords() throws FileNotFoundException {
        ArrayList<String> stopwords;
        BufferedReader archivoStopwords = new BufferedReader(new FileReader("stopwords.txt"));
        String line;
        this.stopwords = new ArrayList<String>();
        try {

            while ((line = archivoStopwords.readLine()) != null) {
                this.stopwords.add(line.toUpperCase());
                //System.out.println("Stop: " + line);
            }

            archivoStopwords.close();

        } catch (Exception e) {
        }

    }
}