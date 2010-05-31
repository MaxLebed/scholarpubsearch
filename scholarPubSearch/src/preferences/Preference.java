/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preferences;

import messages.CfpQuery;
import messages.PublicationRequest;
import messages.InformMessage;
import messages.JournalType;
import messages.PersonType;
import messages.Publications;
import messages.Publication;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

public class Preference {
    private static final int CFP_BONUS = 1;
    private static final int INFORM_BONUS = 2;
    private static final int INITIAL_RATING = 1;

    private static final String packageName =
            Preference.class.getPackage().getName();

    private ObjectFactory of;
    private UserPreference userPreference;
    private Hashtable authors;
    private Hashtable sites;
    private Hashtable subjectAreas;
    private Hashtable journals;

    public Preference() {
        of = new ObjectFactory();
        userPreference = of.createUserPreference();
        userPreference.setAuthors(of.createRating());
        userPreference.setJournals(of.createRating());
        userPreference.setSites(of.createRating());
        userPreference.setSubjectAreas(of.createRating());
        makeHash();
    }

    public void setUserPreference(UserPreference up) {
        if (up != null) {
            userPreference = up;
        } else {
            userPreference = new UserPreference();
        }
        if (userPreference.getAuthors() == null) {
            userPreference.setAuthors(of.createRating());
        }
        if (userPreference.getJournals() == null) {
            userPreference.setJournals(of.createRating());
        }
        if (userPreference.getSites() == null) {
            userPreference.setSites(of.createRating());
        }
        if (userPreference.getSubjectAreas() == null) {
            userPreference.setSubjectAreas(of.createRating());
        }
        makeHash();
    }

    //make a hash tables for preferences collection
    private void makeHash() {
        sites = new Hashtable();
        authors = new Hashtable();
        subjectAreas = new Hashtable();
        journals = new Hashtable();
        for (RatingElem re : userPreference.getAuthors().getRatingElem()) {
            authors.put(re.getKey(), re.getValue());
        }
        for (RatingElem re : userPreference.getJournals().getRatingElem()) {
            journals.put(re.getKey(), re.getValue());
        }
        for (RatingElem re : userPreference.getSubjectAreas().getRatingElem()) {
            subjectAreas.put(re.getKey(), re.getValue());
        }
        for (RatingElem re : userPreference.getSites().getRatingElem()) {
            sites.put(re.getKey(), re.getValue());
        }
    }

    //refresh preference information after search session
    public void refreshUserPreference() {
        Rating authorsRating = of.createRating();
        authorsRating.ratingElem = new ArrayList<RatingElem>();
        for (Object author : authors.keySet()) {
            RatingElem a = of.createRatingElem();
            a.setKey(author.toString());
            a.setValue((Integer) authors.get(author));
            authorsRating.ratingElem.add(a);
        }
        userPreference.setAuthors(authorsRating);

        Rating journalsRating = of.createRating();
        journalsRating.ratingElem = new ArrayList<RatingElem>();
        for (Object journal : journals.keySet()) {
            RatingElem j = of.createRatingElem();
            j.setKey(journal.toString());
            j.setValue((Integer) journals.get(journal));
           
            journalsRating.ratingElem.add(j);
        }
        userPreference.setJournals(journalsRating);

        Rating subjectsRating = of.createRating();
        subjectsRating.ratingElem = new ArrayList<RatingElem>();
        for (Object subject : subjectAreas.keySet()) {
            RatingElem s = of.createRatingElem();
            s.setKey(subject.toString());
            s.setValue((Integer) subjectAreas.get(subject));
           
            subjectsRating.ratingElem.add(s);
        }
        userPreference.setSubjectAreas(subjectsRating);
        
        Rating sitesRating = of.createRating();
        sitesRating.ratingElem = new ArrayList<RatingElem>();
        for (Object site : sites.keySet()) {
            RatingElem s = of.createRatingElem();
            s.setKey(site.toString());
            s.setValue((Integer) sites.get(site));
            
            sitesRating.ratingElem.add(s);
        }
        userPreference.setSites(sitesRating);
    }

    //change preferences depends on CFP message
    public void change(CfpQuery cfp) {
        PublicationRequest pr = cfp.getPublicationRequest();
        String subject = pr.getSubjectArea();
        String journal = pr.getJournalName();
        List<String> authorList = pr.getAuthorList().getAttribute();
        changeSubject(subject, CFP_BONUS);
        changeJournal(journal, CFP_BONUS);
        for (String author : authorList) {
            changeAuthor(author, CFP_BONUS);
        }
    }

    //change preferences depends on Inform message
    public void change(InformMessage inform, Publications pubs) {
        if (pubs != null && inform != null) {
            List<Publication> publications = pubs.getPublication();
            List<String> pubIds = inform.getPublicationId();
            inform.getPublicationId();
            for (Publication publication : publications) {
                if (pubIds.contains(publication.getId())) {
                    if (publication.getJournal() != null) {
                        changeJournal(publication.getJournal().getName(),
                                INFORM_BONUS);
                    }
                    changeSubject(publication.getSubjectArea(), INFORM_BONUS);
                    for (PersonType author : publication.getAuthorList().getAuthor()) {
                        if (publication.getAuthorList() != null) {
                            changeAuthor(author.getName(), INFORM_BONUS);
                        }
                    }
                    //TODO in future: change site
                }
            }
        }
    }

