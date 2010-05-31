package dataSources;

import messages.CfpQuery;
import messages.StringAttributeList;

public class GoogleScholarQuery extends DataSourceQuery{
    public static final String DEFAULT_HREF =
            "http://scholar.google.com/scholar?";
    //Google Scholar query language
    private static final String KEYWORD = "q=";
    private static final String AUTHOR = "+author%3A";
    private static final String JOURNAL = "&as_publication=";
    private static final String SUBJECT = "&as_subj=";
    private static final String FROM_YEAR = "&as_ylo=";
    private static final String TO_YEAR = "&as_yhi=";
    private static final String PAPERS_COUNT = "&num=";
    private static final String AT_LEAST_SUMMARIES = "&as_vis=1";

    public GoogleScholarQuery(CfpQuery c) {
        cfp = c;
        request = cfp.getPublicationRequest();
        href = DEFAULT_HREF;
        parser = new GoogleScholarParser();
    }

    @Override
    public void calculateHref() {
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
        href += KEYWORD;
        if(keywords != null) {
            for(String keyword : keywords.getAttribute()) {
                href += "+" + normalize(keyword,'+');
            }
        }
       keywords = request.getAbstractKeywordList();
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
                href += AUTHOR + normalize(author,'+');
            }
        }
    }

    private void addJournal() {
        String journalName = request.getJournalName();
        if(journalName != null) {
            href += JOURNAL + normalize(journalName,'+');
        }
    }

    private void addSubject() {
        String subjArea = request.getSubjectArea();
        if(subjArea != null) {
            href += SUBJECT + normalize(subjArea,'+');
        }
    }

    private void addFromYear() {
        Integer fromYear =request.getFromYear();
        if(fromYear != null) {
            href += FROM_YEAR + fromYear.toString();
        }
    }

    private void addToYear() {
        Integer toYear =request.getToYear();
        if(toYear != null) {
            href += TO_YEAR + toYear.toString();
        }
    }

    private void addPapersCont() {
        Integer count =cfp.getResultsNumber();
        if(count != null && count != 0) {
            href += PAPERS_COUNT + count.toString();
        }
    }

    public static String normalize(String s, char replacement) {
        String str = s;
        str.trim();
        char [] strToChar = str.toCharArray();
        for(int i = 0; i < strToChar.length; i++) {
            char c = strToChar[i];
            if(!Character.isLetterOrDigit(c))
                strToChar[i] = replacement;
        }
        return String.valueOf(strToChar);
    }

    private void addAtLeastSummaries() {
        href += AT_LEAST_SUMMARIES;
    }
}
