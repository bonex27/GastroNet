package domainModel.Search;

public class SearchConcrete implements Search {

    private final String query;

    // TODO: filtro disponibilità (stock)
    public SearchConcrete(){
        this.query = "SELECT p.* FROM products p WHERE p.stock>0";
    }

    @Override
    public String getSearchQuery() { return this.query; }
}
