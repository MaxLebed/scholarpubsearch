package dataSources;

import atom.ArxivResponse;
import messages.JournalType;
import messages.Propose;
import messages.Publication;
import java.util.List;
import javax.xml.bind.JAXBElement;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import atom.CategoryType;
import atom.DateTimeType;
import atom.EntryType;
import atom.FeedType;
import atom.IdType;
import atom.LinkType;
import atom.PersonType;
import atom.TextType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.bind.JAXBException;
import scholarpubsearch.PublicationListModel;

public class AtomFeedReader extends DataSourceResponseParser{

    private FeedType feed;
    private Propose propose;

    @Override
    public Propose parse(String href, int paperCount) throws IOException{
        try {
            String responseXML = request(href);
            feed = ArxivResponse.unmarshal(responseXML);
            propose = new Propose();
            return readPublications();
        }
        catch (JAXBException je){
            je.printStackTrace();
        }
        return null;
    }

    //HTTP POST to arxiv.org
    public String request(String href) throws IOException {
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
         return responseXML;
    }

    public Propose readPublications() {
        if (feed != null) {
            List<Object> feedChilds = feed.getAuthorOrCategoryOrContributor();
            int ratingPosition = 0;
            for (Object child : feedChilds) {
                if (child.getClass() == JAXBElement.class) {
                    JAXBElement je = (JAXBElement) child;
                    if (je.getName().getLocalPart().endsWith("entry")) {
                        ratingPosition++;
                        EntryType entry = (EntryType) je.getValue();
                        readPublication(entry, ratingPosition);
                    }
                }
            }
        }
        return propose;
    }

    private void readPublication(EntryType entry, int ratingPosition) {
        Publication publn = new Publication();
        publn.setRatingPosition(ratingPosition);
        publn.setSource(ArxivOrgAgent.SERVICE_NAME);
        for (Object child : entry.getAuthorOrCategoryOrContent()) {
            if (child.getClass() == JAXBElement.class) {
                JAXBElement je = (JAXBElement) child;
                String name = je.getName().getLocalPart();
                //read Atom 1.0 <entry></entry> content
                if (name.equals("id")) {
                    readId(publn, je);
                }
                if (name.equals("published")) {
                    readYear(publn, je);
                }
                if (name.equals("title")) {
                    readTitle(publn, je);
                }
                if (name.equals("summary")) {
                    readSummary(publn, je);
                }
                if (name.equals("author")) {
                    readAuthor(publn, je);
                }
                if (name.equals("link")) {
                    readFulltext(publn, je);
                }
                if (name.equals("category")) {
                    readCategory(publn, je);
                }
            }
            //arxiv.org special tags
            else if (child.getClass() == ElementNSImpl.class) {
                ElementNSImpl elem = (ElementNSImpl) child;
                String name = elem.getLocalName();
                if (name.equals("journal_ref")) {
                    readJournal(publn, elem);
                }
                if (name.equals("comment")) {
                    readPagesCount(publn, elem);
                }
            }
        }
        generateBibtexString(publn);
        propose.addPublication(publn);
    }
    //read journal information from <arxiv:comment>
    private void readJournal(Publication publn, ElementNSImpl elem) {
        JournalType journal = new JournalType();
        String[] jouralInfo = elem.getFirstChild().getTextContent().split(" |,");
        String name = "";
        String pages = "";
        String year = "";
        String volume = "";
        for (String infoPart : jouralInfo) {
            try {
                Integer.decode(infoPart);
                if (!volume.equals("")) {
                    pages = infoPart;
                } else {
                    volume = infoPart;
                }
            } catch (NumberFormatException e) {
                if (infoPart.startsWith("(") && infoPart.endsWith(")")) {
                    year = infoPart;
                } else if (!year.equals("") || !volume.equals("")) {
                    pages = infoPart;
                } else if (!infoPart.equals("")) {
                    name += infoPart + " ";
                }
            }
        }
        name = name.substring(0, name.length() - 1);
        journal.setName(name);
        journal.setVolume(volume);
        journal.setPages(pages);

        publn.setJournal(journal);
    }

