import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SortByParty {

  //TODO:
  // - Partei spezifische Dokumente anvertigen
  // - wie kleinteilig wollen wir die Partei spezifischen Dokumente haben? pro debatte, pro Wahlperiode? -> erst mal pro Wahlperiode

  public static void main(String[] args)
      throws IOException, SAXException, ParserConfigurationException {

    File inputfile = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/16/Clean16105.xml");

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(inputfile);

    String inText = doc.getElementsByTagName("Debatte").item(0).getTextContent();

    NodeList nodes = doc.getElementsByTagName("Debatte");
    System.out.println(inText);

    for(int i = 0; i<nodes.getLength(); i++){
      Node node = nodes.item(i);



    }


  }

}
