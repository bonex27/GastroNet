package domainModel.Search;

public class DecoratorSearchPrice extends BaseDecoratorSearch {
    private final float minPrice;
    private final float maxPrice;

    public DecoratorSearchPrice(Search decoratedSearch, float minPrice, float maxPrice) {
        super(decoratedSearch);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND p.price > '" + this.minPrice + "' AND p.price < '" + this.maxPrice + "'";
    }
}