    //sort all publications
    public void range(Publications ps, int resultsCount) {
        List<Publication> publications = ps.getPublication();
        List<Pair> pubRanking = new ArrayList<Pair>();
        for (int i = 0; i < publications.size(); i++) {
            Publication publication = publications.get(i);
            int rank = publications.size() - publication.getRatingPosition();
            //calcrank
            JournalType journal = publication.getJournal();
            if (journal != null && journal.getName()!=null &&
                    journals.containsKey(journal.getName().toLowerCase())) {
                rank += (Integer) journals.get(publication.getJournal().getName().toLowerCase());
            }
            String subjArea = publication.getSubjectArea();
            if (subjArea != null && subjectAreas.containsKey(subjArea.toLowerCase())) {
                rank += (Integer) subjectAreas.get(publication.getSubjectArea().toLowerCase());
            }
            int authorsRank = 0;
            Publication.AuthorList authorList = publication.getAuthorList();
            if (authorList != null) {
                for (PersonType author : authorList.getAuthor()) {
                    String name = author.getName();
                    if (name != null && authors.containsKey(name.toLowerCase())) {
                        authorsRank += (Integer) authors.get(name.toLowerCase());
                    }
                }
            }
            rank += authorsRank;
            //TODO : add for sites rating
            pubRanking.add(new Pair(publication, rank));
        }
        Collections.sort(pubRanking, new Comparator<Pair>() {

            @Override
            public int compare(Pair p1, Pair p2) {
                return p2.rank - p1.rank;

            }
        });
        publications.clear();
        for (Pair p : pubRanking) {
            publications.add(p.pub);
        }
        if(resultsCount < publications.size())
            ps.setPublication(publications.subList(0, resultsCount));
        else
            ps.setPublication(publications);
    }

    //change rating of journal
    private void changeJournal(String jnl, int bonus) {
        if (jnl!= null) {
            String journal = jnl.toLowerCase();
            if (journals.containsKey(journal)) {
                int newValue = (Integer) journals.get(journal) + bonus;
                journals.remove(journal);
                journals.put(journal, newValue);
            } else {
                journals.put(journal, INITIAL_RATING);
            }
            String def = userPreference.getDefaultJournal();
            if (def != null) {
                Object oldTop = journals.get(def);
                if (oldTop != null &&
                        (Integer) journals.get(journal) > ((Integer) oldTop)) {
                    userPreference.setDefaultJournal(journal);
                }
            } else {
                userPreference.setDefaultJournal(journal);
            }
        }
    }

    //change rating of author
    private void changeAuthor(String athr, int bonus) {
        if (athr != null) {
            String author = athr.toLowerCase();
            if (authors.containsKey(author)) {
                int newValue = (Integer) authors.get(author) + bonus;
                authors.remove(author);
                authors.put(author, newValue);

            } else {
                authors.put(author, INITIAL_RATING);
            }
            String def = userPreference.getDefaultAuthor();
            if (def != null) {
                Object oldTop = authors.get(def);
                if (oldTop != null &&
                        (Integer) authors.get(author) > ((Integer) oldTop)) {
                    userPreference.setDefaultAuthor(author);
                }
            } else {
                userPreference.setDefaultAuthor(author);
            }
        }
    }
    //change rating of subject area
    private void changeSubject(String sbjt, int bonus) {
        if (sbjt != null) {
            String subject = sbjt.toLowerCase();
            if (subjectAreas.containsKey(subject)) {
                int newValue = (Integer) subjectAreas.get(subject) + bonus;
                subjectAreas.remove(subject);
                subjectAreas.put(subject, newValue);
            } else {
                subjectAreas.put(subject, INITIAL_RATING);
            }
            String def = userPreference.getDefaultSubject();
            if (def != null) {
                Object oldTop = subjectAreas.get(userPreference.getDefaultSubject());
                if (oldTop != null &&
                        (Integer) subjectAreas.get(subject) > ((Integer) oldTop)) {
                    userPreference.setDefaultSubject(subject);
                }
            } else {
                userPreference.setDefaultSubject(subject);
            }
        }
    }

    public String marshal() {
        try {
            JAXBElement<UserPreference> jel = of.createPreference(userPreference);
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Marshaller m = jc.createMarshaller();
            StringWriter sw = new StringWriter();
            m.marshal(jel, sw);
            return sw.getBuffer().toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static UserPreference unmarshal(String pref) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader reader = new StringReader(pref);
        JAXBElement<UserPreference> jel =
                (JAXBElement<UserPreference>) u.unmarshal(reader);
        return jel.getValue();
    }

    //for sort by rating
    private class Pair {

        public Publication pub;
        public int rank;

        public Pair(Publication p, int r) {
            pub = p;
            rank = r;
        }
    }
}
