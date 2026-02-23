package domainModel.Search;

public class DecoratorSearchName extends BaseDecoratorSearch {
    private final String name;

    public DecoratorSearchName(Search decoratedSearch, String name) {
        super(decoratedSearch);
        this.name = name;
    }

    @Override
    public SearchQuery getSearchQuery() {
        return appendCondition(super.getSearchQuery(), "p.name = ?", this.name);
    }
}
