package domainModel.Search;

public class DecoratorSearchCategory extends BaseDecoratorSearch {
    private final String category;

    public DecoratorSearchCategory(Search decoratedSearch, String category) {
        super(decoratedSearch);
        this.category = category;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery()
                + (super.getSearchQuery().endsWith("WHERE") ? " " : " AND ")
                + "p.id_category = (SELECT c.descCategory FROM categories c WHERE c.category = '" + this.category + "')";
    }
}
