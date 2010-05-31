package dataSources;

import java.io.IOException;
import messages.Propose;

public abstract class DataSourceResponseParser {
    public abstract Propose parse(String href, int resultsNumber) throws IOException;
}
