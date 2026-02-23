package domainModel.Search;

import java.util.ArrayList;
import java.util.List;

public class DecoratorSearchDescription extends BaseDecoratorSearch {
    private final String[] terms;

    public DecoratorSearchDescription(Search decoratedSearch, String str) {
        super(decoratedSearch);
        if (str == null || str.trim().isEmpty()) {
            this.terms = new String[0];
        } else {
            this.terms = str.trim().split("\\s+");
        }
    }

    @Override
    public SearchQuery getSearchQuery() {
        SearchQuery base = super.getSearchQuery();
        if (terms.length == 0) {
            return base;
        }
        StringBuilder condition = new StringBuilder("(");
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < terms.length; i++) {
            condition.append("p.description LIKE ?");
            params.add("%" + terms[i] + "%");
            if (i < terms.length - 1) {
                condition.append(" OR ");
            }
        }
        condition.append(")");
        return appendCondition(base, condition.toString(), params.toArray());
    }
}
