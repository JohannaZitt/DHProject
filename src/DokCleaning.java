
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DokCleaning {

  //READ.ME
  //INPUT: Parlamentsdebatten (file/input)
  //OUTPUT: jede Rede mit zugehöriger Partei abgespeichert. (file/output/CeanDok)
  //Diese Klasse ließt Parlamentsdebatten ein und gibt jede wieder als .xml Datei aus in folgender Form:
  //- Debatte
  //--- Speach
  //------ Partei /Partei
  //------ Text /Text
  //--- /Speach
  //- /Debatte

  // die hier erzeugten Dateien, werden benötigt um Partei spezifische Datensätze zu erhalten.

  // es werden alle Wahlperioden und alle Parlamentsdebatten mit einer Ausführung im entsprechenden Ordner durchlaufen.
  // verschiedene Ordner: GE (gleichgeschlechtliche Ehe), NGE(nicht gleichgeschlechtliche Ehe), all(alle Parlamentsdebatten)
  // Texte werden tokenisiert und nach relevanten Tokens (die eine neue Rede markieren) durhsucht.


  public static void main(String[] args)
      throws ParserConfigurationException, TransformerException, IOException, SAXException {

    // Change directory here (all, GE, NGE)
    String directory = "NGE";

    File dirPath = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/" + directory);
    File[] dirPaths = dirPath.listFiles();

    // durchlaufen alles Wahlperioden
    for(int k = 0; k<dirPaths.length; k++) {
      String periode = dirPaths[k].getName();

      File directoryPath = new File(
          "C:/Users/jojoz/IdeaProjects/DHProject/files/input/" + directory + "/" + periode);
      File[] directoryPaths = directoryPath.listFiles();

      // durchlaufen aller Debatten einer Periode
      for (int i = 0; i < directoryPaths.length; i++) {
        String filename = directoryPaths[i].getName();

        File file = new File(
            "C:/Users/jojoz/IdeaProjects/DHProject/files/input/" + directory + "/" + periode + "/"
                + filename);

        // xml.Datei einlesen:
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        // Konstanten aus xml.Datei herauslesen:
        String inWahlperiode = doc.getElementsByTagName("WAHLPERIODE").item(0).getTextContent();
        String inDokumentenart = doc.getElementsByTagName("DOKUMENTART").item(0).getTextContent();
        String inNr = doc.getElementsByTagName("NR").item(0).getTextContent();
        String inDatum = doc.getElementsByTagName("DATUM").item(0).getTextContent();
        String inTitel = doc.getElementsByTagName("TITEL").item(0).getTextContent();
        String inText = doc.getElementsByTagName("TEXT").item(0).getTextContent();

        // xml.Datei schreiben:
        // Dokument erstellen:
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        // xmlStruktur erstellen und Konstanten einspeichern:
        Element root = document.createElement("Parlamentsdebatte");
        document.appendChild(root);

        Element wahlperiode = document.createElement("Wahlperiode");
        root.appendChild(wahlperiode);
        wahlperiode.appendChild(document.createTextNode(inWahlperiode));

        Element dokumentenart = document.createElement("Dokumentenart");
        root.appendChild(dokumentenart);
        dokumentenart.appendChild(document.createTextNode(inDokumentenart));

        Element nr = document.createElement("Nr.");
        root.appendChild(nr);
        nr.appendChild(document.createTextNode(inNr));

        Element datum = document.createElement("Datum");
        root.appendChild(datum);
        datum.appendChild(document.createTextNode(inDatum));

        Element titel = document.createElement("Titel");
        root.appendChild(titel);
        titel.appendChild(document.createTextNode(inTitel));

        Element debatte = document.createElement("Debatte");
        root.appendChild(debatte);

        // verschiedene Parteien, die auftauchen können; Diese werden zum Abgleichen der Tokens verwendet
        String cdu1 = "(CDU/CSU)";
        String cdu2 = "(CDU)";
        String cdu3 = "(CDU/CSU):";
        String cdu4 = "(CDU):";
        String cdu5 = "(CDU/CSU),";
        String cdu6 = "(CDU),";
        String spd1 = "(SPD)";
        String spd2 = "(SPD):";
        String spd3 = "(SPD),";
        String linke1 = "LINKE)";
        String linke2 = "LINKE):";
        String linke3 = "LINKE),";
        String linke4 = "(PDS/Linke";
        String linke5 = "(PDS)";
        String linke6 = "(PDS):";
        String linke7 = "(PDS),";
        String afd1 = "(AfD)";
        String afd2 = "(AfD):";
        String afd3 = "(AfD),";
        String grunen1 = "(GRÜNE)";
        String grunen2 = "(BÜNDNIS";
        String grunen3 = "(GRÜNE):";
        String grunen4 = "(GRÜNE),";
        String fdp1 = "(FDP):";
        String fdp2 = "(F.D.P.):";
        String fdp3 = "(FDP)";
        String fdp4 = "(F.D.P.)";
        String fdp5 = "(FDP),";
        String fdp6 = "(F.D.P.),";

        // Text wird tokenisiert:
        StringTokenizer tokenizer = new StringTokenizer(inText);
        String currenttext = "";

        Element firstSpeach = document.createElement("Speach");
        debatte.appendChild(firstSpeach);

        Element prevSpeach = firstSpeach;

        //Vorsitzende/Vorsituender, die/der Spricht:
        Element firstpartei = document.createElement("Partei");
        firstSpeach.appendChild(firstpartei);
        firstpartei.appendChild(document.createTextNode("PräsidentIn"));

        // Alle Tokens einer Parlamentsdebatte werden durchlaufen und nach relevanten tokens gesucht
        while (tokenizer.hasMoreTokens()) {
          String currentToken = tokenizer.nextToken();

          //current token == (CDU/CSU)
          if (cdu1.equals(currentToken) || cdu2.equals(currentToken) || cdu3.equals(currentToken)
              || cdu4.equals(currentToken) || cdu5.equals(currentToken) || cdu6.equals(currentToken)) {

            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("CDU/CSU"));

            prevSpeach = speach;

            currenttext = "CDU/CSU ";

            //current token == (SPD)
          } else if (spd1.equals(currentToken) || spd2.equals(currentToken) || spd3.equals(currentToken)) {
            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("SPD"));

            prevSpeach = speach;
            currenttext = "SPD ";

            //current token == (FDP)
          } else if (fdp1.equals(currentToken) || fdp2.equals(currentToken) || fdp3.equals(currentToken) || fdp4.equals(currentToken) || fdp5.equals(currentToken) || fdp6.equals(currentToken)) {
            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("FDP"));

            prevSpeach = speach;
            currenttext = "FDP ";

            //current token == (BÜNDNIS
          } else if (grunen1.equals(currentToken) || grunen2.equals(currentToken) || grunen3.equals(currentToken) || grunen4.equals(currentToken)) {
            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("BÜNDNIS 90/DIE GRÜNEN"));

            prevSpeach = speach;
            currenttext = " 90/DIE GRÜNEN) ";

            //current token == (LINKE)
          } else if (linke1.equals(currentToken) || linke2.equals(currentToken) || linke3.equals(currentToken) || linke4.equals(currentToken) || linke5.equals(currentToken) || linke6.equals(currentToken) || linke7.equals(currentToken)) {
            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("DIE LINKE"));

            prevSpeach = speach;
            currenttext = " LINKE) ";

            // //current token == (AfD)
          } else if (afd1.equals(currentToken) || afd2.equals(currentToken) || afd3.equals(currentToken)) {
            Element text = document.createElement("Text");
            prevSpeach.appendChild(text);
            text.appendChild(document.createTextNode(currenttext));

            Element speach = document.createElement("Speach");
            debatte.appendChild(speach);

            Element partei = document.createElement("Partei");
            speach.appendChild(partei);
            partei.appendChild(document.createTextNode("AfD"));

            prevSpeach = speach;
            currenttext = "AfD ";

          } else {
            currenttext = currenttext + " " + currentToken;
          }
        }

        //letzte Partei, die Spricht
        Element lasttext = document.createElement("Text");
        prevSpeach.appendChild(lasttext);
        lasttext.appendChild(document.createTextNode(currenttext));

        //xml Datei erstellen und abspeichern in file System:
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(
            new File("files/output/CleanDok/" + directory + "/" + periode + "/Clean" + filename));

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.transform(domSource, streamResult);

      }
    }
  }
}
