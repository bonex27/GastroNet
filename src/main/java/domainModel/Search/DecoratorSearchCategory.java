package domainModel.Search;

public class DecoratorSearchCategory extends BaseDecoratorSearch {
    private final String category;

    public DecoratorSearchCategory(Search decoratedSearch, String category) {
        super(decoratedSearch);
        this.category = category;
    }

    @Override
    public SearchQuery getSearchQuery() {
        return appendCondition(super.getSearchQuery(), "p.descCategory = ?", this.category);
    }
}
