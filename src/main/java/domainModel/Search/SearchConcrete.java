package domainModel.Search;

public class SearchConcrete implements Search {

    private final String query;

    public SearchConcrete(boolean stock) {
        this.query = "SELECT p.* FROM products p WHERE p.stock>" + (stock ? "0" : "-1");
    }

    @Override
    public String getSearchQuery() {
        return this.query;
    }
}
