package dataSources;

import messages.CfpQuery;
import messages.Propose;

public class GoogleScholarAgent extends DataSourceAgent {
    public static final String SERVICE_NAME = "Google Scholar";

    @Override
    public Propose requestDataSource(CfpQuery cfp) {
        GoogleScholarQuery q = new GoogleScholarQuery(cfp);
        return q.perform();
    }

    @Override
    public String getDefaultHref() {
        return GoogleScholarQuery.DEFAULT_HREF;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

}