package domainModel.Search;

import java.util.Collections;
import java.util.List;

public class SearchQuery {
    private final String sql;
    private final List<Object> params;

    public SearchQuery(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params == null ? List.of() : List.copyOf(params);
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParams() {
        return Collections.unmodifiableList(params);
    }
}
