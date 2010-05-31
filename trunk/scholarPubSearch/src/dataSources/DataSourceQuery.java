package dataSources;

import java.io.IOException;
import messages.CfpQuery;
import messages.Propose;
import messages.PublicationRequest;

//Translate cfpQuery to data source query language
public abstract class DataSourceQuery {
    public static String href = "";
    public CfpQuery cfp;
    public PublicationRequest request;
    public DataSourceResponseParser parser;
    public Propose perform() {
        calculateHref();
        try {
            return parser.parse(href, cfp.getResultsNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //create link from user request
    public abstract void calculateHref();
}
