package domainModel.Search;

public class DecoratorSearchCategory extends BaseDecoratorSearch {
    private final String category;

    public DecoratorSearchCategory(Search decoratedSearch, String category) {
        super(decoratedSearch);
        this.category = category;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND p.id_category = (SELECT c.id_category FROM categories c WHERE c.category = '" + this.category + "')";
    }
}
