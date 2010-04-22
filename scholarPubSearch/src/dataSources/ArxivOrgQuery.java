package dataSources;

import messages.CfpQuery;
import messages.PublicationRequest;
import messages.StringAttributeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.bind.JAXBException;
import atom.ArxivResponse;
import atom.FeedType;

public class ArxivOrgQuery {

    public static final String DEFAULT_HREF =
            "http://export.arxiv.org/api/query?search_query=";
    private CfpQuery cfp;
    private PublicationRequest request;
    private String href;

    public ArxivOrgQuery(CfpQuery c) {
        cfp = c;
        request = c.getPublicationRequest();
        href = DEFAULT_HREF;
    }

    public FeedType perform() {
        calcHref();
        try {
            String responseXML = "";
            URL url = new URL(href);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseXML += line;
            }
            reader.close();
            return ArxivResponse.unmarshal(responseXML);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JAXBException je){
            je.printStackTrace();
        }
        return null;
    }

    private void calcHref() {
        addAuthors();
        addKeywords("ti:", request.getTitleKeywordList());
        addKeywords("abs:", request.getAbstractKeywordList());
        addJournal();
        addSubject();
        addPapersCount();
    }

    private void addAuthors() {
        StringAttributeList authors =
                request.getAuthorList();
        if(authors != null) {
            for(String authorName : authors.getAttribute()) {
                addAnd();
                href += "au:" + GoogleScholarQuery.normalize(authorName,'_');
            }
        }
    }

    private void addKeywords(String prefix, StringAttributeList keywords) {
        if(keywords != null) {
            for(String keyword : keywords.getAttribute()) {
                addAnd();
                href += prefix + GoogleScholarQuery.normalize(keyword,'_');
            }
        }
    }

    private void addJournal() {
        String journalName = request.getJournalName();
        if(journalName != null) {
            addAnd();
            href += "jr:" + GoogleScholarQuery.normalize(journalName,'_');
        }
    }

    private void addSubject() {
        String subjArea = request.getSubjectArea();
        if(subjArea != null) {
            addAnd();
            href += "cat:" + subjArea;
        }
    }

    private void addAnd() {
        if (!href.equals(DEFAULT_HREF)) {
            href += "+AND+";
        }
    }

    private void addPapersCount() {
        String count = Integer.toString(cfp.getResultsNumber());
        if(count != null && !count.equals("0")) {
            href += "&max_results=" + count;
        }
    }
}
