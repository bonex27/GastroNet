package domainModel.Search;

public class DecoratorSearchDescription extends BaseDecoratorSearch {
    private final String str;

    public DecoratorSearchDescription(Search decoratedSearch, String str) {
        super(decoratedSearch);
        this.str = str;
    }

    // TODO: corrispondenza su singola parola, dividere this.str e iterare
    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND p.name LIKE '%" + this.str + "%'";
    }
}
