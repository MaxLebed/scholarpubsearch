package main;

import java.util.ArrayList;
import java.util.Hashtable;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;

/**
    Import agents from config-file
 */
public class AgentImport {
    private final static String INPUT_AGENT_TAG = "input";
    private final static String PREFERENCE_AGENT_TAG = "preference";
    private final static String CLASSIFIER_AGENT_TAG = "classifier";
    private final static String ARXIV_AGENT_TAG = "arxiv";
    private final static String GOOGLE_SCHOLAR_AGENT_TAG = "google";
    private final static String NAME_ATTRIBUTE = "name";
    private final static String AGENT_ATTRIBUTE = "agent";
    private final static String FILE_ATTRIBUTE = "file";

    private ArrayList<String> userInputAgents;
    private ArrayList<String> userPreferenceAgents;
    private ArrayList<String> classifiers;
    private ArrayList<String> arxivOrgAgents;
    private ArrayList<String> googleScholarAgents;
    private Hashtable<String, String> uiPrefAgent;
    private Hashtable<String, String> uiPrefFile;

    public AgentImport(String uri) {
        AgentReader reader = new AgentReader();
        reader.parse(uri);
    }

    private class AgentReader extends DefaultHandler {

        private String ui;
        private String up;
        private String clas;
        private String arxiv;
        private String gs;
        private String uiPref;
        private String uiFile;

        public void parse(String uri) {
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser stringReader = spf.newSAXParser();
                stringReader.parse(uri, this);

            } catch (SAXException e) {
                System.out.println("Error in agent description: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Reading file failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Parsing agents failed:" + e.getMessage());
            }
        }

        @Override
        public void startDocument() {
            userInputAgents = new ArrayList<String>();
            userPreferenceAgents = new ArrayList<String>();
            classifiers = new ArrayList<String>();
            arxivOrgAgents = new ArrayList<String>();
            googleScholarAgents = new ArrayList<String>();
            uiPrefFile = new Hashtable<String, String>();
            uiPrefAgent = new Hashtable<String, String>();
        }

        @Override
        public void startElement(String namespaceURI, String localName,
                String rawName, Attributes attrs) {
            if (rawName.equals(INPUT_AGENT_TAG)) {
                    ui = attrs.getValue(NAME_ATTRIBUTE);
                    uiPref = attrs.getValue(AGENT_ATTRIBUTE);
                    uiFile = attrs.getValue(FILE_ATTRIBUTE);
                } else if (rawName.equals(PREFERENCE_AGENT_TAG)) {
                    up = attrs.getValue(NAME_ATTRIBUTE);
                } else if (rawName.equals(CLASSIFIER_AGENT_TAG)) {
                    clas = attrs.getValue(NAME_ATTRIBUTE);
                } else if (rawName.equals(ARXIV_AGENT_TAG)) {
                    arxiv = attrs.getValue(NAME_ATTRIBUTE);
                } else if (rawName.equals(GOOGLE_SCHOLAR_AGENT_TAG)) {
                    gs = attrs.getValue(NAME_ATTRIBUTE);
                }
        }

        @Override
        public void endElement(String namespaceURI, String localName,
                String rawName) {
            if (rawName.equals(INPUT_AGENT_TAG)) {
                userInputAgents.add(ui);
                uiPrefAgent.put(ui, uiPref);
                uiPrefFile.put(ui, uiFile);
            } else if (rawName.equals(PREFERENCE_AGENT_TAG)) {
                userPreferenceAgents.add(up);
            } else if (rawName.equals(CLASSIFIER_AGENT_TAG)) {
                classifiers.add(clas);
            } else if (rawName.equals(ARXIV_AGENT_TAG)) {
                arxivOrgAgents.add(arxiv);
            } else if (rawName.equals(GOOGLE_SCHOLAR_AGENT_TAG)) {
                googleScholarAgents.add(gs);
            }
        }
    }

    public String[] getUserInputAgents() {
        String[] UserInputAgents = new String[userInputAgents.size()];
        userInputAgents.toArray(UserInputAgents);
        return UserInputAgents;
    }

    public String[] getUserPreferenceAgents() {
        String[] UserPreferenceAgents = new String[userPreferenceAgents.size()];
        userPreferenceAgents.toArray(UserPreferenceAgents);
        return UserPreferenceAgents;
    }

    public String[] getClassifiers() {
        String[] Classifiers = new String[classifiers.size()];
        classifiers.toArray(Classifiers);
        return Classifiers;
    }

    public String[] getArxivOrgAgents() {
        String[] ArxivOrgAgents = new String[arxivOrgAgents.size()];
        arxivOrgAgents.toArray(ArxivOrgAgents);
        return ArxivOrgAgents;
    }

    public String[] getGoogleScholarAgents() {
        String[] GoogleScholarAgents = new String[googleScholarAgents.size()];
        googleScholarAgents.toArray(GoogleScholarAgents);
        return GoogleScholarAgents;
    }

    public String getPref(String ui) {
        return uiPrefAgent.get(ui);
    }

    public String getFile(String ui) {
        return uiPrefFile.get(ui);
    }
}
