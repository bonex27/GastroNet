package dao.query;

public abstract class BaseDecoratorSearch implements Search {
    private final Search decoratedSearch;

    public BaseDecoratorSearch(Search decoratedSearch) {
        this.decoratedSearch = decoratedSearch;
    }

    protected SearchQuery appendCondition(SearchQuery base, String condition, Object... params) {
        String sql = base.getSql();
        String nextSql = sql + (sql.contains(" WHERE ") ? " AND " : " WHERE ") + condition;
        java.util.List<Object> nextParams = new java.util.ArrayList<>(base.getParams());
        nextParams.addAll(java.util.Arrays.asList(params));
        return new SearchQuery(nextSql, nextParams);
    }

    @Override
    public SearchQuery getSearchQuery() {
        return decoratedSearch.getSearchQuery();
    }
}
