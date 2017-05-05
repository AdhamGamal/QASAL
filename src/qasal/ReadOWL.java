package qasal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadOWL {

    private final ArrayList<String> answers = new ArrayList<>();
    private final ArrayList<String> otherValues = new ArrayList<>();

    public ArrayList<String> semintic(String word) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(new File(".").getCanonicalPath() + "\\OWLFiles\\places-in-quran.owl");
            getLocation(doc.getChildNodes().item(0).getChildNodes(), word);
        } catch (IOException | ParserConfigurationException | SAXException e) {
        }
        return answers;
    }

    public ArrayList<String> otherValues(String word) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(new File(".").getCanonicalPath() + "\\OWLFiles\\places-in-quran.owl");
            getOtherValue(doc.getChildNodes().item(0).getChildNodes(), word);
        } catch (IOException | ParserConfigurationException | SAXException e) {
        }
        return otherValues;
    }

    private void getLocation(NodeList nodeList, String word) {
        Node tempNode;
        int size = nodeList.getLength();
        for (int i = 0; i < size; i++) {
            tempNode = nodeList.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                ObjectPropertyAssertionLocation(tempNode, word);
            }
        }
        if (answers.isEmpty()) {
            getOtherValue(nodeList, word);
            otherValues.forEach((otherValue) -> {
                getLocation2(nodeList, otherValue);
            });
        }
    }

    private void getLocation2(NodeList nodeList, String word) {
        Node tempNode;
        int size = nodeList.getLength();
        for (int i = 0; i < size; i++) {
            tempNode = nodeList.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                ObjectPropertyAssertionLocation(tempNode, word);
            }
        }
    }

    private void getOtherValue(NodeList nodeList, String word) {
        Node tempNode;
        int size = nodeList.getLength();
        for (int i = 0; i < size; i++) {
            tempNode = nodeList.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                SameIndividual(tempNode, word);
            }
        }
    }

    private void SameIndividual(Node node, String word) {
        if (node.getNodeName().equals("SameIndividual")) {
            NodeList nodeChilds = node.getChildNodes();
            Node child1 = nodeChilds.item(1).getAttributes().item(0);
            Node child2 = nodeChilds.item(3).getAttributes().item(0);
            if (removeOnt(child1.getNodeValue()).equals(word)) {
                otherValues.add(removeOnt(child2.getNodeValue()));
            } else if (removeOnt(child2.getNodeValue()).equals(word)) {
                otherValues.add(removeOnt(child1.getNodeValue()));
            }
        }
    }

    private void ObjectPropertyAssertionLocation(Node node, String word) {
        if (node.getNodeName().equals("ObjectPropertyAssertion")) {
            NodeList nodeChilds = node.getChildNodes();
            Node child1 = nodeChilds.item(1).getAttributes().item(0);
            Node child2 = nodeChilds.item(3).getAttributes().item(0);
            Node child3 = nodeChilds.item(5).getAttributes().item(0);
            if (removeOnt(child2.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("isLocated_in")) {
                answers.add(removeOnt(child2.getNodeValue()) + " تقع في " + removeOnt(child3.getNodeValue()));
            } else if (removeOnt(child2.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("is_inAreaOf")) {
                answers.add(removeOnt(child2.getNodeValue()) + " في منطقة " + removeOnt(child3.getNodeValue()));
            } else if (removeOnt(child2.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("isMountain_in")) {
                answers.add(removeOnt(child2.getNodeValue()) + " جبل في " + removeOnt(child3.getNodeValue()));
            } else if (removeOnt(child2.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("is_a")) {
                answers.add(removeOnt(child2.getNodeValue()) + " عبارة عن " + removeOnt(child3.getNodeValue()));
            } else if (removeOnt(child2.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("is_partOf")) {
                answers.add(removeOnt(child2.getNodeValue()) + " جزء من " + removeOnt(child3.getNodeValue()));
            } else if (removeOnt(child3.getNodeValue()).equals(word) && removeOnt(child1.getNodeValue()).equals("hasPart")) {
                answers.add(removeOnt(child2.getNodeValue()) + " جزء منها " + removeOnt(child3.getNodeValue()));
            }
        }
    }

    private String removeOnt(String str) {
        try {
            return str.substring(4);
        } catch (Exception e) {
            return str;
        }
    }
}
