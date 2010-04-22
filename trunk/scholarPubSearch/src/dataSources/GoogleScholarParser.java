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

public class GoogleScholarParser {

    public static final String encoding = "ISO-8859-1";
    public static int paperCount;

    public static Propose parse(String href, int resultsCount) throws IOException {
        /*conn.setRequestProperty("Cookie", "GSP=ID=21c7a62c98ac6c89:IN=78b3896d5538486c:CF=4");
        conn.addRequestProperty("teivltao", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 (.NET CLR 3.5.30729)");*/
        paperCount = resultsCount;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(href);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        //method.setRequestHeader("Cookie", "PREF=ID=c2dbdcf31a660dca:U=522b899d5e4c10a3:LD=en:NR=10:TM=1249930482:LM=1257984277:S=WSlEK41z20sP9d7y; NID=26=gZ45dJbuLvfQR8X_sv5dCN6D-k_MdovJpsa4dNrpAry0IVBZlbaj83zWVucXnbc-Xv3gGGLLF6EKm2f70CDWOWHH0bTBT0qR_85ZLNB339oeHtQz6Oawh_VrGhg23F1d; GSP=ID=c2dbdcf31a660dca:IN=15ea0d06b0148432:CF=4");
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.setRequestHeader("Cookie", "GSP=ID=88aada13e3ed1b7d:IN=78b3896d5538486c:CF=4:DT=1");
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
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
            return propose;
        } catch (Exception e) {
            System.err.println("Fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String getBibtexLink(String hash) {
        return "http://scholar.google.com/scholar.bib?q=info:" + hash
                + ":scholar.google.com/&output=citation&hl=en&as_sdt=2000&ct=citation&cd=0";
    }

    private static Publications readBibtex(Outliner otl) throws IOException {
        String[] hashList = otl.getHashList();
        String[] paperList = otl.getPaperList();
        String[] fulltextList = otl.getFulltextList();
        Publications pubs = new Publications();
        ArrayList<Publication> publicationList = new ArrayList<Publication>();
        for (int i = 0; i < hashList.length; i++) {
            String hash = hashList[i];
            if (hash != null) {
                HttpClient client = new HttpClient();
                String bibtexLink = getBibtexLink(hash);
                GetMethod method = new GetMethod(bibtexLink);
                try {
                    method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
                    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
                    method.setRequestHeader("Cookie", "GSP=ID=88aada13e3ed1b7d:IN=78b3896d5538486c:CF=4:DT=1");
                    int statusCode = client.executeMethod(method);
                    if (statusCode != HttpStatus.SC_OK) {
                        System.err.println("Method failed: " + method.getStatusLine());
                    }
                    String responseBibtex = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String line;

                    Publication p = new Publication();
                    p.setJournal(new JournalType());
                    while ((line = reader.readLine()) != null) {
                        responseBibtex += line;
                        readBibtexLine(line, p);
                    }
                    p.setBibtex(responseBibtex);
                    p.setFulltext(fulltextList[i]);
                    p.setId(paperList[i]);
                    p.setSource(GoogleScholarAgent.SERVICE_NAME);
                    p.setRatingPosition(i);

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
