
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

  //TODO:
  // - fertig

  public static void main(String[] args)
      throws ParserConfigurationException, TransformerException, IOException, SAXException {

    // Change here Wahlperiode
    String periode = "11";

    File directoryPath = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/input/" + periode);
    File[] directoryPaths = directoryPath.listFiles();

    for (int i = 0; i < directoryPaths.length; i++) {
      String filename = directoryPaths[i].getName();

      File file = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/input/" + periode + "/" + filename);

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

      // verschiedene Parteien, die auftauchen können:
      String cdu1 = "(CDU/CSU)";
      String cdu2 = "(CDU)";
      String cdu3 = "(CDU/CSU):";
      String cdu4 = "(CDU):";
      String spd1 = "(SPD)";
      String spd2 = "(SPD):";
      String linke1 = "LINKE)";
      String linke2 = "LINKE):";
      String afd1 = "(AfD)";
      String afd2 = "(AfD):";
      String grunen1 = "(GRÜNE)";
      String grunen2 = "(BÜNDNIS";
      String grunen3 = "(GRÜNE):";
      String fdp1 = "(FDP):";
      String fdp2 = "(F.D.P.):";
      String fdp3 = "(FDP)";
      String fdp4 = "(F.D.P.)";

      StringTokenizer tokenizer = new StringTokenizer(inText);
      String currenttext = "";

      //inText = inText.replaceAll("", "");

      //Vorsitzende/Vorsituender, die/der Spricht:
      Element firstpartei = document.createElement("Partei");
      debatte.appendChild(firstpartei);
      firstpartei.appendChild(document.createTextNode("PräsidentIn"));

      while (tokenizer.hasMoreTokens()) {

        String currentToken = tokenizer.nextToken();

        if (cdu1.equals(currentToken) || cdu2.equals(currentToken) || cdu3.equals(currentToken)
            || cdu4.equals(currentToken)) { //current token == (CDU/CSU)

          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("CDU/CSU"));

          currenttext = "CDU/CSU ";

        } else if (spd1.equals(currentToken) || spd2.equals(currentToken)) {//current token == (SPD)
          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("SPD"));

          currenttext = "SPD ";

        } else if (fdp1.equals(currentToken) || fdp2.equals(currentToken) || fdp3
            .equals(currentToken) || fdp4.equals(currentToken)) {//current token == (FDP)
          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("FDP"));

          currenttext = "FDP ";

        } else if (grunen1.equals(currentToken) || grunen2.equals(currentToken) || grunen3
            .equals(currentToken)) {//current token == (BÜNDNIS
          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("BÜNDNIS 90/DIE GRÜNEN"));

          currenttext = " 90/DIE GRÜNEN) ";

        } else if (linke1.equals(currentToken) || linke2
            .equals(currentToken)) {//current token == (LINKE)
          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("DIE LINKE"));

          currenttext = " LINKE) ";

        } else if (afd1.equals(currentToken) || afd2.equals(currentToken)) {
          Element text = document.createElement("Text");
          debatte.appendChild(text);
          text.appendChild(document.createTextNode(currenttext));

          Element partei = document.createElement("Partei");
          debatte.appendChild(partei);
          partei.appendChild(document.createTextNode("AfD"));

          currenttext = "AfD ";

        } else {
          currenttext = currenttext + " " + currentToken;
        }
      }

      //letzte Partei, die Spricht
      Element lasttext = document.createElement("Text");
      debatte.appendChild(lasttext);
      lasttext.appendChild(document.createTextNode(currenttext));

      //xml Datei erstellen und abspeichern in file System:
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File("files/output/CleanDok/" + periode + "/Clean" + filename));

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
      transformer.transform(domSource, streamResult);

    }
  }
}
