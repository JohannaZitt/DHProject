import java.io.File;
import java.io.IOException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SortByParty {

  //READ.ME
  //INPUT: CleanDok Dokumente (file/output/CleanDok)
  //OUTPUT: Redebeiträge nach Partei geordnet, je Wahlperiode (file/output/ParteiSpezifischeDateien)

  //Die Dokumente, die man nach der DokCeaning Klasse erhält werden nun Sortiert nach Parteien pro Wahlperiode sortiert

  public static void main(String[] args)
      throws IOException, SAXException, ParserConfigurationException, TransformerException {

    // change directory here
    String directory = "NGE";

    File dirPath = new File("C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/" + directory);
    File[] dirPaths = dirPath.listFiles();

    // für jede Wahlperiode
    for (int k = 0; k < dirPaths.length; k++) {

      String periode = dirPaths[k].getName();
      File directoryPath = new File(
          "C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/" + directory + "/"
              + periode);
      File[] directoryPaths = directoryPath.listFiles();

      //erstellen der verschiedenen Dokumente pro Partei
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document CDUdoc = db.newDocument();
      Document SPDdoc = db.newDocument();
      Document Grunedoc = db.newDocument();
      Document Linkedoc = db.newDocument();
      Document FDPdoc = db.newDocument();
      Document AfDdoc = db.newDocument();

      Element CDUroot = CDUdoc.createElement("CDUdoc");
      CDUdoc.appendChild(CDUroot);
      Element CDUparty = CDUdoc.createElement("Party");
      CDUroot.appendChild(CDUparty);
      CDUparty.appendChild(CDUdoc.createTextNode("CDU"));

      Element SPDroot = SPDdoc.createElement("SPDdoc");
      SPDdoc.appendChild(SPDroot);
      Element SPDparty = SPDdoc.createElement("Party");
      SPDroot.appendChild(SPDparty);
      SPDparty.appendChild(SPDdoc.createTextNode("SPD"));

      Element Gruneroot = Grunedoc.createElement("Grunedoc");
      Grunedoc.appendChild(Gruneroot);
      Element Gruneparty = Grunedoc.createElement("Party");
      Gruneroot.appendChild(Gruneparty);
      Gruneparty.appendChild(Grunedoc.createTextNode("Grune"));

      Element Linkeroot = Linkedoc.createElement("Linkedoc");
      Linkedoc.appendChild(Linkeroot);
      Element Linkeparty = Linkedoc.createElement("Party");
      Linkeroot.appendChild(Linkeparty);
      Linkeparty.appendChild(Linkedoc.createTextNode("Linke"));

      Element FDProot = FDPdoc.createElement("FDPdoc");
      FDPdoc.appendChild(FDProot);
      Element FDPparty = FDPdoc.createElement("Party");
      FDProot.appendChild(FDPparty);
      FDPparty.appendChild(FDPdoc.createTextNode("FDP"));

      Element AfDroot = AfDdoc.createElement("AfDdoc");
      AfDdoc.appendChild(AfDroot);
      Element AfDparty = AfDdoc.createElement("Party");
      AfDroot.appendChild(AfDparty);
      AfDparty.appendChild(AfDdoc.createTextNode("AfD"));

      assert directoryPaths != null;

      // Für alle Parlamentsdebatten
      for (int i = 0; i < directoryPaths.length; i++) {

        StringBuilder CDUText = new StringBuilder();
        StringBuilder SPDText = new StringBuilder();
        StringBuilder GruneText = new StringBuilder();
        StringBuilder LinkeText = new StringBuilder();
        StringBuilder FDPText = new StringBuilder();
        StringBuilder AfDText = new StringBuilder();

        //aus inputfile ensprechende .xml nodes lesen
        String filename = directoryPaths[i].getName();
        String name = filename.substring(0, filename.length() - 4);
        File inputfile = new File(
            "C:/Users/jojoz/IdeaProjects/DHProject/files/output/CleanDok/" + directory + "/"
                + periode + "/" + filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputfile);
        NodeList nodes = doc.getElementsByTagName("Speach");

        // für jeden Knoten "Speach" in Parlamentsdebatte
        for (int j = 0; j < nodes.getLength(); j++) {

          Node node = nodes.item(j);
          Element elem = (Element) node;

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("CDU/CSU")) {
            CDUText.append(" ").append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("SPD")) {
            SPDText.append(" ").append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent()
              .equals("BÜNDNIS 90/DIE GRÜNEN")) {
            GruneText.append(" ")
                .append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("DIE LINKE")) {
            LinkeText.append(" ")
                .append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("FDP")) {
            FDPText.append(" ").append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

          if (elem.getElementsByTagName("Partei").item(0).getTextContent().equals("AfD")) {
            AfDText.append(" ").append(elem.getElementsByTagName("Text").item(0).getTextContent());
          }

        }

        // erstellen der neuen Dokumente
        Element textNodeCDU = CDUdoc.createElement("textNode");
        CDUroot.appendChild(textNodeCDU);
        Element idCDU = CDUdoc.createElement("ID");
        textNodeCDU.appendChild(idCDU);
        idCDU.appendChild(CDUdoc.createTextNode(name));
        Element textCDU = CDUdoc.createElement("text");
        textNodeCDU.appendChild(textCDU);
        textCDU.appendChild(CDUdoc.createTextNode(CDUText.toString()));

        Element textNodeSPD = SPDdoc.createElement("textNode");
        SPDroot.appendChild(textNodeSPD);
        Element idSPD = SPDdoc.createElement("ID");
        textNodeSPD.appendChild(idSPD);
        idSPD.appendChild(SPDdoc.createTextNode(name));
        Element textSPD = SPDdoc.createElement("text");
        textNodeSPD.appendChild(textSPD);
        textSPD.appendChild(SPDdoc.createTextNode(SPDText.toString()));

        Element textNodeGrune = Grunedoc.createElement("textNode");
        Gruneroot.appendChild(textNodeGrune);
        Element idGrune = Grunedoc.createElement("ID");
        textNodeGrune.appendChild(idGrune);
        idGrune.appendChild(Grunedoc.createTextNode(name));
        Element textGrune = Grunedoc.createElement("text");
        textNodeGrune.appendChild(textGrune);
        textGrune.appendChild(Grunedoc.createTextNode(GruneText.toString()));

        Element textNodeLinke = Linkedoc.createElement("textNode");
        Linkeroot.appendChild(textNodeLinke);
        Element idLinke = Linkedoc.createElement("ID");
        textNodeLinke.appendChild(idLinke);
        idLinke.appendChild(Linkedoc.createTextNode(name));
        Element textLinke = Linkedoc.createElement("text");
        textNodeLinke.appendChild(textLinke);
        textLinke.appendChild(Linkedoc.createTextNode(LinkeText.toString()));

        Element textNodeFDP = FDPdoc.createElement("textNode");
        FDProot.appendChild(textNodeFDP);
        Element idFDP = FDPdoc.createElement("ID");
        textNodeFDP.appendChild(idFDP);
        idFDP.appendChild(FDPdoc.createTextNode(name));
        Element textFDP = FDPdoc.createElement("text");
        textNodeFDP.appendChild(textFDP);
        textFDP.appendChild(FDPdoc.createTextNode(FDPText.toString()));

        Element textNodeAfD = AfDdoc.createElement("textNode");
        AfDroot.appendChild(textNodeAfD);
        Element idAfD = AfDdoc.createElement("ID");
        textNodeAfD.appendChild(idAfD);
        idAfD.appendChild(AfDdoc.createTextNode(name));
        Element textAfD = AfDdoc.createElement("text");
        textNodeAfD.appendChild(textAfD);
        textAfD.appendChild(AfDdoc.createTextNode(AfDText.toString()));
      }

      //xml Datei erstellen und abspeichern in file System:
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

      DOMSource CDUdom = new DOMSource(CDUdoc);
      StreamResult CDUstreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/CDU" + periode + directory
              + ".xml"));
      transformer.transform(CDUdom, CDUstreamResult);

      DOMSource SPDdom = new DOMSource(SPDdoc);
      StreamResult SPDstreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/SPD" + periode + directory
              + ".xml"));
      transformer.transform(SPDdom, SPDstreamResult);

      DOMSource Grunedom = new DOMSource(Grunedoc);
      StreamResult GrunestreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/Grune" + periode + directory
              + ".xml"));
      transformer.transform(Grunedom, GrunestreamResult);

      DOMSource Linkedom = new DOMSource(Linkedoc);
      StreamResult LinkestreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/Linke" + periode + directory
              + ".xml"));
      transformer.transform(Linkedom, LinkestreamResult);

      DOMSource FDPdom = new DOMSource(FDPdoc);
      StreamResult FDPstreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/FDP" + periode + directory
              + ".xml"));
      transformer.transform(FDPdom, FDPstreamResult);

      DOMSource AfDdom = new DOMSource(AfDdoc);
      StreamResult AfDstreamResult = new StreamResult(new File(
          "files/output/ParteiSpezifischeDateien/" + directory + "/" + periode + "/AfD" + periode + directory
              + ".xml"));
      transformer.transform(AfDdom, AfDstreamResult);
    }
  }
}
