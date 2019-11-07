package atividade2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Atividade2 {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //getting xml file
        Document doc = (Document) builder.parse("src\\FeiraDeSantana.xml");
        //getting the node tags who is inside the file (check the file to understand)
        NodeList everyNode = doc.getElementsByTagName("node");
        //we will put the instances of establishments here, in a JSON array
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < everyNode.getLength(); i++){
            Node node = everyNode.item(i);
            //if the Node have something more than just tags inside it
            if(node.getNodeType() == Node.ELEMENT_NODE){
                //converting node to element
                Element element = (Element) node;
                //getting tag "tag" inside the node
                NodeList tags = element.getElementsByTagName("tag");
                
                //referêncies to what we have to get into the node
                boolean hasAmenity = false;
                boolean hasName = false;
                String auxType = null;
                String auxName = null;
                
                //check is this node have "amenity" and "name"
                for (int j = 0; j < tags.getLength(); j++) {
                    Element e = (Element) tags.item(j);
                    switch (e.getAttribute("k")){
                        case "amenity": 
                            hasAmenity = true;
                            auxType = e.getAttribute("v");
                            break;
                        case "name":
                            hasName = true;
                            auxName = e.getAttribute("v");
                            break;
                        default:
                            break;
                    }

                }
                
                //if it get the amenity and the name, add this to the Json Array
                if(hasAmenity && hasName){
                    jsonArray.put(nodeToJson(node, auxType, auxName));
                }

            }
        }
        
        //encapsulate the array in a new json object
        JSONObject total = new JSONObject();
        total.accumulate("estabelecimentos", jsonArray);
        jsonToFile(total);
    }

    //informations we want in the json data struct for each establishment
    private static JSONObject nodeToJson(Node node, String type, String name) {
        JSONObject no = new JSONObject();
        Element element = (Element) node;
        no.accumulate("name", name);
        no.accumulate("type", type);
        
        JSONObject point = new JSONObject();
        point.accumulate("lat", Double.valueOf(element.getAttribute("lat")));
        point.accumulate("lon", Double.valueOf(element.getAttribute("lon")));
        
        no.accumulate("point", point);
        return no;
    }
    
    //creating a json file with the result
    private static void jsonToFile(JSONObject object) throws IOException{
        File file = new File("estabelecimentosOSM.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(object.toString());
            writer.flush();
        }
    }
}
