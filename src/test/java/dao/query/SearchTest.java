package dao.query;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SearchTest {

    @Test
    public void baseSearchReturnsDefaultQuery() {
        Search search = new SearchConcrete();
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p", query.getSql());
        Assertions.assertEquals(List.of(), query.getParams());
    }

    @Test
    public void nameDecoratorAddsConditionAndParam() {
        Search search = new DecoratorSearchName(new SearchConcrete(), "Pizza");
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p WHERE p.name = ?", query.getSql());
        Assertions.assertEquals(List.of("Pizza"), query.getParams());
    }

    @Test
    public void categoryDecoratorAddsConditionAndParam() {
        Search search = new DecoratorSearchCategory(new SearchConcrete(), "Drinks");
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p WHERE p.descCategory = ?", query.getSql());
        Assertions.assertEquals(List.of("Drinks"), query.getParams());
    }

    @Test
    public void priceDecoratorAddsRangeAndParams() {
        Search search = new DecoratorSearchPrice(new SearchConcrete(), 1.5f, 9.5f);
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p WHERE p.price >= ? AND p.price <= ?", query.getSql());
        Assertions.assertEquals(List.of(1.5f, 9.5f), query.getParams());
    }

    @Test
    public void stockDecoratorAddsAvailabilityCondition() {
        Search availableSearch = new DecoratorSearchStock(new SearchConcrete(), true);
        SearchQuery availableQuery = availableSearch.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p WHERE p.stock > 0", availableQuery.getSql());
        Assertions.assertEquals(List.of(), availableQuery.getParams());

        Search emptySearch = new DecoratorSearchStock(new SearchConcrete(), false);
        SearchQuery emptyQuery = emptySearch.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p WHERE p.stock = 0", emptyQuery.getSql());
        Assertions.assertEquals(List.of(), emptyQuery.getParams());
    }

    @Test
    public void descriptionDecoratorSplitsTermsAndUsesLike() {
        Search search = new DecoratorSearchDescription(new SearchConcrete(), "fresh spicy");
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals(
                "SELECT p.* FROM Products p WHERE (p.description LIKE ? OR p.description LIKE ?)",
                query.getSql()
        );
        Assertions.assertEquals(List.of("%fresh%", "%spicy%"), query.getParams());
    }

    @Test
    public void descriptionDecoratorSkipsEmptyInput() {
        Search search = new DecoratorSearchDescription(new SearchConcrete(), "   ");
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals("SELECT p.* FROM Products p", query.getSql());
        Assertions.assertEquals(List.of(), query.getParams());
    }

    @Test
    public void chainedDecoratorsAppendConditionsInOrder() {
        Search search = new DecoratorSearchPrice(
                new DecoratorSearchCategory(new SearchConcrete(), "Dessert"),
                2.0f,
                6.0f
        );
        SearchQuery query = search.getSearchQuery();

        Assertions.assertEquals(
                "SELECT p.* FROM Products p WHERE p.descCategory = ? AND p.price >= ? AND p.price <= ?",
                query.getSql()
        );
        Assertions.assertEquals(List.of("Dessert", 2.0f, 6.0f), query.getParams());
    }
}
