package top.echovoid.common.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/5/17
 */
public class XmlToMapUtil {
    public static Map<String, String> xmlToMap(String xmlStr) throws Exception {
        Map<String, String> result = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 防止 XXE 攻击
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new java.io.ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            // 过滤文本节点（如换行符）
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result.put(node.getNodeName(), node.getTextContent());
            }
        }

        return result;
    }
}
