package org.kettle.ext.cluster;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.www.SslConfiguration;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @description: 从服务器编解码器
 * @author: ZX
 * @date: 2018/11/20 18:07
 */
public class SlaveServerCodec {

    public static JsonObject encode(SlaveServer slaveServer) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("name", slaveServer.getName());
        jsonObject.put("hostname", slaveServer.getHostname());
        jsonObject.put("port", slaveServer.getPort());
        jsonObject.put("webAppName", slaveServer.getWebAppName());
        jsonObject.put("username", slaveServer.getUsername());
        jsonObject.put("password", slaveServer.getPassword());
        jsonObject.put("proxy_hostname", slaveServer.getProxyHostname());
        jsonObject.put("proxy_port", slaveServer.getProxyPort());
        jsonObject.put("non_proxy_hosts", slaveServer.getNonProxyHosts());
        jsonObject.put("master", slaveServer.isMaster() ? "Y" : "N");
        jsonObject.put("sslMode", slaveServer.isSslMode() ? "Y" : "N");
        if (slaveServer.getSslConfig() != null) {

        }

        return jsonObject;
    }

    public static SlaveServer decode(JsonObject jsonObject) throws ParserConfigurationException, SAXException, IOException {
        StringBuilder xml = new StringBuilder();

        xml.append("<").append(SlaveServer.XML_TAG).append(">");

        xml.append(XMLHandler.addTagValue("name", jsonObject.optString("name"), false));
        xml.append(XMLHandler.addTagValue("hostname", jsonObject.optString("hostname"), false));
        xml.append(XMLHandler.addTagValue("port", jsonObject.optString("port"), false));
        xml.append(XMLHandler.addTagValue("webAppName", jsonObject.optString("webAppName"), false));
        xml.append(XMLHandler.addTagValue("username", jsonObject.optString("username"), false));
        xml.append(XMLHandler.addTagValue("password", jsonObject.optString("password"), false));
        xml.append(XMLHandler.addTagValue("proxy_hostname", jsonObject.optString("proxy_hostname"), false));
        xml.append(XMLHandler.addTagValue("proxy_port", jsonObject.optString("proxy_port"), false));
        xml.append(XMLHandler.addTagValue("non_proxy_hosts", jsonObject.optString("non_proxy_hosts"), false));
        xml.append(XMLHandler.addTagValue("master", jsonObject.optString("master"), false));
        xml.append(XMLHandler.addTagValue(SlaveServer.SSL_MODE_TAG, jsonObject.optString("sslMode"), false));

        JsonObject sslConfig = jsonObject.optJSONObject("sslConfig");
        if (sslConfig != null) {
            xml.append("<").append(SslConfiguration.XML_TAG).append(">");
            String keyStore = sslConfig.optString("keyStore");
            if (StringUtils.hasText(keyStore)) {
                xml.append(XMLHandler.addTagValue("keyStore", keyStore, false));
            }

            String keyStorePassword = sslConfig.optString("keyStorePassword");
            if (StringUtils.hasText(keyStorePassword)) {
                xml.append(XMLHandler.addTagValue("keyStorePassword", keyStorePassword, false));
            }

            String keyPassword = sslConfig.optString("keyPassword");
            if (StringUtils.hasText(keyPassword)) {
                xml.append(XMLHandler.addTagValue("keyPassword", keyPassword, false));
            }
            xml.append("</").append(SslConfiguration.XML_TAG).append(">");
        }

        xml.append("</").append(SlaveServer.XML_TAG).append(">");
        StringReader sr = new StringReader(xml.toString());
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        SlaveServer slaveServer = new SlaveServer(doc.getDocumentElement());
        return slaveServer;
    }

}
