package domainModel.Search;

import java.util.List;

public class SearchConcrete implements Search {

    private final SearchQuery query;

    public SearchConcrete() {
        this.query = new SearchQuery("SELECT p.* FROM Products p", List.of());
    }

    @Override
    public SearchQuery getSearchQuery() {
        return query;
    }
}
