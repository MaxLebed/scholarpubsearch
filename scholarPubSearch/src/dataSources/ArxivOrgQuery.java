package dataSources;

import messages.CfpQuery;
import messages.StringAttributeList;

public class ArxivOrgQuery extends DataSourceQuery{

    public static final String DEFAULT_HREF =
            "http://export.arxiv.org/api/query?search_query=";
    //ARXIV query language
    private static final String TITLE_KEYWORD = "ti:";
    private static final String ABSTRACT_KEYWORD = "abs:";
    private static final String AUTHOR = "au:";
    private static final String JOURNAL = "jr:";
    private static final String SUBJECT = "cat:";
    private static final String PAPERS_COUNT = "&max_results=";
    private static final String AND= "+AND+";

    public ArxivOrgQuery(CfpQuery c) {
        cfp = c;
        request = c.getPublicationRequest();
        href = DEFAULT_HREF;
        parser = new AtomFeedReader();
    }

    @Override
    public void calculateHref() {
        addAuthors();
        addKeywords(TITLE_KEYWORD, request.getTitleKeywordList());
        addKeywords(ABSTRACT_KEYWORD, request.getAbstractKeywordList());
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
                href += AUTHOR + GoogleScholarQuery.normalize(authorName,'+');
            }
        }
    }

    private void addKeywords(String prefix, StringAttributeList keywords) {
        if(keywords != null) {
            for(String keyword : keywords.getAttribute()) {
                addAnd();
                href += prefix + GoogleScholarQuery.normalize(keyword,'+');
            }
        }
    }

    private void addJournal() {
        String journalName = request.getJournalName();
        if(journalName != null) {
            addAnd();
            href += JOURNAL + GoogleScholarQuery.normalize(journalName,'+');
        }
    }

    private void addSubject() {
        String subjArea = request.getSubjectArea();
        if(subjArea != null) {
            addAnd();
            href += SUBJECT + subjArea;
        }
    }

    private void addAnd() {
        if (!href.equals(DEFAULT_HREF)) {
            href += AND;
        }
    }

    private void addPapersCount() {
        String count = Integer.toString(cfp.getResultsNumber());
        if(count != null && !count.equals("0")) {
            href += PAPERS_COUNT + count;
        }
    }
}
