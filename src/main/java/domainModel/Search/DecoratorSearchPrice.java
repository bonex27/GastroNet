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
    public SearchQuery getSearchQuery() {
        return appendCondition(
                super.getSearchQuery(),
                "p.price >= ? AND p.price <= ?",
                this.minPrice,
                this.maxPrice
        );
    }
}
