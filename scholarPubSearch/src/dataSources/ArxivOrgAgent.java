package dataSources;

import messages.CfpQuery;
import messages.Propose;

public class ArxivOrgAgent extends DataSourceAgent {

    public static final String SERVICE_NAME = "ArxivOrg";

    @Override
    public Propose requestDataSource(CfpQuery cfp) {
        ArxivOrgQuery q = new ArxivOrgQuery(cfp);
        return q.perform();
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
