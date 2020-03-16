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

  //READ.ME
  //INPUT: Parlamentsdebatten (file/input/all)
  //OUTPUT: Worthäufigkeiten pro Wahlperiode (file/output/WordFrequencies)
  //zählen von Worthäufigkeiten pro Parlamentsdebatte

  public static void main(String[] args)
      throws ParserConfigurationException, IOException, SAXException {

    File dirPath = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/input/all");
    File[] dirPaths = dirPath.listFiles();

    assert dirPaths != null;
    for (File value : dirPaths) {
      String periode = value.getName();

      File directoryPath = new File(
          "C:/Users/jojoz/IdeaProjects/DHProject/files/input/all/" + periode);
      File[] directoryPaths = directoryPath.listFiles();

      // Text,
      StringBuilder inputText = new StringBuilder();

      // Aufbauen des Textes, dessen Wortfrequenz gezählt werden sollen
      assert directoryPaths != null;
      for (File path : directoryPaths) {
        String filename = path.getName();

        File file = new File(
            "C:/Users/jojoz/IdeaProjects/DHProject/files/input/all/" + periode + "/" + filename);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        inputText.append(" ").append(doc.getElementsByTagName("TEXT").item(0).getTextContent());
      }

      // entfernen aller Satzzeichen und lowerCase
      inputText = new StringBuilder(inputText.toString().replaceAll("\\p{Punct}", ""));
      inputText = new StringBuilder(inputText.toString().toLowerCase());

      StringTokenizer tokenizer = new StringTokenizer(inputText.toString());

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
          new BufferedWriter(
              new FileWriter("files/output/WordFrequencies/" + periode + "WordFrequency.txt")));
      sortedArrayList.forEach(tokensWriter::println);
      tokensWriter.flush();
      tokensWriter.close();
    }
  }
}
