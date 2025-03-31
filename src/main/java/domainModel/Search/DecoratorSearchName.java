package domainModel.Search;

public class DecoratorSearchName extends BaseDecoratorSearch {
    private final String name;

    public DecoratorSearchName(Search decoratedSearch, String name) {
        super(decoratedSearch);
        this.name = name;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery()
                + (super.getSearchQuery().endsWith("WHERE") ? " " : " AND ")
                + "p.name = '" + this.name + "'";
    }
}
