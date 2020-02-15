import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WordFrequency {

  //ToDo
  // -Stemming anwenden!
  // -Wortumbrüche entfernen!

  public static void main(String[] args)
      throws ParserConfigurationException, IOException, SAXException {

    // xml.Datei einlesen:
    File file = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/input/16/16105.xml");
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(file);

    // InputText, der bearbeitet werden soll
    String inputText = doc.getElementsByTagName("TEXT").item(0).getTextContent();
    // entfernen aller Satzzeichen und lowerCase
    inputText = inputText.replaceAll("\\p{Punct}", "");
    inputText = inputText.toLowerCase();

    StringTokenizer tokenizer = new StringTokenizer(inputText);

    // in Map werden Häufigkeiten der verschiedenen tokens gespeichert
    Map<String, Integer> map = new HashMap<>();
    String currentToken;
    while (tokenizer.hasMoreTokens()) {
      currentToken = tokenizer.nextToken();
      if (map.containsKey(currentToken)) {
        map.put(currentToken, map.get(currentToken) + 1);
      } else {
        map.put(currentToken, 1);
      }
    }

    // Hashmap sortieren
    List<Entry<String, Integer>> sortedArrayList = new ArrayList<>(map.entrySet());
    sortedArrayList.sort((o1, o2) -> o2.getValue() - o1.getValue());

    //alle Tokens mit Häufigkeit in Datei "TokensFrequency" abspeichern:
    PrintWriter tokensWriter = new PrintWriter(
        new BufferedWriter(new FileWriter("files/output/TokensFreuquency.txt")));
    sortedArrayList.forEach(tokensWriter::println);
    tokensWriter.flush();
    tokensWriter.close();
  }
}
