package org.mynah.oblatum.sql;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mynah.oblatum.util.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SqlMap implements SqlMapOperations {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());
    public static final String KEY_NAMESPACE = "namespace";
    public static final String KEY_ID = "id";
    public static final String KEY_SQL = "sql";
    private String locationPattern = "classpath:*.*sql.xml";
    private final Map<String, String> sqlMap = new HashMap<String, String>();

    public SqlMap(String locationPattern) throws IOException, ParserConfigurationException, SAXException {
        setLocationPattern(locationPattern);
        loadResource();
    }

    public SqlMap() throws ParserConfigurationException, SAXException, IOException {
        loadResource();
    }

    public String getLocationPattern() {
        return locationPattern;
    }

    public void setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public void loadResource() throws IOException, ParserConfigurationException, SAXException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(getLocationPattern());
        for (int i = 0; i < resources.length; i++) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            resolverResource(resources[i], builder);
        }
    }

    public void resolverResource(Resource resource, DocumentBuilder builder) throws IOException, SAXException {
        Document document = builder.parse(resource.getInputStream());
        Element root = document.getDocumentElement();
        String namespace = root.getAttribute(KEY_NAMESPACE);
        NodeList nodes = root.getElementsByTagName(KEY_SQL);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                String id = element.getAttribute(KEY_ID);
                String sql = getText(element);
                String key = namespace + Constants.DOT + id;
                if (sqlMap.containsKey(key)) {
                    logger.error(key + " already exists!!!");
                }
                sqlMap.put(key, sql);
            }
        }
    }

    public String getText(Element element) {
        String text = null;
        NodeList nodes = element.getChildNodes();
        int length = nodes.getLength();
        if (length == 1) {
            text = element.getFirstChild().getNodeValue().trim();
        } else {
            for (int i = 0; i < length; i++) {
                Node node = nodes.item(i);
                if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
                    text = node.getNodeValue().trim();
                }
            }
        }
        return text;
    }

    @Override
    public String get(String key) {
        return sqlMap.get(key);
    }

}