    private void readPagesCount(Publication publn, ElementNSImpl elem) {
        String comment = elem.getFirstChild().getTextContent();
        String[] commentElems = comment.split(" |,");
        for (int i = 1; i < commentElems.length; i++) {
            String comElem = commentElems[i];
            if (comElem.equals("pages")) {
                try {
                    publn.setPagesCount(commentElems[i - 1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readFulltext(Publication publn, JAXBElement je) {
        LinkType fulltext = (LinkType) je.getValue();
        String title = fulltext.getTitle();
        if (title != null && fulltext.getTitle().equals("pdf")) {
            publn.setFulltext(fulltext.getHref());
        }
    }

    private void readCategory(Publication publn, JAXBElement je) {
        CategoryType cat = (CategoryType) je.getValue();
        publn.setSubjectArea(cat.getTerm());
    }

    private void readSummary(Publication publn, JAXBElement je) {
        TextType summ = (TextType) je.getValue();
        publn.setSummary(summ.getContent().get(0).toString().trim());
    }

    private void readTitle(Publication publn, JAXBElement je) {
        TextType title = (TextType) je.getValue();
        publn.setTitle(title.getContent().get(0).toString());
    }

    private void readYear(Publication publn, JAXBElement je) {
        DateTimeType date = (DateTimeType) je.getValue();
        publn.setYear(date.getValue().getYear());
    }

    private void readAuthor(Publication publn, JAXBElement je) {
        PersonType value = (PersonType) je.getValue();
        messages.PersonType author =
                new messages.PersonType();
        for (Object attr : value.getNameOrUriOrEmail()) {
            if (attr.getClass().equals(JAXBElement.class)) {
                JAXBElement attrElem = (JAXBElement) attr;
                if (attrElem.getName().getLocalPart().equals("name")) {
                    String authorName = attrElem.getValue().toString();
                    author.setName(authorName);
                }
            } else if (attr.getClass().equals(ElementNSImpl.class)) {
                ElementNSImpl elem = (ElementNSImpl) attr;
                if (elem.getLocalName().equals("affiliation")) {
                    String organization = elem.getFirstChild().getTextContent();
                    author.setOrganization(organization);
                }
            }
        }
        Publication.AuthorList authors = publn.getAuthorList();
        authors.addAuthor(author);
        publn.setAuthorList(authors);
    }

    private void readId(Publication publn, JAXBElement je) {
        IdType id = (IdType) je.getValue();
        publn.setId(id.getValue());
    }

    //generate BibTex record
    public static void generateBibtexString(Publication publn) {
        String result = "@article{";
        result += generateBibtexRecordName(publn) + ",\n";
        if(publn.getTitle()!=null)
            result += "  title={{" +publn.getTitle() +"}},\n";
        if(publn.getAuthorList()!=null){
            String authorNames =
                    PublicationListModel.getAuthorString(publn.getAuthorList()).toString();
            if(!authorNames.equals(""))
                result += "  author={" + authorNames + "},\n";
        }
        JournalType journal = publn.getJournal();
        if(journal!=null) {
            if(journal.getName()!=null)
                result += "  journal={" +journal.getName() +"},\n";
            if(journal.getVolume()!=null)
                result += "  volume={" +journal.getVolume() +"},\n";
            if(journal.getNumber()!=null)
                result += "  number={" +journal.getNumber() +"},\n";
            if(journal.getPages()!=null)
                result += "  pages={" +journal.getPages() +"},\n";
        }
        if(publn.getYear()!=null)
            result += "  year={" +publn.getYear().toString() +"},\n";

        if(result.endsWith(",\n")) {
            result = result.substring(0, result.length() - 2);
            result +="\n";
        }
        result += "}";
        publn.setBibtex(result);
    }
    //generate unique name
    private static String generateBibtexRecordName(Publication publn) {
        String name = "";
         if(publn.getAuthorList()!=null){
            messages.PersonType author = publn.getAuthorList().getAuthor().get(0);
            if(author !=null && author.getName() != null) {
                String [] authorWords = getLetters(author.getName()).split(" ");
                if(authorWords.length > 0)
                    name+=authorWords [0];
            }
         }
        if(publn.getYear()!=null)
            name+=publn.getYear().toString();
        if(publn.getTitle()!=null) {
            String [] titleWords = getLetters(publn.getTitle()).split(" ");
            if(titleWords.length > 0)
                name+=titleWords [0];
        }
        if(name.equals(""))
            name += "arxivOrg" + String.valueOf(publn.getRatingPosition());
        return name.toLowerCase();
    }
    //letter filter
    private static String getLetters(String s) {
        char [] letters = s.toCharArray();
        for(int i = 0; i< letters.length; i++) {
            Character c = letters[i];
            if (!Character.isLetter(c))
                letters [i] = ' ';
        }
        return new String(letters);
    }
}
