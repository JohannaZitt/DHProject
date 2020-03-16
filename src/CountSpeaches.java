import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CountSpeaches {

  //READ.ME
  //INPUT: CleanDok Dokumente (file/output/CleanDok/all)
  //OUTPUT: Ausgabe aller Häufigkeiten: wie viele Redebeiträge pro Partei je Parlamentsdebatten in Konsole

  //Diese Klasse gibt aus, wie viele Redebeiträge es welcher Parteien gibt pro Debatte gibt.
  //alle Parlamentsdebatten werden durchlaufen und jeder Redebeitrag gezählt

  public static void main(String[] args)
      throws ParserConfigurationException, IOException, SAXException {

    // Count über alle Parlametsdebatten
    int totalcdu = 0;
    int totalspd = 0;
    int totalfdp = 0;
    int totallinke = 0;
    int totalafd = 0;
    int totalgrüne = 0;

    File dirPath = new File(
        "C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/all/");
    File[] dirPaths = dirPath.listFiles();

    // Für jede Wahlperiode
    for (int k = 0; k < dirPaths.length; k++) {
      String periode = dirPaths[k].getName();

      File directoryPath = new File(
          "C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/all/" + periode);
      File[] directoryPaths = directoryPath.listFiles();

      // Count über jede Wahlperiode
      int pcdu = 0;
      int pspd = 0;
      int pfdp = 0;
      int plinke = 0;
      int pgrune =0;
      int pafd = 0;

      // für jede Debatte
      for(int i = 0; i<directoryPaths.length; i++) {
        String debate = directoryPaths[i].getName();
        File file = new File(
            "C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/all/" + periode + "/"
                + debate);

        // xml.Datei einlesen:
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        NodeList nodes = doc.getElementsByTagName("Speach");

        // Count über jede Debatte
        int cdu = 0;
        int spd = 0;
        int grune = 0;
        int fdp = 0;
        int afd = 0;
        int link = 0;

        // für jeden Speach Knoten
        for (int j = 0; j < nodes.getLength(); j++) {

          Node node = nodes.item(j);
          Element elem = (Element) node;

          //Zählen der Redebeiträge pro Partei
          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("CDU/CSU")) {
            cdu += 1;
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("SPD")) {
            spd += 1;
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("FDP")) {
            fdp += 1;
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("BÜNDNIS 90/DIE GRÜNEN")) {
            grune += 1;
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("DIE LINKE")) {
            link += 1;
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("AfD")) {
            afd += 1;
          }
        }

        pcdu += cdu;
        pspd += spd;
        pfdp += fdp;
        plinke += link;
        pgrune += grune;
        pafd += afd;

        //erzeugen des outputs
        System.out.println("Debatte: " + debate);
        System.out.println("CDU(" + cdu +"), SPD(" + spd +"), FDP("+ fdp + "), Linke("+ link +"), Grüne("+ grune +"), AfD(" + afd + ")");
        System.out.println("------------------------------------------------------------------------------------------------------");

      }

      totalafd += pafd;
      totalcdu += pcdu;
      totalfdp += pfdp;
      totalgrüne += pgrune;
      totallinke += plinke;
      totalspd += pspd;

      //erzeugen des outputs
      System.out.println("------------------------------------------------------------------------------------------------------");
      System.out.println("Wahlperiode: " + periode);
      System.out.println("CDU(" + pcdu +"), SPD(" + pspd +"), FDP("+ pfdp + "), Linke("+ plinke +"), Grüne("+ pgrune +"), AfD(" + pafd + ")");
      System.out.println("------------------------------------------------------------------------------------------------------");
    }

    //erzeugen des outputs
    System.out.println("------------------------------------------------------------------------------------------------------");
    System.out.println("------------------------------------------------------------------------------------------------------");
    System.out.println("Total: ");
    System.out.println("CDU(" + totalcdu +"), SPD(" + totalspd +"), FDP("+ totalfdp + "), Linke("+ totallinke +"), Grüne("+ totalgrüne +"), AfD(" + totalafd + ")");
  }
}
