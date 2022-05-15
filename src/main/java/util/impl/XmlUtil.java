package util.impl;

import exception.MyDocumentException;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * @author cmc
 */
public abstract class XmlUtil {

    public static JSON xmlToJson(String xmlString){
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(xmlString);
        return json;
    }

    public static JSON xmlToJson(Document xmlDocument){
        return xmlToJson(xmlDocument.toString());
    }

    public static String jsonToXml(String jsonString){
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
    }

    public static String jsonToXml(JSON json, String topName){
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setObjectName(topName);
        xmlSerializer.setTypeHintsEnabled(false);
        return xmlSerializer.write(json);
    }

    public static Document strToXml(String xmlStr) throws MyDocumentException {

        Document doc;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new MyDocumentException("无法转换到Document,请检查xml文件格式:" + e.getMessage());
        }

        return doc;
    }
}
