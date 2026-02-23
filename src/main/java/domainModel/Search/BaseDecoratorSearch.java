package domainModel.Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseDecoratorSearch implements Search {
    private final Search decoratedSearch;

    public BaseDecoratorSearch(Search decoratedSearch) {
        this.decoratedSearch = decoratedSearch;
    }

    protected SearchQuery appendCondition(SearchQuery base, String condition, Object... params) {
        String sql = base.getSql();
        String nextSql = sql + (sql.contains(" WHERE ") ? " AND " : " WHERE ") + condition;
        List<Object> nextParams = new ArrayList<>(base.getParams());
        nextParams.addAll(Arrays.asList(params));
        return new SearchQuery(nextSql, nextParams);
    }

    @Override
    public SearchQuery getSearchQuery() {
        return decoratedSearch.getSearchQuery();
    }
}
