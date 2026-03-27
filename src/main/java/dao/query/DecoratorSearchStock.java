package dao.query;

public class DecoratorSearchStock extends BaseDecoratorSearch {
    private final boolean stock;

    public DecoratorSearchStock(Search decoratedSearch, boolean stock) {
        super(decoratedSearch);
        this.stock = stock;
    }

    @Override
    public SearchQuery getSearchQuery() {
        return appendCondition(super.getSearchQuery(), "p.stock " + (this.stock ? "> 0" : "= 0"));
    }
}
