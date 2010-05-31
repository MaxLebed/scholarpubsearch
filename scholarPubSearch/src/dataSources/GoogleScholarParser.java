package dataSources;

import messages.Publications;
import messages.Publication;
import messages.JournalType;
import messages.PersonType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import atom.DateTimeType;
import messages.Propose;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class GoogleScholarParser extends DataSourceResponseParser{

    public static final String encoding = "ISO-8859-1";
    public static int paperCount;

    public Propose parse(String href, int resultsCount) throws IOException {
        paperCount = resultsCount;
        try {
            GetMethod method = method = request(href);
            ParserGetter getter = new ParserGetter();
            HTMLEditorKit.Parser parser = getter.getParser();
            InputStream in = method.getResponseBodyAsStream();
            InputStreamReader r = new InputStreamReader(in, encoding);
            Outliner callback = new Outliner(new OutputStreamWriter(System.out));
            parser.parse(r, callback, true);
            in.close();
            r.close();
            Publications pubs = readBibtex(callback);
            Propose propose = new Propose();
            propose.setPublications(pubs);
            method.releaseConnection();
            return propose;
        } catch (IOException e) {
            System.err.println("Fatal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //create HTTPClient and request to Google Scholar with cookie
    public GetMethod request(String href) throws IOException{
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(href);
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.setRequestHeader("Cookie", "GSP=ID=88aada13e3ed1b7d:IN=78b3896d5538486c:CF=4:DT=1");
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
        }
        return method;
    }

    //Special GoogleScholar feature
    public static String getBibtexLink(String hash) {
        return "http://scholar.google.com/scholar.bib?q=info:" + hash
                + ":scholar.google.com/&output=citation&hl=en&as_sdt=2000&ct=citation&cd=0";
    }

    //Read paper attributes from BibTex record
    private Publications readBibtex(Outliner otl) throws IOException {
        String[] hashList = otl.getHashList();
        String[] paperList = otl.getPaperList();
        String[] fulltextList = otl.getFulltextList();
        Publications pubs = new Publications();
        ArrayList<Publication> publicationList = new ArrayList<Publication>();
        for (int i = 0; i < hashList.length; i++) {
            String hash = hashList[i];
            if (hash != null) {
                String bibtexLink = getBibtexLink(hash);
                try {
                    GetMethod method = request(bibtexLink);
                    //read attributes from Google HTML page and BibTex-record
                    String responseBibtex = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String line;

                    Publication p = new Publication();
                    p.setJournal(new JournalType());
                    while ((line = reader.readLine()) != null) {
                        responseBibtex += line;
                        readBibtexLine(line, p);
                    }
                    //p.setBibtex(responseBibtex);
                    p.setFulltext(fulltextList[i]);
                    p.setId(paperList[i]);
                    p.setSource(GoogleScholarAgent.SERVICE_NAME);
                    p.setRatingPosition(i);
                    AtomFeedReader.generateBibtexString(p);
                    publicationList.add(p);
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        pubs.setPublication(publicationList);
        return pubs;
    }

    //read one line (one attribute of a publication)
    public static void readBibtexLine(String line, Publication p) {
        String cutComma = line;
        if (cutComma.endsWith(",")) {
            cutComma = cutComma.substring(0, cutComma.length() - 1);
        }
        if (line.startsWith("  title={{")) {
            p.setTitle(cutComma.substring(10, cutComma.length() - 2));
        }
        if (line.startsWith("  author={")) {
            cutComma = cutComma.substring(10, cutComma.length() - 1);
            
            ArrayList<PersonType> authors = new ArrayList<PersonType>();
            for (String authorName : cutComma.split(" and ")) {
                PersonType author = new PersonType();
                author.setName(authorName);
                authors.add(author);
            }
            Publication.AuthorList auth = new Publication.AuthorList();
            auth.setAuthor(authors);
            p.setAuthorList(auth);
        }
        if (line.startsWith("  journal={")) {
            JournalType journal = p.getJournal();
            journal.setName(cutComma.substring(11, cutComma.length() - 1));
            p.setJournal(journal);
        }
        if (line.startsWith("  booktitle={")) {
            JournalType journal = p.getJournal();
            journal.setName(cutComma.substring(13, cutComma.length() - 1));
            p.setJournal(journal);
        }
        if (line.startsWith("  volume={")) {
            JournalType journal = p.getJournal();
            journal.setVolume(cutComma.substring(10, cutComma.length() - 1));
            p.setJournal(journal);
        }
        if (line.startsWith("  number={")) {
            JournalType journal = p.getJournal();
            journal.setNumber(cutComma.substring(10, cutComma.length() - 1));
            p.setJournal(journal);
        }
        if (line.startsWith("  year={")) {
            DateTimeType date = new DateTimeType();
            p.setYear(Integer.decode(cutComma.substring(8, cutComma.length() - 1)));
        }
        if (line.startsWith("  pages={")) {
            cutComma = cutComma.substring(9, cutComma.length() - 1);
            JournalType journal = p.getJournal();
            journal.setPages(cutComma);
            p.setJournal(journal);
            String[] pages = cutComma.split("--");
            if (pages.length > 1) {
                Integer start = Integer.decode(pages[0]);
                Integer end = Integer.decode(pages[1]);
                p.setPagesCount(new Integer(end - start).toString());
            }
        }
    }
}

class Outliner extends HTMLEditorKit.ParserCallback {

    private Writer out;
    private String[] paperList, fulltextList, hashList;
    private String[] titles, authors;
    private String paperLink, fulltextLink, paperHash;
    private boolean isArticle, isPaperLink, isFulltextLink, isRelatedLinks,
            isAuthors;
    private int paperNumber;

    public Outliner(Writer out) {
        this.out = out;
        paperList = new String[GoogleScholarParser.paperCount];
        fulltextList = new String[GoogleScholarParser.paperCount];
        hashList = new String[GoogleScholarParser.paperCount];
        titles = new String[GoogleScholarParser.paperCount];
        authors = new String[GoogleScholarParser.paperCount];
        isArticle = isPaperLink = isFulltextLink = isRelatedLinks = isAuthors
                = false;
        paperNumber = -1;
    }

    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
        if (tag.equals(Tag.DIV)) {
            Object classAttr = attributes.getAttribute(HTML.Attribute.CLASS);
            if (classAttr != null && classAttr.equals("gs_r")) {
                paperNumber++;
                isArticle = true;
            }
        }
        if (tag.equals(Tag.H3)) {
            isPaperLink = true;
            titles [paperNumber] = "";
        }
        if (tag.equals(Tag.SPAN)) {
            Object classAttr = attributes.getAttribute(HTML.Attribute.CLASS);
            if (classAttr != null && classAttr.equals("gs_ggs gs_fl")) {
                isFulltextLink = true;
            }
            if (classAttr != null && classAttr.equals("gs_fl")) {
                isRelatedLinks = true;
            }
            if (classAttr != null && classAttr.equals("gs_a")) {
                isAuthors = true;
                authors[paperNumber] = "";
            }

        }
        if (tag.equals(Tag.A)) {
            if (isPaperLink) {
                paperLink = attributes.getAttribute(HTML.Attribute.HREF).toString();
            }
            if (isFulltextLink) {
                fulltextLink = attributes.getAttribute(HTML.Attribute.HREF).toString();
            }
            if (isRelatedLinks) {
                String s = attributes.getAttribute(HTML.Attribute.HREF).toString();
                if (s.contains("/scholar.bib?q=info")) {
                    paperHash = s.split(":")[1];
                }
            }
        }
    }

    @Override
    public void handleEndTag(HTML.Tag tag, int position) {
        if (tag.equals(Tag.H3) && isPaperLink) {
            isPaperLink = false;
        }
        if (tag.equals(Tag.SPAN) && isFulltextLink) {
            isFulltextLink = false;
        }
        if (tag.equals(Tag.SPAN) && isRelatedLinks) {
            isRelatedLinks = false;
        }
        if (tag.equals(Tag.SPAN) && isAuthors) {
            isAuthors = false;
        }
        if (tag.equals(Tag.DIV) && isArticle) {
            isArticle = false;
            paperList[paperNumber] = paperLink;
            fulltextList[paperNumber] = fulltextLink;
            hashList[paperNumber] = paperHash;
            paperLink = null;
            fulltextLink = null;
            paperHash = null;
        }
    }

    @Override
    public void  handleText(char[] data, int pos) {
        if(isPaperLink) {
            String title = new String(data);
            titles [paperNumber] += title;
        }
        if(isAuthors) {
            String [] authorWords = new String(data).split(" - ");
            authors [paperNumber] +=  authorWords [0];
            if(authorWords.length > 1)
                 isAuthors = false;
        }
    }

    public String[] getHashList() {
        return hashList;
    }

    public String[] getPaperList() {
        return paperList;
    }

    public String[] getFulltextList() {
        return fulltextList;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getTitles() {
        return titles;
    }
}

class ParserGetter extends HTMLEditorKit {

    @Override
    public HTMLEditorKit.Parser getParser() {
        return super.getParser();
    }
}
