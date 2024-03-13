package uk.ac.soton.comp2211.runwayredeclaration.XMLloader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Airport;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Runway;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class xmFileLoader {

    static File file = new File("src/main/resources/predefined/Airport.xml");

    public static ArrayList<Airport> loadAirports() {
        ArrayList<Airport> airports = new ArrayList<>();
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();



            NodeList airportList = doc.getElementsByTagName("airport");

            for (int i = 0; i < airportList.getLength(); i++){
                Node current_airport = airportList.item(i);


                if (current_airport.getNodeType() == Node.ELEMENT_NODE){
                    Element airportElement = (Element) current_airport;
                    Airport airport = new Airport(airportElement.getElementsByTagName("name").item(0).getTextContent());


                    NodeList runwayList = ((Element) current_airport).getElementsByTagName("runway");

                    for (int j = 0; j < runwayList.getLength(); j++){
                        Node current_runway = runwayList.item(j);

                        if (current_runway.getNodeType() == Node.ELEMENT_NODE){
                            Element runwayElement = (Element) current_runway;
                            Runway runway = new Runway(runwayElement.getElementsByTagName("name").item(0).getTextContent());

                            NodeList subRunwayList = ((Element) current_runway).getElementsByTagName("subRunway");

                            for (int n = 0; n < subRunwayList.getLength(); n++){
                                Node current_subRunway = subRunwayList.item(n);
                                if (current_subRunway.getNodeType() == Node.ELEMENT_NODE){
                                    Element subRunwayElement = (Element) current_subRunway;
                                    String designator = subRunwayElement.getElementsByTagName("designator").item(0).getTextContent();
                                    double tora = Double.parseDouble(subRunwayElement.getElementsByTagName("tora").item(0).getTextContent());
                                    double toda = Double.parseDouble(subRunwayElement.getElementsByTagName("toda").item(0).getTextContent());
                                    double asda = Double.parseDouble(subRunwayElement.getElementsByTagName("asda").item(0).getTextContent());
                                    double lda = Double.parseDouble(subRunwayElement.getElementsByTagName("lda").item(0).getTextContent());
                                    double displacedThreshold = Double.parseDouble(subRunwayElement.getElementsByTagName("displacedThreshold").item(0).getTextContent());
                                    double clearwayLength = Double.parseDouble(subRunwayElement.getElementsByTagName("clearwayLength").item(0).getTextContent());
                                    double stopwayLength = Double.parseDouble(subRunwayElement.getElementsByTagName("stopwayLength").item(0).getTextContent());
                                    double stripEndLength = Double.parseDouble(subRunwayElement.getElementsByTagName("stripEndLength").item(0).getTextContent());
                                    double blastProtection = Double.parseDouble(subRunwayElement.getElementsByTagName("blastProtection").item(0).getTextContent());

                                    SubRunway subRunway = new SubRunway(designator, tora, toda, asda, lda, displacedThreshold, clearwayLength, stopwayLength, stripEndLength, blastProtection);
                                    runway.addSubRunway(subRunway);
                                }
                            }

                            airport.addRunway(runway);
                        }

                    }

                    airports.add(airport);

                }






            }


        } catch (Exception e) {
            e.printStackTrace();
        }




        return airports;
    }
}
