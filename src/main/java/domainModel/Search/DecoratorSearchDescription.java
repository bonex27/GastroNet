package domainModel.Search;

import java.util.List;

public class DecoratorSearchDescription extends BaseDecoratorSearch {
    private final String[] str;

    public DecoratorSearchDescription(Search decoratedSearch, String str) {
        super(decoratedSearch);
        this.str = str.split(" ");
    }

    @Override
    public String getSearchQuery() {
        String strOut = super.getSearchQuery() + (super.getSearchQuery().endsWith("WHERE") ? " (" : " AND (");
        for (int i = 0; i < str.length; i++) {
            strOut += "p.name LIKE '%" + str[i] + "%'" + ((i != str.length - 1) ? " OR " : ")");
        }
        return strOut;
    }
}
