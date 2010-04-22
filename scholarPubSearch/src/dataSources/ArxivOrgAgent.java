package dataSources;

import messages.CfpQuery;
import messages.Propose;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import atom.FeedType;

public class ArxivOrgAgent extends DataSourceAgent {

    public static final String SERVICE_NAME = "ArxivOrg";

    @Override
    public Propose RequestDataSource(CfpQuery cfp) {
        ArxivOrgQuery q = new ArxivOrgQuery(cfp);
        FeedType ft = q.perform();
        AtomFeedReader reader = new AtomFeedReader(ft);
        return reader.buildPublications();
    }

    @Override
    public String getDefaultHref() {
        return ArxivOrgQuery.DEFAULT_HREF;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
