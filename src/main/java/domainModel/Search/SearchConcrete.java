package domainModel.Search;

public class SearchConcrete implements Search {

    private final String query;

    public SearchConcrete() {
        this.query = "SELECT p.* FROM products p WHERE";
    }

    @Override
    public String getSearchQuery() {
        return this.query;
    }
}
