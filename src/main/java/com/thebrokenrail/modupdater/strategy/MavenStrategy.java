package com.thebrokenrail.modupdater.strategy;

import com.thebrokenrail.modupdater.ModUpdater;
import com.thebrokenrail.modupdater.api.UpdateStrategy;
import com.thebrokenrail.modupdater.data.ModUpdate;
import com.thebrokenrail.modupdater.util.Util;
import com.vdurmont.semver4j.Semver;
import me.flashyreese.fabricmm.schema.ModUpdaterConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MavenStrategy implements UpdateStrategy {
    private final DocumentBuilder builder;

    public MavenStrategy() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public ModUpdate run(ModUpdaterConfig obj, String oldVersion, String name, String id) {
        String repository = obj.getRepository();
        String group = obj.getGroup();
        String artifact = obj.getArtifact();

        String mavenRoot = String.format("%s/%s/%s", repository, group.replaceAll("\\.", "/"), artifact);

        String data;
        try {
            data = Util.urlToString(mavenRoot + "/maven-metadata.xml");
        } catch (IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        Document doc;
        try (InputStream source = new ByteArrayInputStream(data.getBytes())) {
            doc = builder.parse(source);
        } catch (IOException | SAXException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList versions;
        try {
            versions = (NodeList) xPath.compile("/metadata/versioning/versions/*").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        boolean strict = isStrict(obj);

        String newestVersion = null;
        for (int i = 0; i < versions.getLength(); i++) {
            Node node = versions.item(i);

            String version = node.getTextContent();
            if (Util.isVersionCompatible(id, version, strict)) {
                if (newestVersion != null) {
                    if (new Semver(version).compareTo(new Semver(newestVersion)) > 0){
                        newestVersion = version;
                    }
                } else {
                    newestVersion = version;
                }
            }
        }

        if (newestVersion != null) {
            if (new Semver(newestVersion).compareTo(new Semver(oldVersion)) > 0){
                return new ModUpdate(oldVersion, newestVersion, String.format("%s/%s/%s-%s%s", mavenRoot, newestVersion, artifact, newestVersion, Util.JAR_EXTENSION), name);
            }else{
                return null;
            }
        } else {
            return null;
        }
    }
}
