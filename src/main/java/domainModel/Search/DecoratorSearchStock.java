package domainModel.Search;

public class DecoratorSearchStock extends BaseDecoratorSearch {
    private final boolean stock;

    public DecoratorSearchStock(Search decoratedSearch, boolean stock) {
        super(decoratedSearch);
        this.stock = stock;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery()
                + (super.getSearchQuery().endsWith("WHERE") ? " " : " AND ")
                + "p.stock " + (this.stock ? ">0" : "=0");
    }
}
