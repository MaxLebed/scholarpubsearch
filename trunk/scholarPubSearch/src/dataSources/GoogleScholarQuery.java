package dataSources;

import messages.CfpQuery;
import messages.PublicationRequest;
import messages.StringAttributeList;
import java.io.IOException;
import messages.Propose;

public class GoogleScholarQuery {
    public static final String DEFAULT_HREF =
            "http://scholar.google.com/scholar?";
    private CfpQuery cfp;
    private PublicationRequest request;
    public String href;

    public GoogleScholarQuery(CfpQuery c) {
        cfp = c;
        request = cfp.getPublicationRequest();
        href = DEFAULT_HREF;
    }

    public Propose perform() {
        getHref();
        try {
            GoogleScholarParser p = new GoogleScholarParser();
            return GoogleScholarParser.parse(href, cfp.getResultsNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getHref() {
        addKeywords();
        addAuthors();
        addJournal();
        addSubject();
        addFromYear();
        addToYear();
        addPapersCont();
        addAtLeastSummaries();
    }

    private void addKeywords() {
        StringAttributeList keywords = request.getTitleKeywordList();
        href += "q=";
        if(keywords != null) {
            for(String keyword : keywords.getAttribute()) {
                href += "+" + normalize(keyword,'+');
            }
        }
    }

    private void addAuthors() {
        StringAttributeList authors = request.getAuthorList();
        if(authors != null) {
            for(String author : authors.getAttribute()) {
                href += "+author%3A" + normalize(author,'+');
            }
        }
    }

    private void addJournal() {
        String journalName = request.getJournalName();
        if(journalName != null) {
            href += "&as_publication=" + normalize(journalName,'+');
        }
    }

    private void addSubject() {
        String subjArea = request.getSubjectArea();
        if(subjArea != null) {
            href += "&as_subj=" + normalize(subjArea,'+');
        }
    }

    private void addFromYear() {
        Integer fromYear =request.getFromYear();
        if(fromYear != null) {
            href += "&as_ylo=" + fromYear.toString();
        }
    }

    private void addToYear() {
        Integer toYear =request.getToYear();
        if(toYear != null) {
            href += "&as_yhi=" + toYear.toString();
        }
    }

    private void addPapersCont() {
        Integer count =cfp.getResultsNumber();
        if(count != null && count != 0) {
            href += "&num=" + count.toString();
        }
    }

    public static String normalize(String s, char replacement) {
        String str = s;
        str.trim();
        char [] strToChar = str.toCharArray();
        for(int i = 0; i < strToChar.length; i++) {
            char c = strToChar[i];
            if(Character.isSpaceChar(c))
                strToChar[i] = replacement;
        }
        return String.valueOf(strToChar);
    }

    private void addAtLeastSummaries() {
        href += "&as_vis=1";
    }
}
